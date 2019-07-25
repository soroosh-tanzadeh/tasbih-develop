package ir.maxivity.tasbih.tools;

import android.content.Context;
import android.media.MediaPlayer;

import ir.maxivity.tasbih.R;

public class AzanPlayer {

    private static AzanPlayer instance = null;
    private MediaPlayer mediaPlayer;
    private static String TAG = "FUCK MEDIA";
    private Context context;


    private AzanPlayer(Context context) {
        this.context = context;
        initiateMedia();
    }

    public static AzanPlayer getInstance(Context context) {
        if (instance == null) {
            instance = new AzanPlayer(context);
        }

        return instance;
    }

    public void initiateMedia() {
        mediaPlayer = MediaPlayer.create(context, R.raw.azan_moazen_zadeh);

    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void play() {
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }



}
