package ir.maxivity.tasbih.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import ir.maxivity.tasbih.R;

public class AzanPlayer extends MediaPlayer {

    private static AzanPlayer instance = null;
    private MediaPlayer mediaPlayer;
    private static String TAG = "FUCK MEDIA";

    protected AzanPlayer() {
    }

    public static AzanPlayer getInstance() {
        if (instance == null) {
            instance = new AzanPlayer();
        }

        return instance;
    }

    public void initiateMedia(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.azan_moazen_zadeh);
        Log.v(TAG, "created");
    }

    @Override
    public void start() throws IllegalStateException {
        mediaPlayer.start();
        Log.v(TAG, "started");
        //super.start();
    }

    @Override
    public void release() {
        mediaPlayer.release();
        //super.release();
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
        //super.prepare();
        mediaPlayer.prepare();
        Log.v(TAG, "prepared");
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


}
