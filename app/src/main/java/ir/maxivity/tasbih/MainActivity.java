package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import org.osmdroid.config.Configuration;

import co.ronash.pushe.Pushe;
import ir.maxivity.tasbih.tools.BottomNavigationViewHelper;
import ir.maxivity.tasbih.tools.CustomGestureDetector;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushe.initialize(this,true);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.main_drawer_layout);
        final Fragment start = new Start_freg();
        final Fragment home = new Podcasts();
        final Fragment map = new Map();

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        drawerLayout = findViewById(R.id.drawer_layout);


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "تسبیح");
                    String shareMessage = "\nLاپلیکیشن تسبیح را دانلود کنید\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(MainActivity.this, Sidebar.class);
                startActivity(i);*/
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
                        BottomSheetDialogFragment fragment = new BottomSheet();
                        fragment.show(getSupportFragmentManager(), "TAG");
                        return false;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_start);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
