package ir.maxivity.tasbih.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.Calendar;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.ReminderAdapter;
import ir.maxivity.tasbih.reminderTools.AlarmReceiver;
import ir.maxivity.tasbih.reminderTools.Reminder;
import ir.maxivity.tasbih.reminderTools.ReminderDatabase;
import tools.Utilities;

public class ReminderActivity extends BaseActivity {

    RecyclerView recyclerView;
    ReminderDatabase reminderDatabase;
    List<Reminder> reminders = new ArrayList<>();
    ReminderAdapter adapter;
    AlarmReceiver alarmReceiver;
    LinearLayout emptyList;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        reminderDatabase = new ReminderDatabase(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);

        settings_btn.setVisibility(View.GONE);
        TextView englishDate = actionbarview.findViewById(R.id.english_date_text);
        persianDate.setText(Utilities.getTodayJalaliDate(this));
        arabicDate.setText(Utilities.getTodayIslamicDate(this));
        englishDate.setText(Utilities.getTodayGregortianDate(this));


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });

        add = findViewById(R.id.add_reminder);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, Calendar.class));
            }
        });

        recyclerView = findViewById(R.id.reminder_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminders = reminderDatabase.getAllReminders();
        emptyList = findViewById(R.id.empty_list);

        alarmReceiver = new AlarmReceiver();

        if (reminders.size() > 0)
            adapter = new ReminderAdapter(this, reminders);
        else
            emptyList.setVisibility(View.VISIBLE);

        try {
            recyclerView.setAdapter(adapter);
            adapter.setOnDeleteListener(new ReminderAdapter.OnDeleteClick() {
                @Override
                public void onDelete(String id) {
                    deleteReminder(Integer.parseInt(id));
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void deleteReminder(int id) {
        Reminder reminder = reminderDatabase.getReminder(id);

        reminderDatabase.deleteReminder(reminder);

        //adapter.removeItem(reminder);

        adapter.updateList(reminderDatabase.getAllReminders());

        if (reminderDatabase.getAllReminders().size() == 0) {
            emptyList.setVisibility(View.VISIBLE);
        }

        alarmReceiver.cancelAlarm(this, id);
    }
}
