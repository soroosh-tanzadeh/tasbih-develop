package ir.maxivity.tasbih.calendarTabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.PersianDateSerializable;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.CivilDate;
import ir.mirrajabi.persiancalendar.core.models.IslamicDate;
import ir.mirrajabi.persiancalendar.helpers.DateConverter;

public class Today_tab extends Fragment {

    View view;

    private PersianDateSerializable date;
    private final static String DATE_KEY = "DATE";
    TextView cday;
    TextView cyear;
    TextView cmounth;
    TextView aday;
    TextView amonth;
    TextView ayear;
    TextView daytext;


        String[] iMonthNames = { "محرم", "صفر", "ربیع‌الاول",
                "ربیع‌الاخر", "جمادی‌الاول", "جمادی ‌الاخر", "رجب",
                "شعبان", "رمضان", "شوال", "ذی‌القعده", "ذی‌الحجه"};

        String[] eMonthNames = {
                "jan", "feb", "mar", "apr", "may", "jum", "jul", "aug", "sep", "oct", "nov", "dec"
        };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_calendar_tab, container, false);
        initViews();
        return this.view;
    }

    public static Today_tab newInstance(PersianDateSerializable date) {
        Today_tab today_tab = new Today_tab();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATE", date);
        today_tab.setArguments(bundle);
        return today_tab;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = (PersianDateSerializable) getArguments().getSerializable(DATE_KEY);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(date);
    }


    private void initViews() {
        cday = this.view.findViewById(R.id.interday);
        cyear = this.view.findViewById(R.id.inter_year);
        cmounth = this.view.findViewById(R.id.inter_month);
        aday = this.view.findViewById(R.id.arabic_day);
        amonth = this.view.findViewById(R.id.arabic_month);
        ayear = this.view.findViewById(R.id.arabic_year);
        daytext = this.view.findViewById(R.id.today_text);
    }

    public void setupViews(PersianDateSerializable date) {
        if (date != null) {
            PersianCalendarHandler calendar = PersianCalendarHandler.getInstance(getContext());
            daytext.setText(calendar.getWeekDayName(date.date) + " " + calendar.formatNumber(date.date.getDayOfMonth())
                    + " " + calendar.getMonthName(date.date));
            Log.v("FUCK TODAy : ", calendar.getWeekDayName(date.date) + " " + calendar.formatNumber(date.date.getDayOfMonth())
                    + " " + calendar.getMonthName(date.date));
            CivilDate civilDate = DateConverter.persianToCivil(date.date);
            IslamicDate islamicDate = DateConverter.persianToIslamic(date.date);

            cday.setText(calendar.formatNumber(civilDate.getDayOfMonth()) + "");
            cmounth.setText(eMonthNames[civilDate.getMonth() - 1]);
            cyear.setText(calendar.formatNumber(civilDate.getYear()) + "");

            aday.setText(calendar.formatNumber(islamicDate.getDayOfMonth()) + "");
            amonth.setText(iMonthNames[islamicDate.getMonth() - 1]);
            ayear.setText(calendar.formatNumber(islamicDate.getYear()) + "");
        }
    }

}
