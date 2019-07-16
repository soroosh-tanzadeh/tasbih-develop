package ir.maxivity.tasbih;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;

public class Podcasts extends BaseFragment {


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
