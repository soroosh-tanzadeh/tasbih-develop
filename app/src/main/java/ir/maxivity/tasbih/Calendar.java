package ir.maxivity.tasbih;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ir.maxivity.tasbih.calendarTabs.EventsList;
import ir.maxivity.tasbih.calendarTabs.SectionsPagerAdapter;
import ir.maxivity.tasbih.calendarTabs.Today_tab;
import ir.maxivity.tasbih.fragments.AddReminderDialogFragment;
import ir.maxivity.tasbih.models.PersianDateSerializable;
import ir.maxivity.tasbih.reminderTools.AlarmReceiver;
import ir.maxivity.tasbih.reminderTools.Reminder;
import ir.maxivity.tasbih.reminderTools.ReminderDatabase;
import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.interfaces.OnDayClickedListener;
import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;
import ir.mirrajabi.persiancalendar.core.models.CivilDate;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.mirrajabi.persiancalendar.helpers.DateConverter;
import tools.Utilities;


public class Calendar extends BaseActivity implements AddReminderDialogFragment.OnSubmitClick {

    private static final String TAG = "FUCK CALENDAR";
    private static final String DIALOG_TAG = "ADD REMINDER";
    public static final String EXTRA_REMINDER_ID = "reminder_id";
    PersianCalendarHandler calendar;
    private ViewPager viewPager;
    private EventsList events_frag;
    private Today_tab todayTab;
    private PersianDate currentSelectedDate;
    private FloatingActionButton addEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        viewPager = findViewById(R.id.calendar_viewer);
        addEvent = findViewById(R.id.add_event);

        final LinearLayout l = findViewById(R.id.tab_container);
        this.overridePendingTransition(R.anim.slide_right,
                R.anim.fixed_anime);
        final PersianCalendarView persianCalendarView = findViewById(R.id.persian_calendar);
        final PersianCalendarHandler calendar = persianCalendarView.getCalendar();
        this.calendar = calendar;
        final PersianDate today = calendar.getToday(); // get today date
        currentSelectedDate = today;
        setupViewerPager(today, calendar); // setup pageview

        final TabLayout tabLayout = findViewById(R.id.calendar_tabs);
        tabLayout.setupWithViewPager(viewPager);


        final int tabIconColor = ContextCompat.getColor(Calendar.this, R.color.white);

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_today_tab);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_events_tab);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        persianCalendarView.setOnDayClickedListener(new OnDayClickedListener() {
            @Override
            public void onClick(final PersianDate date) {
                currentSelectedDate = date;
                todayTab.setupViews(new PersianDateSerializable(date));
                List<CalendarEvent> eventsList = calendar.getAllEventsForDay(date);
                ArrayList<String> events = new ArrayList<>();

                for (CalendarEvent event : eventsList) {
                    events.add(event.getTitle());
                }
                events_frag.setEventTexts(events);
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CivilDate date = DateConverter.persianToCivil(currentSelectedDate);
                Date d = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDayOfMonth()).getTime();

                DialogFragment dialogFragment = AddReminderDialogFragment.newInstance(Utilities.
                        getJalaliDate(Calendar.this, currentSelectedDate));
                dialogFragment.show(getSupportFragmentManager(), DIALOG_TAG);

                Log.v(TAG, d.getTime() + "");

            }
        });
    }


    private void setupViewerPager(PersianDate date, PersianCalendarHandler pch) {
        SectionsPagerAdapter sectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        List<CalendarEvent> eventsList = pch.getAllEventsForDay(date);

        ArrayList<String> events = new ArrayList<>();

        for (CalendarEvent event : eventsList) {
            events.add(event.getTitle());
        }

        events_frag = EventsList.newIstance(events);

        todayTab = Today_tab.newInstance(new PersianDateSerializable(date));


        Log.v("FUCK CALENDAR : ", calendar.getWeekDayName(date) + " " + calendar.formatNumber(date.getDayOfMonth())
                + " " + calendar.getMonthName(date));
        ////// Adding Fragments to Pager Adapter
        sectionPagerAdapter.addFragment(events_frag,"مناسبت ها");
        sectionPagerAdapter.addFragment(todayTab, "روز");
        viewPager.setAdapter(sectionPagerAdapter);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_to_right);
    }

    @Override
    public void onAddReminderClick(String message, String time) {
        setReminder(message, time);
        calendar.addLocalEvent(new CalendarEvent(currentSelectedDate, message, false));
        Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }

    }

    public void setReminder(String message, String time) {
        ReminderDatabase rb = new ReminderDatabase(this);
        CivilDate civilDate = DateConverter.persianToCivil(currentSelectedDate);


        String setDate = civilDate.getDayOfMonth()
                + "/" + (civilDate.getMonth())
                + "/" + civilDate.getYear();
        Log.v("ALARM", setDate);
        int hour = 8;
        int min = 0;
        try {
            hour = Integer.parseInt(time.split(":")[0]);
            min = Integer.parseInt(time.split(":")[1]);

            if (min < 10) {
                time = hour + ":" + "0" + min;
            }
        } catch (Exception e) {
            time = "8:00";
        }


        int ID = rb.addReminder(new Reminder(message, setDate, time));

        java.util.Calendar mCalendar = java.util.Calendar.getInstance();
        long milMinute = 60000L;

        mCalendar.set(java.util.Calendar.MONTH, civilDate.getMonth() - 1);
        mCalendar.set(java.util.Calendar.YEAR, civilDate.getYear());
        mCalendar.set(java.util.Calendar.DAY_OF_MONTH, civilDate.getDayOfMonth());
        mCalendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(java.util.Calendar.MINUTE, min);
        mCalendar.set(java.util.Calendar.SECOND, 0);

        Log.v("ALARM", mCalendar.getTime() + "");
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setRepeatAlarm(this, mCalendar, ID, 10 * milMinute);

        showShortToast("یاد آور با موفقیت ذخیره شد");
    }
}

