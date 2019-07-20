package ir.maxivity.tasbih.reminderTools;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class AzanJobIntentService extends JobIntentService {
    private static final int AZAN_JOB_ID = 2000;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, AzanJobIntentService.class, AZAN_JOB_ID, intent);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AzanReciever reciever = new AzanReciever();
        reciever.setAzanNotification(intent, context);
    }
}
