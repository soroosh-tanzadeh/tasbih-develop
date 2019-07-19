package ir.maxivity.tasbih;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;

import ir.maxivity.tasbih.models.GetQuranText;
import ir.maxivity.tasbih.models.GetQuranVoice;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

import static tools.Utilities.JSON;
import static tools.Utilities.createBody;

public class QuranAdyehTextActivity extends BaseActivity implements View.OnTouchListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = "FUCK QURAN";
    private int id;
    private String name;
    private TextView header, content;
    private Boolean quranType;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageView play;
    private int mediaFileLengthInMilliseconds;
    private Handler handler = new Handler();
    private String mediaUrl;
    private boolean prepare = false;
    private AVLoadingIndicatorView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_adyeh_text);
        header = findViewById(R.id.header_name_txt);
        content = findViewById(R.id.content_txt);
        seekBar = findViewById(R.id.player_seek_bar);
        progress = findViewById(R.id.progress_loading);
        seekBar.setMax(99);
        seekBar.setOnTouchListener(this);
        play = findViewById(R.id.play_btn);
        id = getIntent().getIntExtra("ID", 0);
        name = getIntent().getStringExtra("NAME");
        quranType = getIntent().getBooleanExtra("QURAN", true);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


        if (quranType) {
            getSuraText();
        }
        else
            getAdyehText();

        header.setText(name);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
                play.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                prepare = true;
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prepare) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        play.setImageResource(R.drawable.ic_play2);
                    } else if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        play.setImageResource(R.drawable.ic_pause);
                    }
                    primarySeekBarProgressUpdater();
                } else {
                    showShortToast("فایل هنوز آماده نیست");
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void primarySeekBarProgressUpdater() {
        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    private void setContentText(ArrayList<GetQuranText.QuranResponse> quranTexts) {
        StringBuilder builder = new StringBuilder();

        for (GetQuranText.QuranResponse quran : quranTexts) {
            builder.append(quran.text);
            builder.append(" (");
            builder.append(Utilities.numberConvert_En2Fa(quran.aya));
            builder.append(")");
            builder.append("\n");
        }

        content.setText(builder.toString());
    }


    public void prepareMedia() {
        if (mediaUrl != null) {
            try {
                mediaPlayer.setDataSource(mediaUrl);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                showShortToast("فایل صوتی در حال حاضر در دسترس نیست");
            }
        } else {
            showShortToast("در حال دریافت فایل لطفا منتظر بمانید");
        }


    }

    private void getSuraText() {

        application.api.getQuranText(RequestBody.create(JSON, createBody(id))).enqueue(new Callback<GetQuranText>() {
            @Override
            public void onResponse(Call<GetQuranText> call, Response<GetQuranText> response) {
                try {
                    if (response.isSuccessful()) {
                        setContentText(response.body().data);
                        getQuranVoice();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(QuranAdyehTextActivity.this, "مشکلی بوجود آمده دوباره تلاش کنید", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetQuranText> call, Throwable t) {
                Log.v(TAG, "fail : " + t.getMessage());
            }
        });
    }

    private void getQuranVoice() throws NullPointerException {
        application.api.getQuranVoice(id).enqueue(new Callback<GetQuranVoice>() {
            @Override
            public void onResponse(Call<GetQuranVoice> call, Response<GetQuranVoice> response) {
                if (response.isSuccessful()) {
                    if (response.body().result == 1) {
                        mediaUrl = response.body().data;
                        prepareMedia();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetQuranVoice> call, Throwable t) {
                showShortToast(t.getMessage());
            }
        });
    }

    private void getAdyehText() {
        Log.v(TAG, "adyeh");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.player_seek_bar) {
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) view;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        play.setImageResource(R.drawable.ic_play2);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        Log.v(TAG, "percent : " + percent);
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.release();
    }
}
