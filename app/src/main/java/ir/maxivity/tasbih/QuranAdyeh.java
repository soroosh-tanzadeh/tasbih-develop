package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ir.maxivity.tasbih.calendarTabs.SectionsPagerAdapter;
import ir.maxivity.tasbih.fragments.AdyehFragment;
import ir.maxivity.tasbih.fragments.QuranFragment;
import tools.Utilities;

public class QuranAdyeh extends BaseActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quran_adyeh_drawer_layout);
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

        drawerActions();

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });


        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        ViewPager viewPager = findViewById(R.id.qa_viewerpager);
        TabLayout tabLayout = findViewById(R.id.qa_tablayout);

        setupViewerPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupViewerPager(ViewPager vp){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new AdyehFragment(), getString(R.string.adyeh));
        sectionsPagerAdapter.addFragment(new QuranFragment(), getString(R.string.quran));
        vp.setAdapter(sectionsPagerAdapter);
        vp.setCurrentItem(1);
    }
}
