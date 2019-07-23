package ir.maxivity.tasbih;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ir.maxivity.tasbih.activities.SelectLanguageActivity;
import ir.maxivity.tasbih.dataAccess.LocalDB;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullScreenSplash extends BaseActivity {
    private static int oppend = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_splash);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if (application.getUserId() != null) {
                    Intent intent = new Intent(FullScreenSplash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(FullScreenSplash.this, SelectLanguageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }

    public void decide(LocalDB localDB){
        if (!localDB.isLogedin() && !localDB.isLoginanothertime()){
            FullScreenSplash.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FullScreenSplash.this, SelectLanguageActivity.class);
                    FullScreenSplash.oppend = 1;
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            FullScreenSplash.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FullScreenSplash.this,MainActivity.class);
                    FullScreenSplash.oppend = 1;
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

}
