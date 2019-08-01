package ir.maxivity.tasbih;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.azan.Azan;
import com.azan.AzanTimes;
import com.azan.Method;
import com.azan.Time;
import com.azan.astrologicalCalc.SimpleDate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ir.maxivity.tasbih.activities.SelectLanguageActivity;
import ir.maxivity.tasbih.dataAccess.DataFileAccess;
import ir.maxivity.tasbih.dataAccess.LocalDB;
import ir.maxivity.tasbih.reminderTools.AlarmReceiver;
import ir.maxivity.tasbih.reminderTools.BootReceiver;
import ir.maxivity.tasbih.reminderTools.Reminder;
import ir.maxivity.tasbih.reminderTools.ReminderDatabase;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullScreenSplash extends BaseActivity {
    private static int oppend = 0;
    public static final double iranDefaultLat = 32.4279;
    public static final double iranDefaultLon = 53.6880;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean setAlarms = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_splash);

        final DataFileAccess dataFileAccess = new DataFileAccess(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalDB localDB = dataFileAccess.readLocalDB();
                    if (localDB != null) {

                    } else {
                        localDB = new LocalDB();
                        dataFileAccess.writeLocalDB(localDB);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        if (application.getAzanRefresh() == 0L) {
            application.setAzanRefreshKey(today);
            setAlarms = true;
        } else if (daysBetween(application.getAzanRefresh(), today) < 5) {
            Log.v("FUCK REFRESH", daysBetween(application.getAzanRefresh(), today) + "");
            setAlarms = false;
        } else {
            setAlarms = true;
        }

        try {
            if (setAlarms)
                setAzanReminders();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void navigateToNext() {
        if (application.getToken() != null) {
            Intent intent = new Intent(FullScreenSplash.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(FullScreenSplash.this, SelectLanguageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startReceivingLocationUpdates();
    }

    public void startReceivingLocationUpdates() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        saveUserLocation();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                navigateToNext();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    @SuppressLint("MissingPermission")
    private void saveUserLocation() {

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    application.setUserLocation(location.getLatitude() + "," + location.getLongitude());
                }
            }
        });


    }

    private java.util.Calendar setAzanTime(Date date, Time azantime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (passedTime(azantime, date)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(java.util.Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(java.util.Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(java.util.Calendar.HOUR_OF_DAY, azantime.getHour());
        calendar.set(java.util.Calendar.MINUTE, azantime.getMinute());
        calendar.set(Calendar.SECOND, azantime.getSecond());
        return calendar;
    }

    private boolean passedTime(Time azantime, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > azantime.getHour()) {
            return true;
        } else {
            return false;
        }
    }

    private void addAzanRemindersToDatabase() {
        ReminderDatabase rb = new ReminderDatabase(this);
        String time = "";
        String date = "";

        int sobId = rb.addReminder(new Reminder(getString(R.string.azan_fajr), date, time, false));
        int zohrId = rb.addReminder(new Reminder(getString(R.string.azan_zohr), date, time, false));
        int asrId = rb.addReminder(new Reminder(getString(R.string.azan_asr), date, time, false));
        int maqribId = rb.addReminder(new Reminder(getString(R.string.azan_maqrib), date, time, false));
        int ishaId = rb.addReminder(new Reminder(getString(R.string.azan_ishaa), date, time, false));

        String id = sobId + "," + zohrId + "," + asrId + "," + maqribId + "," + ishaId;
        application.setAzanReminderIds(id);
    }

    public void setAzanReminders() throws NumberFormatException {
        String id = "";
        if (application.getReminderIds() != null) {
            id = application.getReminderIds();
        } else {
            addAzanRemindersToDatabase();
            id = application.getReminderIds();
        }

        SimpleDate today = new SimpleDate(new GregorianCalendar());
        Calendar date = Calendar.getInstance();
        double lat = iranDefaultLat;
        double lon = iranDefaultLon;
        try {
            lat = Double.parseDouble(application.getUserLocation().split(",")[0]);
            lon = Double.parseDouble(application.getUserLocation().split(",")[1]);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        com.azan.astrologicalCalc.Location location = new com.azan.astrologicalCalc.Location(lat, lon, 4.5, 0);
        Azan azan = new Azan(location, Method.Companion.getMUSLIM_LEAGUE());
        AzanTimes azanTimes = azan.getPrayerTimes(today);
        AlarmReceiver reciever = new AlarmReceiver();
        String[] speceficId = id.split(",");


        reciever.setRepeatAlarm(this, setAzanTime(date.getTime(), azanTimes.fajr()), Integer.parseInt(speceficId[0]), BootReceiver.milDay);
        reciever.setRepeatAlarm(this, setAzanTime(date.getTime(), azanTimes.thuhr()), Integer.parseInt(speceficId[1]), BootReceiver.milDay);
        reciever.setRepeatAlarm(this, setAzanTime(date.getTime(), azanTimes.assr()), Integer.parseInt(speceficId[2]), BootReceiver.milDay);
        reciever.setRepeatAlarm(this, setAzanTime(date.getTime(), azanTimes.maghrib()), Integer.parseInt(speceficId[3]), BootReceiver.milDay);
        reciever.setRepeatAlarm(this, setAzanTime(date.getTime(), azanTimes.ishaa()), Integer.parseInt(speceficId[4]), BootReceiver.milDay);
        application.setAzanRefreshKey(date.getTimeInMillis());
    }
}
