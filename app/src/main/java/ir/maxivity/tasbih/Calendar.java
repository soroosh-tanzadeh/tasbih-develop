package ir.maxivity.tasbih;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.maxivity.tasbih.calendarTabs.EventsList;
import ir.maxivity.tasbih.calendarTabs.SectionsPagerAdapter;
import ir.maxivity.tasbih.calendarTabs.Today_tab;
import ir.maxivity.tasbih.models.PersianDateSerializable;
import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.interfaces.OnDayClickedListener;
import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;


public class Calendar extends BaseActivity {

    PersianCalendarHandler calendar;
    private ViewPager viewPager;
    private EventsList events_frag;
    private Today_tab todayTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        viewPager = findViewById(R.id.calendar_viewer);

        final LinearLayout l = findViewById(R.id.tab_container);
        this.overridePendingTransition(R.anim.slide_right,
                R.anim.fixed_anime);
        final PersianCalendarView persianCalendarView = findViewById(R.id.persian_calendar);
        final PersianCalendarHandler calendar = persianCalendarView.getCalendar();
        this.calendar = calendar;
        final PersianDate today = calendar.getToday(); // get today date

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
                todayTab.setupViews(new PersianDateSerializable(date));
                List<CalendarEvent> eventsList = calendar.getAllEventsForDay(date);
                ArrayList<String> events = new ArrayList<>();

                for (CalendarEvent event : eventsList) {
                    events.add(event.getTitle());
                }
                events_frag.setEventTexts(events);
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

}

