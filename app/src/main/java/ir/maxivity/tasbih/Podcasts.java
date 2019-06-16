package ir.maxivity.tasbih;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Podcasts extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout l = new LinearLayout(getContext());
        TextView t = new TextView(getContext());
        t.setText("این بخش به زودی اضافه می‌شود");
        t.setTextSize(30);
        l.addView(t);
        return l;
    }
}
