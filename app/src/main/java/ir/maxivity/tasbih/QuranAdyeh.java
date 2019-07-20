package ir.maxivity.tasbih;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ir.maxivity.tasbih.calendarTabs.SectionsPagerAdapter;
import ir.maxivity.tasbih.fragments.AdyehFragment;
import ir.maxivity.tasbih.fragments.QuranFragment;

public class QuranAdyeh extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_adyeh);
        /*HeaderNav hn = new HeaderNav();
        hn.loadHeadernav(this);*/
        getSupportActionBar().setTitle("قرآن و ادعیه");

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
