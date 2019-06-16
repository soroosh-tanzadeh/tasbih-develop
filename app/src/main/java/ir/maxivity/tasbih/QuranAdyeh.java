package ir.maxivity.tasbih;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.maxivity.tasbih.calendarTabs.SectionsPagerAdapter;
import ir.maxivity.tasbih.tools.HeaderNav;

public class QuranAdyeh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_adyeh);
        HeaderNav hn = new HeaderNav();
        hn.loadHeadernav(this);
        ViewPager viewPager = findViewById(R.id.qa_viewerpager);
        TabLayout tabLayout = findViewById(R.id.qa_tablayout);

        setupViewerPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupViewerPager(ViewPager vp){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new Quran_frag(),"اعدیه");
        sectionsPagerAdapter.addFragment(new Quran_frag(),"قرآن");
        vp.setAdapter(sectionsPagerAdapter);
    }
}
