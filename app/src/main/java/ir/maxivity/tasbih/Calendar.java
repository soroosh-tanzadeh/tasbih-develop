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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final ViewPager viewPager = findViewById(R.id.calendar_viewer);

        final LinearLayout l = findViewById(R.id.tab_container);
        this.overridePendingTransition(R.anim.slide_right,
                R.anim.fixed_anime);
        final PersianCalendarView persianCalendarView = findViewById(R.id.persian_calendar);
        final PersianCalendarHandler calendar = persianCalendarView.getCalendar();
        this.calendar = calendar;
        final PersianDate today = calendar.getToday(); // get today date

        setupViewerPager(viewPager,today,calendar); // setup pageview

        final TabLayout tabLayout = findViewById(R.id.calendar_tabs);
        tabLayout.setupWithViewPager(viewPager);


        final int tabIconColor = ContextCompat.getColor(Calendar.this, R.color.white);

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_today_tab);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_events_tab);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
               //         int tabIconColor = ContextCompat.getColor(Calendar.this, R.color.white);
             //           tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
  //                      int tabIconColor = ContextCompat.getColor(Calendar.this, R.color.unselectedcolor);
//                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                });

        persianCalendarView.setOnDayClickedListener(new OnDayClickedListener() {
            @Override
            public void onClick(final PersianDate date) {
                // Setup Page View with selected date
                setupViewerPager(viewPager,date,calendar);


                // Select Today Tab
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_today_tab);
                tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_events_tab);
                tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

            }
        });
    }


    private void setupViewerPager(ViewPager viewPager,PersianDate date,PersianCalendarHandler pch){
        SectionsPagerAdapter sectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        /*Today_tab todayTab = new Today_tab();
        todayTab.setPersinaDate(date);*/

        List<CalendarEvent> eventsList = pch.getAllEventsForDay(date);

        ArrayList<String> events = new ArrayList<>();

        for (CalendarEvent event : eventsList) {
            events.add(event.getTitle());
        }

        EventsList events_frag = EventsList.newIstance(events);

        Today_tab today_tab = Today_tab.newInstance(new PersianDateSerializable(date));


        Log.v("FUCK CALENDAR : ", calendar.getWeekDayName(date) + " " + calendar.formatNumber(date.getDayOfMonth())
                + " " + calendar.getMonthName(date));
        ////// Adding Fragments to Pager Adapter
        sectionPagerAdapter.addFragment(events_frag,"مناسبت ها");
        sectionPagerAdapter.addFragment(today_tab, "روز");
        viewPager.setAdapter(sectionPagerAdapter);


     /*   events_frag.setEventTexts(events);
        today_tab.setupViews(new PersianDateSerializable(date));*/

        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_to_right);
    }

}

