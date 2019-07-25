package ir.maxivity.tasbih.reminderTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.azan.Azan;
import com.azan.AzanTimes;
import com.azan.Method;
import com.azan.Time;
import com.azan.astrologicalCalc.SimpleDate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ir.maxivity.tasbih.R;

import static android.content.Context.MODE_PRIVATE;
import static ir.maxivity.tasbih.FullScreenSplash.iranDefaultLat;
import static ir.maxivity.tasbih.FullScreenSplash.iranDefaultLon;
import static ir.maxivity.tasbih.NasimApplication.MAIN_PREF_NAME;
import static ir.maxivity.tasbih.NasimApplication.PREF_USER_LOCATION_KEY;

public class BootReceiver extends BroadcastReceiver {

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;
    private AzanReciever azanReciever;

    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private String mRepeat;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;
    private long mRepeatTime;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    public static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ReminderDatabase rb = new ReminderDatabase(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();
            azanReciever = new AzanReciever();
            List<Reminder> reminders = rb.getAllReminders();
            List<Reminder> azanReminders = rb.getAzanReminders();
            for (Reminder rm : reminders) {

                mReceivedID = rm.getID();
                mRepeat = rm.getRepeat();
                mRepeatNo = rm.getRepeatNo();
                mRepeatType = rm.getRepeatType();
                mActive = rm.getActive();
                mDate = rm.getDate();
                mTime = rm.getTime();

                mDateSplit = mDate.split("/");
                mTimeSplit = mTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                mCalendar.set(Calendar.MONTH, --mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                // Cancel existing notification of the reminder by using its ID
                // mAlarmReceiver.cancelAlarm(context, mReceivedID);

                // Check repeat type
                if (mRepeatType.equals("Minute")) {
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                } else if (mRepeatType.equals("Hour")) {
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
                } else if (mRepeatType.equals("Day")) {
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                } else if (mRepeatType.equals("Week")) {
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                } else if (mRepeatType.equals("Month")) {
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                }

                // Create a new notification
                if (mActive.equals("true")) {
                    if (mRepeat.equals("true")) {
                        mAlarmReceiver.setRepeatAlarm(context, mCalendar, mReceivedID, mRepeatTime);
                    } else if (mRepeat.equals("false")) {
                        mAlarmReceiver.setAlarm(context, mCalendar, mReceivedID);
                    }
                }
            }

            AzanReciever reciever = new AzanReciever();
            SimpleDate today = new SimpleDate(new GregorianCalendar());
            for (Reminder reminder : azanReminders) {

                if (reminder.getTitle().equals(context.getString(R.string.azan_fajr))) {
                    reciever.setAlarm(context, setAzanTime(today, getAzanTimes(context).fajr()), reminder.getID());
                } else if (reminder.getTitle().equals(context.getString(R.string.azan_zohr))) {
                    reciever.setAlarm(context, setAzanTime(today, getAzanTimes(context).thuhr()), reminder.getID());
                } else if (reminder.getTitle().equals(context.getString(R.string.azan_asr))) {
                    reciever.setAlarm(context, setAzanTime(today, getAzanTimes(context).assr()), reminder.getID());
                } else if (reminder.getTitle().equals(context.getString(R.string.azan_maqrib))) {
                    reciever.setAlarm(context, setAzanTime(today, getAzanTimes(context).maghrib()), reminder.getID());
                } else if (reminder.getTitle().equals(context.getString(R.string.azan_ishaa))) {
                    reciever.setAlarm(context, setAzanTime(today, getAzanTimes(context).ishaa()), reminder.getID());
                }
            }


        }
    }

    private String getUserLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USER_LOCATION_KEY, null);
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
        com.azan.astrologicalCalc.Location location = new com.azan.astrologicalCalc.Location(lat, lon, 4.5, 0);
        Azan azan = new Azan(location, Method.Companion.getMUSLIM_LEAGUE());
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
}
