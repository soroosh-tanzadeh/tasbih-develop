package ir.maxivity.tasbih.calendarTabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;

public class EventsList extends Fragment {

    LinearLayout view;
    TextView textView;

    private List<CalendarEvent> eventsCalendar;
    private ArrayList<String> events;
    private final static String EVENT_LIST_KEY = "EVENTS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = new LinearLayout(getContext());
        this.view.setOrientation(LinearLayout.VERTICAL);
        this.view.setPadding(64,64,64,64);
        textView = new TextView(getContext());
        this.view.addView(textView);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEventTexts(events);

    }


    public void setEventTexts(ArrayList<String> events) {
        StringBuilder event = new StringBuilder();
        if (events != null) {
            if (!events.isEmpty()) {
                for (int i = 0; i < events.size(); i++) {
                    event.append(events.get(i));
                    event.append("\n");
                }
            }
        }
        textView.setText(event.toString());

    }
}
