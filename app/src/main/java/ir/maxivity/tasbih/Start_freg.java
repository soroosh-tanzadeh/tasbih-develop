package ir.maxivity.tasbih;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;


public class Start_freg extends BaseFragment {


    private View the_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        the_view = inflater.inflate(R.layout.fragment_start_freg, container, false);
        return the_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageButton calendar_btn = the_view.findViewById(R.id.calendar_btn);
        TextView time = the_view.findViewById(R.id.textView5);
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start_freg.this.getActivity(), Calendar.class);
                startActivity(i);
            }
        });

        Date currentTime = java.util.Calendar.getInstance().getTime();

        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(java.util.Calendar.getInstance().getTime());

        time.setText(date);

    }
}
