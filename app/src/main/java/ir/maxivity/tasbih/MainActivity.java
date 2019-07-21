package ir.maxivity.tasbih;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.azan.Azan;
import com.azan.AzanTimes;
import com.azan.Method;
import com.azan.Time;
import com.azan.astrologicalCalc.SimpleDate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.osmdroid.config.Configuration;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.maxivity.tasbih.activities.FavoritePlacesActivity;
import ir.maxivity.tasbih.activities.MyPlacesActivity;
import ir.maxivity.tasbih.activities.ReminderActivity;
import ir.maxivity.tasbih.adapters.DrawerListAdapter;
import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;
import ir.maxivity.tasbih.reminderTools.AzanReciever;
import ir.maxivity.tasbih.reminderTools.Reminder;
import ir.maxivity.tasbih.reminderTools.ReminderDatabase;
import ir.maxivity.tasbih.tools.BottomNavigationViewHelper;
import ir.maxivity.tasbih.tools.CreateDrawerItem;
import ir.maxivity.tasbih.tools.CustomGestureDetector;
import tools.Utilities;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerItemList;
    private FusedLocationProviderClient fusedLocationClient;


    // Iran location
    public static final double iranDefaultLat = 32.4279;
    public static final double iranDefaultLon = 53.6880;

    //generate alarm Ids
    public static final int FAJR_ID = 3000;
    public static final int ZOHR_ID = 3001;
    public static final int ASR_ID = 3002;
    public static final int MAQRIB_ID = 3003;
    public static final int ISHAA_ID = 3004;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushe.initialize(this,true);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        setContentView(R.layout.main_drawer_layout);
        final Fragment start = new Start_freg();
        final Fragment home = new Podcasts();
        final Fragment map = new Map();

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);

        persianDate.setText(Utilities.getTodayJalaliDate(this));
        arabicDate.setText(Utilities.getTodayIslamicDate(this));

        drawerLayout = findViewById(R.id.drawer_layout);


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "تسبیح");
                    String shareMessage = "\nLاپلیکیشن تسبیح را دانلود کنید\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(MainActivity.this, Sidebar.class);
                startActivity(i);*/
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        CustomGestureDetector mCustomGestureDetectorForUpperLayout = new CustomGestureDetector(bottomNavigationView) {
            @Override
            public void onLeftToRightSwap() {

            }

            @Override
            public void onRightToLeftSwap() {

            }

            @Override
            public void onTopToBottomSwap() {
                //Toast.makeText(MainActivity.this, "onTopToBottomSwap", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBottomToTopSwap() {
                //Toast.makeText(MainActivity.this, "onBottomToTopSwap", Toast.LENGTH_SHORT).show();
                BottomSheetDialogFragment fragment = new BottomSheet();
                fragment.show(getSupportFragmentManager(), "TAG");
            }

            @Override
            public void onLeftToRightTopToBottomDiagonalSwap() {

            }

            @Override
            public void onLeftToRightBottomToTopDiagonalSwap() {

            }

            @Override
            public void onRightToLeftTopToBottomDiagonalSwap() {

            }

            @Override
            public void onRightToLeftBottomToTopDiagonalSwap() {

            }

            @Override
            public void onSingleTap() {

            }

            @Override
            public void onDoubleTap() {

            }

            @Override
            public void onLongPressPerformed(MotionEvent e) {

            }
        };

        final GestureDetector
                mGestureDetectorUpperLayout = new GestureDetector(mCustomGestureDetectorForUpperLayout);
        bottomNavigationView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetectorUpperLayout.onTouchEvent(motionEvent);
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_start:
                        loadFragment(start);
                        return true;
                    case R.id.navigation_home:
                        loadFragment(home);
                        return true;
                    case R.id.navigation_map:
                        loadFragment(map);
                        return true;
                    case R.id.navigation_detail:
                        try {
                            BottomSheetDialogFragment fragment = new BottomSheet();
                            fragment.show(getSupportFragmentManager(), "TAG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_start);
        initViews();
    }

    private void initViews() {
        drawerItemList = findViewById(R.id.drawer_item_list);

        final CreateDrawerItem items = new CreateDrawerItem(this, "guest", application.getLoginLater());

        DrawerListAdapter adapter = new DrawerListAdapter(this, R.layout.drawer_item_layout, items.getItems());

        drawerItemList.setAdapter(adapter);

        drawerItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (items.getItems().get(i).getText() == getString(R.string.favorite)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(MainActivity.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, FavoritePlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.sign_up)) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                if (items.getItems().get(i).getText() == getString(R.string.my_places)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(MainActivity.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, MyPlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.reminder)) {
                    Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        startReceivingLocationUpdates();
    }

    boolean doubleBackPressed = false;

    private void doubleBackPress() {
        if (doubleBackPressed) {
            System.exit(0);
            return;
        }
        this.doubleBackPressed = true;
        showShortToast(getString(R.string.double_back_pressed));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackPressed = false;
            }
        }, 2000);
    }


    public void startReceivingLocationUpdates() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        saveUserLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    private void saveUserLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    application.setUserLocation(location.getLatitude() + "," + location.getLongitude());
                    try {
                        setAzanReminders();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i) instanceof BaseFragment) {
                handled = ((BaseFragment) fragmentList.get(i)).onBackPressed();

                if (handled) {
                    break;
                }
            }
        }
        if (!handled) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else
                doubleBackPress();

        }


    }

    public void setAzanReminders() throws NumberFormatException {
        String id = "";
        if (application.getReminderIds() != null) {
            id = application.getReminderIds();
            Log.v("FUCK MAIN", id);
        } else {
            addAzanRemindersToDatabase();
        }

        SimpleDate today = new SimpleDate(new GregorianCalendar());
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
        AzanReciever reciever = new AzanReciever();
        String[] speceficId = id.split(",");
        reciever.setAlarm(this, setAzanTime(today, azanTimes.fajr()), Integer.parseInt(speceficId[0]));
        /*reciever.setAlarm(this, setAzanTime(today, azanTimes.thuhr()), Integer.parseInt(speceficId[1]));
        reciever.setAlarm(this, setAzanTime(today, azanTimes.assr()), Integer.parseInt(speceficId[2]));
        reciever.setAlarm(this, setAzanTime(today, azanTimes.maghrib()), Integer.parseInt(speceficId[3]));
        reciever.setAlarm(this, setAzanTime(today, azanTimes.ishaa()), Integer.parseInt(speceficId[4]));
*/
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

    private Calendar setAzanTime(SimpleDate date, Time azantime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, azantime.getHour());
        calendar.set(Calendar.MINUTE, azantime.getMinute());
        calendar.set(Calendar.SECOND, azantime.getSecond());
        return calendar;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
