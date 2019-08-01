package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

import ir.maxivity.tasbih.activities.FavoritePlacesActivity;
import ir.maxivity.tasbih.activities.MyPlacesActivity;
import ir.maxivity.tasbih.activities.ReminderActivity;
import ir.maxivity.tasbih.adapters.DrawerListAdapter;
import ir.maxivity.tasbih.adapters.QuranAdyehAdapter;
import ir.maxivity.tasbih.tools.CreateDrawerItem;
import tools.Utilities;

public class ZekrsList extends BaseActivity {


    private JSONArray zekrs;
    private DrawerLayout drawerLayout;

    @SuppressLint({"ResourceAsColor", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_drawer_layout);

        try {
            zekrs = new JSONArray(readJsonFromAssets());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);
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

        drawerLayout = findViewById(R.id.drawer_layout);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        initViews();

    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.quran_recycler);
        EditText search = findViewById(R.id.search_surah_input);

        final QuranAdyehAdapter adapter = new QuranAdyehAdapter(this, zekrs);
        recyclerView.setAdapter(adapter);


        adapter.setOnSurahClickListener(new QuranAdyehAdapter.OnSurahClick() {
            @Override
            public void onClick(int id, String name) {
                Intent i = new Intent(ZekrsList.this, ZekrCounter.class);
                i.putExtra("code", id + "");
                i.putExtra("zekrname", name);
                startActivity(i);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ListView drawerItemList = findViewById(R.id.drawer_item_list);

        final CreateDrawerItem items = new CreateDrawerItem(this, getString(R.string.guest_user), application.getLoginLater());

        DrawerListAdapter drawerAdapter = new DrawerListAdapter(this, R.layout.drawer_item_layout, items.getItems());

        drawerItemList.setAdapter(drawerAdapter);

        drawerItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (items.getItems().get(i).getText() == getString(R.string.favorite)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(ZekrsList.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(ZekrsList.this, FavoritePlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.sign_up)) {
                    finish();
                    Intent intent = new Intent(ZekrsList.this, Login.class);
                    startActivity(intent);
                }
                if (items.getItems().get(i).getText() == getString(R.string.my_places)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(ZekrsList.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(ZekrsList.this, MyPlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.reminder)) {
                    Intent intent = new Intent(ZekrsList.this, ReminderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private String readJsonFromAssets() {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open("zekrs.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
