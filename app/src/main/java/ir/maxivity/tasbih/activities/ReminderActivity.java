package ir.maxivity.tasbih.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.ReminderAdapter;
import ir.maxivity.tasbih.reminderTools.AlarmReceiver;
import ir.maxivity.tasbih.reminderTools.Reminder;
import ir.maxivity.tasbih.reminderTools.ReminderDatabase;

public class ReminderActivity extends BaseActivity {

    RecyclerView recyclerView;
    ReminderDatabase reminderDatabase;
    List<Reminder> reminders = new ArrayList<>();
    ReminderAdapter adapter;
    AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        reminderDatabase = new ReminderDatabase(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        recyclerView = findViewById(R.id.reminder_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminders = reminderDatabase.getAllReminders();

        alarmReceiver = new AlarmReceiver();

        adapter = new ReminderAdapter(this, reminders);
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteListener(new ReminderAdapter.OnDeleteClick() {
            @Override
            public void onDelete(String id) {
                deleteReminder(Integer.parseInt(id));
            }
        });
    }


    public void deleteReminder(int id) {
        Reminder reminder = reminderDatabase.getReminder(id);

        reminderDatabase.deleteReminder(reminder);

        //adapter.removeItem(reminder);

        adapter.updateList(reminderDatabase.getAllReminders());

        alarmReceiver.cancelAlarm(this, id);
    }
}
