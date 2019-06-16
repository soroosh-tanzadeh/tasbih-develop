package ir.maxivity.tasbih.calendarTabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.maxivity.tasbih.R;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.CivilDate;
import ir.mirrajabi.persiancalendar.core.models.IslamicDate;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.mirrajabi.persiancalendar.helpers.DateConverter;

public class Today_tab extends Fragment {

    View view;

    private PersianDate date;


        String[] iMonthNames = { "محرم", "صفر", "ربیع‌الاول",
                "ربیع‌الاخر", "جمادی‌الاول", "جمادی ‌الاخر", "رجب",
                "شعبان", "رمضان", "شوال", "ذی‌الغعده", "ذی‌الحجه" };

        String[] eMonthNames = {
                "ژانویه", "فوریه", "مارس", "آوریل", "مه", "ژوئن", "ژوئیه", "اوت","سپتامبر","اکتبر","نوامبر","دسامبر"
        };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_calendar_tab, container, false);
        return this.view;
    }

    public void setPersinaDate(PersianDate date) {
        this.date = date;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (date != null) {
            final TextView daytext = view.findViewById(R.id.today_text);
            PersianCalendarHandler calendar = PersianCalendarHandler.getInstance(getContext());

            daytext.setText(calendar.getWeekDayName(this.date) + " " + calendar.formatNumber(this.date.getDayOfMonth())
                    + " " + calendar.getMonthName(this.date));

            final TextView cday = this.view.findViewById(R.id.interday);
            final TextView cyear = this.view.findViewById(R.id.inter_year);
            final TextView cmounth = this.view.findViewById(R.id.inter_month);

            final TextView aday = this.view.findViewById(R.id.arabic_day);
            final TextView amonth = this.view.findViewById(R.id.arabic_month);
            final TextView ayear = this.view.findViewById(R.id.arabic_year);

            CivilDate civilDate = DateConverter.persianToCivil(this.date);
            IslamicDate islamicDate = DateConverter.persianToIslamic(this.date);

            cday.setText(calendar.formatNumber(civilDate.getDayOfMonth()) + "");
            cmounth.setText(eMonthNames[civilDate.getMonth()]);
            cyear.setText(calendar.formatNumber(civilDate.getYear()) + "");

            aday.setText(calendar.formatNumber(islamicDate.getDayOfMonth()) + "");
            amonth.setText(iMonthNames[islamicDate.getMonth()]);
            ayear.setText(calendar.formatNumber(islamicDate.getYear()) + "");
        }
    }

    public static class EventsList extends Fragment {

    }
}
