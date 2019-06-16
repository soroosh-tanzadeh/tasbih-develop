package ir.maxivity.tasbih.calendarTabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;

public class EventsList extends Fragment {

    LinearLayout view;

    private List<CalendarEvent> events;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = new LinearLayout(getContext());
        this.view.setOrientation(LinearLayout.VERTICAL);
        this.view.setPadding(64,64,64,64);
        return this.view;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (events != null) {
            if (!events.isEmpty()) {
                for (int i = 0; i < events.size(); i++) {
                    TextView t = new TextView(getContext());
                    t.setText(events.get(i).getTitle());
                    this.view.addView(t);
                }
            }
        }
    }
}
