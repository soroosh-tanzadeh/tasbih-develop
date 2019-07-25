package ir.maxivity.tasbih.reminderTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.maxivity.tasbih.tools.AzanPlayer;

public class NotificationDismissReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AzanPlayer.getInstance(context).stop();
    }
}
