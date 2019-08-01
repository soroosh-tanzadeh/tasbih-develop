package ir.maxivity.tasbih;

import android.app.ActionBar;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.IOException;

import ir.maxivity.tasbih.dataAccess.DataFileAccess;
import ir.maxivity.tasbih.dataAccess.ZekrData;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import tools.Utilities;

public class ZekrCounter extends BaseActivity {

    int zekr_num = 0;
    String zekr_code;
    String zekr_name;
    MediaPlayer mediaPlayer;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_counter_drawer_layout);

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

        drawerLayout = findViewById(R.id.drawer_layout);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.tasbih);
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.zekr_code = getIntent().getExtras().getString("code", "");
        this.zekr_name = getIntent().getExtras().getString("zekrname");

        DataFileAccess dataFileAccess = new DataFileAccess(this);
        try {
            ZekrData zd = (ZekrData) dataFileAccess.readObject("zekr_"+zekr_code);
            if (zd != null){
               zekr_num = zd.getZekrnum();
            }
        } catch (IOException e) {
            Log.e("Reading Zekr",e.getMessage());
            zekr_num = 0;
        } catch (ClassNotFoundException e) {
            Log.e("Reading Zekr",e.getMessage());
            zekr_num = 0;
        }

        ConstraintLayout zekrcounter = findViewById(R.id.countzekr_btn);
        final TextView zekrnum = findViewById(R.id.zekrnum);
        Button zekrsbtn = findViewById(R.id.zekrsbtn);
        Button resetzekrbtn = findViewById(R.id.resetzekrbtn);
        TextView zekrname = findViewById(R.id.zekrname);
        zekrname.setText(this.zekr_name);
        final PersianCalendarHandler ph = PersianCalendarHandler.getInstance(this);
        zekrnum.setText(ph.formatNumber(zekr_num));
        zekrcounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                zekr_num++;
                zekrnum.setText(ph.formatNumber(zekr_num));
            }
        });
        zekrsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        resetzekrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zekr_num = 0;
                zekrnum.setText(ph.formatNumber(zekr_num));
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        mediaPlayer.stop();
        mediaPlayer.release();
        DataFileAccess dataFileAccess = new DataFileAccess(this);
        ZekrData zd = new ZekrData();
        zd.setZekr_code(zekr_code);
        zd.setZekrnum(zekr_num);
        try {
            dataFileAccess.writeObject(zd,"zekr_"+zekr_code);
        } catch (IOException e) {
            Log.e("Writing Zekr",e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
