package ir.maxivity.tasbih.reminderTools;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.HashMap;

public class MyJobIntentService extends JobIntentService {
    private static final int JOB_ID = 1000;
    static HashMap<String, Object> contextMap = new HashMap<>();

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, MyJobIntentService.class, JOB_ID, intent);
        contextMap.put("context", ctx);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setNotification(intent, (Context) contextMap.get("context"));
    }
}
