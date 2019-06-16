package ir.maxivity.tasbih;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import tools.Alert;
import tools.LocationService;


public class
Qiblah_compass extends AppCompatActivity implements SensorEventListener {

    TextView degree;
    ImageView compass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qiblah_compass);
        degree = findViewById(R.id.qiblah_dir);
        compass = findViewById(R.id.qiblah_compass);
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
                                    compass.setRotation(finalI);
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
                                    compass.setRotation(finalI);
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
                    smoothRotation((int) toDeg);
                }
            });
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}