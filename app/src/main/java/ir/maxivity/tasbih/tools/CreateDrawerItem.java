package ir.maxivity.tasbih.tools;

import android.content.Context;

import java.util.ArrayList;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.DrawerItem;

public class CreateDrawerItem {

    ArrayList<DrawerItem> items = new ArrayList<>();

    public CreateDrawerItem(Context context, String userName, boolean signUp) {
        items.add(new DrawerItem(R.drawable.user, userName));
        items.add(new DrawerItem(R.drawable.ic_fave, context.getString(R.string.favorite)));
        items.add(new DrawerItem(R.drawable.marker2, context.getString(R.string.my_places)));
        items.add(new DrawerItem(R.drawable.alarm_blur, context.getString(R.string.reminder)));
        items.add(new DrawerItem(R.drawable.ic_settings_blue, context.getString(R.string.settings)));
        if (signUp)
            items.add(new DrawerItem(R.drawable.ic_login, context.getString(R.string.sign_up)));
        items.add(new DrawerItem(R.drawable.ic_logout, context.getString(R.string.logout)));

    }

    public ArrayList<DrawerItem> getItems() {
        return items;
    }

    public void setItems(DrawerItem item) {
        items.add(item);
    }

}
