package ir.maxivity.tasbih;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import tools.Alert;
import tools.Compass;
import tools.LocationService;
import tools.Utilities;


public class
Qiblah_compass extends BaseActivity {

    private TextView degree;
    private ImageView compassImage;
    private LinearLayout compassWrapper;
    private float currentAzimuth = 0f;
    private Compass compass;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qiblah_compass);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);

        settings_btn.setVisibility(View.GONE);
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

        degree = findViewById(R.id.qiblah_dir);
        compassImage = findViewById(R.id.qiblah_compass);
        compassWrapper = findViewById(R.id.compass_wrapper);

        setupCompass();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationService.locationRcode);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocationService.locationRcode + 10);
        } else {
            LocationService locationService = new LocationService();
            LocationService.LocationResult locationResult = new LocationService.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    ShowQiblah(location);
                }
            };
            if (!locationService.getLocation(this, locationResult)) {
                Alert.ShowAlert("خطا", "دسترسی به موقعیت وجود ندارد", this);
            }
        }

    }

    private void setupCompass() {
        compass = new Compass(this);
        Compass.CompassListener listener = new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) {
                adjustView(azimuth);
            }
        };

        compass.setListener(listener);
    }

    private void adjustView(float azimuth) {
        Animation animator = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        currentAzimuth = azimuth;

        animator.setDuration(500);
        animator.setRepeatCount(0);
        animator.setFillAfter(true);

        compassWrapper.startAnimation(animator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    private void smoothRotation(final int degree) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (degree > 0) {
                    for (int i = 0; i != degree; i++) {
                        try {
                            Thread.sleep(10);
                            final int finalI = i;
                            Qiblah_compass.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    compassWrapper.setRotation(finalI);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    for (int i = 0; i != degree; i--) {
                        try {
                            Thread.sleep(10);
                            final int finalI = i;
                            Qiblah_compass.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    compassWrapper.setRotation(finalI);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        t.start();
    }

    private void ShowQiblah(Location location) {
        if (location != null) {
            double lon2 = 29.907371;
            double lat2 = 31.184603;

            double lon1 = location.getLongitude();
            double lat1 = location.getLatitude();

            double lonDelta = (lon2 - lon1);
            double y = Math.sin(lonDelta) * Math.cos(lat2);
            double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lonDelta);
            double brng = Math.atan2(y, x);
            final double toDeg = (brng * 180) / Math.PI;

            final String Qiblah = String.valueOf(Math.round(toDeg));

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    degree.setText(Qiblah);
                    // smoothRotation((int) toDeg);
                }
            });
        }
    }


}