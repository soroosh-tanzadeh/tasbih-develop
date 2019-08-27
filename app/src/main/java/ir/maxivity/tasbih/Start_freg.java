package ir.maxivity.tasbih;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.maxivity.tasbih.adapters.EventAdapter;
import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;
import ir.maxivity.tasbih.models.GetEventResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;


public class Start_freg extends BaseFragment {


    private View the_view;
    private DateFormat df;
    private ImageButton volume;
    private RecyclerView eventsRecycler;
    private EventAdapter adapter;

    TextView time;

    public Start_freg() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        the_view = inflater.inflate(R.layout.fragment_start_freg, container, false);

        return the_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageButton calendar_btn = the_view.findViewById(R.id.calendar_btn);
        time = the_view.findViewById(R.id.textView5);
        volume = the_view.findViewById(R.id.volumeButton);
        eventsRecycler = the_view.findViewById(R.id.recycler_event);
        eventsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start_freg.this.getActivity(), Calendar.class);
                startActivity(i);
            }
        });

        final AudioManager amanager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        Date currentTime = java.util.Calendar.getInstance().getTime();

        df = new SimpleDateFormat("HH:mm");
        String date = df.format(java.util.Calendar.getInstance().getTime());

        time.setText(date);

        updateTime();
        final MainActivity mainActivity = (MainActivity) getActivity();
        try {
            Log.v("FUCK QUE", mainActivity.application.getSoundMode());
            if (mainActivity.application.getSoundMode().equals("OFF")) {
                volume.setImageResource(R.drawable.ic_volume_off_black_24dp);
            } else {
                volume.setImageResource(R.drawable.ic_volume_up_black_24dp);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            volume.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }

        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volume.getContentDescription() == "OFF") {
                    try {
                        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                        amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
                        volume.setContentDescription("ON");
                        mainActivity.application.setApplicationSound(volume.getContentDescription().toString());
                        volume.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), getString(R.string.do_not_disturb_warning), Toast.LENGTH_LONG).show();

                    }
                } else {
                    try {
                        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
                        volume.setContentDescription("OFF");
                        mainActivity.application.setApplicationSound(volume.getContentDescription().toString());
                        volume.setImageResource(R.drawable.ic_volume_off_black_24dp);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), getString(R.string.do_not_disturb_warning), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        getEvents();

    }

    Runnable updater = null;

    void updateTime() {
        final Handler timerHandler = new Handler();

        updater = new Runnable() {
            @Override
            public void run() {

                String date = df.format(java.util.Calendar.getInstance().getTime());
                time.setText(date);
                timerHandler.postDelayed(updater, 1000);
            }
        };
        timerHandler.post(updater);
    }

    public void getEvents() {
        final MainActivity main = (MainActivity) getActivity();
        RequestBody place_id = RequestBody.create(Utilities.TEXT, "-1");
        RequestBody offset = RequestBody.create(Utilities.TEXT, "0");
        RequestBody limit = RequestBody.create(Utilities.TEXT, "6");

        main.application.api.getEvents(offset, limit, place_id).enqueue(new Callback<GetEventResponse>() {
            @Override
            public void onResponse(Call<GetEventResponse> call, Response<GetEventResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().result == 1) {
                        adapter = new EventAdapter(getContext(), response.body().data);
                        eventsRecycler.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetEventResponse> call, Throwable t) {

            }
        });
    }
}
