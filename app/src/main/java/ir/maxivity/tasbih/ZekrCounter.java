package ir.maxivity.tasbih;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import ir.maxivity.tasbih.dataAccess.DataFileAccess;
import ir.maxivity.tasbih.dataAccess.ZekrData;
import ir.maxivity.tasbih.tools.HeaderNav;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;

public class ZekrCounter extends AppCompatActivity {

    int zekr_num = 0;
    String zekr_code;
    String zekr_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zekr_counter);

        this.zekr_code = getIntent().getExtras().getString("code");
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
        HeaderNav hn = new HeaderNav();
        hn.loadHeadernav(this);

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
