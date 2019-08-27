package ir.maxivity.tasbih.reminderTools;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.azan.Azan;
import com.azan.AzanTimes;
import com.azan.Method;
import com.azan.Time;
import com.azan.astrologicalCalc.SimpleDate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ir.maxivity.tasbih.MainActivity;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.tools.AzanPlayer;
import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;

import static android.content.Context.MODE_PRIVATE;
import static ir.maxivity.tasbih.FullScreenSplash.iranDefaultLat;
import static ir.maxivity.tasbih.FullScreenSplash.iranDefaultLon;
import static ir.maxivity.tasbih.NasimApplication.MAIN_PREF_NAME;
import static ir.maxivity.tasbih.NasimApplication.PREF_USER_LOCATION_KEY;

public class AlarmReceiver extends BroadcastReceiver {
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    Context context;
    public static final String CUSTOM_INTENT = "ir.maxivity.tasbih.intent.action.ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        MyJobIntentService.enqueueWork(context.getApplicationContext(), intent);
    }


    public void setNotification(Intent intent, Context context) {
        int mReceivedID = Integer.parseInt(intent.getStringExtra(ir.maxivity.tasbih.Calendar.EXTRA_REMINDER_ID));

        try {
            ReminderDatabase rb = new ReminderDatabase(context);
            Reminder reminder = rb.getReminder(mReceivedID);
            List<Reminder> azanReminders = rb.getAzanReminders();
            String mTitle = reminder.getTitle();
            AzanPlayer player = AzanPlayer.getInstance(context);
            int icon = R.drawable.ic_alarm;
            if (reminder.getActive().equals("false")) {
                player.play();
                icon = R.drawable.tasbihlogo_transparent;
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                mTitle = reminder.getTitle() + " " + "در ساعت : " + hour + ":" + minute;
            } else {
                icon = R.drawable.ic_alarm;
            }

            Intent homeIntent = new Intent(context, MainActivity.class);
            homeIntent.putExtra(ir.maxivity.tasbih.Calendar.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
            PendingIntent mClick = PendingIntent.getActivity(context, mReceivedID, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent deletInetent = new Intent(context, NotificationDismissReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, deletInetent, 0);

            // Create Notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Reminder")
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(icon)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setTicker(mTitle)
                    .setContentText(mTitle)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setDeleteIntent(pendingIntent)
                    .setOnlyAlertOnce(true);

            if (!reminder.getTitle().equals(context.getString(R.string.refresh_key))) {
                refreshAlarm(azanReminders);
                NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nManager.notify(mReceivedID, mBuilder.build());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void refreshAlarm(List<Reminder> azanReminders) {
        SimpleDate today = new SimpleDate(new GregorianCalendar());
        for (Reminder reminder : azanReminders) {
            if (reminder.getTitle().equals(context.getString(R.string.azan_fajr))) {
                setAlarm(context, setAzanTime(today, getAzanTimes(context).fajr()), reminder.getID());
            } else if (reminder.getTitle().equals(context.getString(R.string.azan_zohr))) {
                setAlarm(context, setAzanTime(today, getAzanTimes(context).thuhr()), reminder.getID());
            } else if (reminder.getTitle().equals(context.getString(R.string.azan_asr))) {
                setAlarm(context, setAzanTime(today, getAzanTimes(context).assr()), reminder.getID());
            } else if (reminder.getTitle().equals(context.getString(R.string.azan_maqrib))) {
                setAlarm(context, setAzanTime(today, getAzanTimes(context).maghrib()), reminder.getID());
            } else if (reminder.getTitle().equals(context.getString(R.string.azan_ishaa))) {
                setAlarm(context, setAzanTime(today, getAzanTimes(context).ishaa()), reminder.getID());
            }
        }
    }

    private AzanTimes getAzanTimes(Context context) {
        SimpleDate today = new SimpleDate(new GregorianCalendar());
        double lat = iranDefaultLat;
        double lon = iranDefaultLon;
        try {
            lat = Double.parseDouble(getUserLocation(context).split(",")[0]);
            lon = Double.parseDouble(getUserLocation(context).split(",")[1]);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        PersianCalendarView calendarView = new PersianCalendarView(context);
        PersianCalendarHandler calendarHandler = calendarView.getCalendar();
        PersianDate todaydate = calendarHandler.getToday();

        int month = todaydate.getMonth();
        double gmtDiff = 3.5;
        if (month <= 6) {
            gmtDiff = 4.5;
        }
        com.azan.astrologicalCalc.Location location = new com.azan.astrologicalCalc.Location(lat, lon, gmtDiff, 0);
        Azan azan = new Azan(location, Method.Companion.getKARACHI_SHAF());
        return azan.getPrayerTimes(today);
    }

    private Calendar setAzanTime(SimpleDate date, Time azantime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, azantime.getHour());
        calendar.set(Calendar.MINUTE, azantime.getMinute());
        calendar.set(Calendar.SECOND, azantime.getSecond());
        return calendar;
    }


    private String getUserLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USER_LOCATION_KEY, null);
    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ir.maxivity.tasbih.Calendar.EXTRA_REMINDER_ID, Integer.toString(ID));
        intent.setAction(CUSTOM_INTENT);
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + diffTime,
                mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ir.maxivity.tasbih.Calendar.EXTRA_REMINDER_ID, Integer.toString(ID));
        intent.setAction(CUSTOM_INTENT);
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification timein
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using initial notification time and repeat interval time
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                RepeatTime, mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel Alarm using Reminder ID
        mPendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        mAlarmManager.cancel(mPendingIntent);

        // Disable alarm
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
