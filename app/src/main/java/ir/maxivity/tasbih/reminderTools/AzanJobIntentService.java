package ir.maxivity.tasbih.reminderTools;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.HashMap;

public class AzanJobIntentService extends JobIntentService {
    private static final int AZAN_JOB_ID = 2000;
    static HashMap<String, Object> azanContextMap = new HashMap<>();

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, AzanJobIntentService.class, AZAN_JOB_ID, intent);
        azanContextMap.put("context", ctx);

    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AzanReciever reciever = new AzanReciever();
        reciever.setAzanNotification(intent, (Context) azanContextMap.get("context"));

    }
}
