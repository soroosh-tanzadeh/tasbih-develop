package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.DrawerItem;


public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {
    private int resourceLayout;
    private Context mContext;

    public DrawerListAdapter(@NonNull Context context, int resource, List<DrawerItem> items) {
        super(context, resource, items);
        this.mContext = context;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        if (root == null) {
            root = LayoutInflater.from(mContext).inflate(R.layout.drawer_item_layout, parent, false);
        }
        DrawerItem item = getItem(position);

        TextView itemText = root.findViewById(R.id.drawer_item_text);
        ImageView itemIcon = root.findViewById(R.id.drawer_item_icon);

        itemText.setText(item.getText());
        itemIcon.setBackgroundResource(item.getDrawable());

        return root;
    }
}
