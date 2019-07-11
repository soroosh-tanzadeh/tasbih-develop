package ir.maxivity.tasbih;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import ir.maxivity.tasbih.activities.SelectLanguageActivity;
import ir.maxivity.tasbih.dataAccess.DataFileAccess;
import ir.maxivity.tasbih.dataAccess.LocalDB;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullScreenSplash extends AppCompatActivity {
    private static int oppend = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_splash);
        final DataFileAccess dataFileAccess = new DataFileAccess(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalDB localDB = dataFileAccess.readLocalDB();
                    if (localDB != null){
                        decide(localDB);
                    }else {
                        localDB = new LocalDB();
                        dataFileAccess.writeLocalDB(localDB);
                        decide(localDB);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        if (FullScreenSplash.oppend == 0) {
            t.start();
        }else{
            Intent intent = new Intent(FullScreenSplash.this,MainActivity.class);
            FullScreenSplash.oppend = 1;
            startActivity(intent);
        }

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
