package ir.maxivity.tasbih.reminderTools;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import ir.maxivity.tasbih.MainActivity;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.tools.AzanPlayer;

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
            String mTitle = reminder.getTitle();
            AzanPlayer player = AzanPlayer.getInstance(context);
            if (reminder.getActive().equals("false")) {
                player.play();
            }

            Intent homeIntent = new Intent(context, MainActivity.class);
            homeIntent.putExtra(ir.maxivity.tasbih.Calendar.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
            PendingIntent mClick = PendingIntent.getActivity(context, mReceivedID, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent deletInetent = new Intent(context, NotificationDismissReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, deletInetent, 0);

            // Create Notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Reminder")
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setTicker(mTitle)
                    .setContentText(mTitle)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setDeleteIntent(pendingIntent)
                    .setOnlyAlertOnce(true);

            NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(mReceivedID, mBuilder.build());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

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
