package ir.maxivity.tasbih.calendarTabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;

public class EventsList extends Fragment {

    LinearLayout view;

    private List<CalendarEvent> eventsCalendar;
    private ArrayList<String> events;
    private final static String EVENT_LIST_KEY = "EVENTS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = new LinearLayout(getContext());
        this.view.setOrientation(LinearLayout.VERTICAL);
        this.view.setPadding(64,64,64,64);
        return this.view;
    }

    public static EventsList newIstance(ArrayList<String> events) {
        EventsList eventsList = new EventsList();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EVENT_LIST_KEY, events);
        eventsList.setArguments(bundle);

        return eventsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = new ArrayList<>();
        if (getArguments() != null) {
            events = getArguments().getStringArrayList(EVENT_LIST_KEY);
        }
    }

    public void setEventsCalendar(List<CalendarEvent> eventsCalendar) {
        this.eventsCalendar = eventsCalendar;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEventTexts(events);

    }


    public void setEventTexts(ArrayList<String> events) {


        Log.v("FUCK Event : ", events.get(0));
        if (events != null) {
            if (!events.isEmpty()) {
                for (int i = 0; i < events.size(); i++) {
                    TextView t = new TextView(getContext());
                    t.setText(events.get(i));
                    this.view.addView(t);
                }
            }
        }
    }
}
