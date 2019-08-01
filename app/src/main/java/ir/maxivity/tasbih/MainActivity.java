package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.osmdroid.config.Configuration;

import java.util.List;

import co.ronash.pushe.Pushe;
import ir.maxivity.tasbih.activities.FavoritePlacesActivity;
import ir.maxivity.tasbih.activities.MyPlacesActivity;
import ir.maxivity.tasbih.activities.ReminderActivity;
import ir.maxivity.tasbih.activities.SettingActivity;
import ir.maxivity.tasbih.adapters.DrawerListAdapter;
import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;
import ir.maxivity.tasbih.tools.BottomNavigationViewHelper;
import ir.maxivity.tasbih.tools.CreateDrawerItem;
import ir.maxivity.tasbih.tools.CustomGestureDetector;
import tools.Utilities;

public class MainActivity extends BaseActivity implements BottomSheet.clickListener {

    private DrawerLayout drawerLayout;
    private ListView drawerItemList;
    private FusedLocationProviderClient fusedLocationClient;
    private BottomNavigationView bottomNavigationView;
    final Fragment start = new Start_freg();
    final Fragment home = new Podcasts();
    final Fragment map = new Map();


    // Iran location


    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushe.initialize(this,true);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        setContentView(R.layout.main_drawer_layout);


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

        drawerLayout = findViewById(R.id.drawer_layout);


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        CustomGestureDetector mCustomGestureDetectorForUpperLayout = new CustomGestureDetector(bottomNavigationView) {
            @Override
            public void onLeftToRightSwap() {

            }

            @Override
            public void onRightToLeftSwap() {

            }

            @Override
            public void onTopToBottomSwap() {
                //Toast.makeText(MainActivity.this, "onTopToBottomSwap", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBottomToTopSwap() {
                //Toast.makeText(MainActivity.this, "onBottomToTopSwap", Toast.LENGTH_SHORT).show();
                BottomSheetDialogFragment fragment = new BottomSheet();
                fragment.show(getSupportFragmentManager(), "TAG");
            }

            @Override
            public void onLeftToRightTopToBottomDiagonalSwap() {

            }

            @Override
            public void onLeftToRightBottomToTopDiagonalSwap() {

            }

            @Override
            public void onRightToLeftTopToBottomDiagonalSwap() {

            }

            @Override
            public void onRightToLeftBottomToTopDiagonalSwap() {

            }

            @Override
            public void onSingleTap() {

            }

            @Override
            public void onDoubleTap() {

            }

            @Override
            public void onLongPressPerformed(MotionEvent e) {

            }
        };

        final GestureDetector
                mGestureDetectorUpperLayout = new GestureDetector(mCustomGestureDetectorForUpperLayout);
        bottomNavigationView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetectorUpperLayout.onTouchEvent(motionEvent);
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_start:
                        loadFragment(start);
                        return true;
                    case R.id.navigation_home:
                        loadFragment(home);
                        return true;
                    case R.id.navigation_map:
                        loadFragment(map);
                        return true;
                    case R.id.navigation_detail:
                        try {
                            BottomSheetDialogFragment fragment = new BottomSheet();
                            fragment.show(getSupportFragmentManager(), "TAG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_start);
        initViews();
    }

    private void initViews() {
        drawerItemList = findViewById(R.id.drawer_item_list);

        String user = "";
        if (application.getToken() != null) {
            user = application.getUserId();
        } else {
            user = getString(R.string.guest_user);
        }
        final CreateDrawerItem items = new CreateDrawerItem(this, user, application.getLoginLater());

        DrawerListAdapter adapter = new DrawerListAdapter(this, R.layout.drawer_item_layout, items.getItems());

        drawerItemList.setAdapter(adapter);

        drawerItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (items.getItems().get(i).getText() == getString(R.string.favorite)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(MainActivity.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, FavoritePlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.sign_up)) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                if (items.getItems().get(i).getText() == getString(R.string.my_places)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(MainActivity.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, MyPlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.reminder)) {
                    Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                    startActivity(intent);
                }

                if (items.getItems().get(i).getText() == getString(R.string.settings)) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    boolean doubleBackPressed = false;

    private void doubleBackPress() {
        if (doubleBackPressed) {
            System.exit(0);
            return;
        }
        this.doubleBackPressed = true;
        showShortToast(getString(R.string.double_back_pressed));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackPressed = false;
            }
        }, 2000);
    }




    @Override
    public void onBackPressed() {

        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i) instanceof BaseFragment) {
                handled = ((BaseFragment) fragmentList.get(i)).onBackPressed();

                if (handled) {
                    break;
                }
            }
        }
        if (!handled) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else
                doubleBackPress();

        }


    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onMapClick() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_map);
        loadFragment(map);
    }
}
