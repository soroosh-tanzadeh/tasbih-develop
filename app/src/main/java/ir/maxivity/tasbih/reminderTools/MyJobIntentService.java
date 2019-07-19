package ir.maxivity.tasbih.reminderTools;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class MyJobIntentService extends JobIntentService {
    private static final int JOB_ID = 1000;
    private static Context context;

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, MyJobIntentService.class, JOB_ID, intent);
        context = ctx;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setNotification(intent, context);
    }
}
