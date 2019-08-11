package ir.maxivity.tasbih.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.card.MaterialCardView;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.SahnTypes;

public class ImamRezaDialogFragment extends DialogFragment {

    MaterialCardView jame, jomhuri, azadi, enghelab;
    private OnCardClick listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.imam_reza_dialog, container, false);
        jame = root.findViewById(R.id.sahne_jame);
        azadi = root.findViewById(R.id.sahne_azadi);
        jomhuri = root.findViewById(R.id.sahne_jomhuri);
        enghelab = root.findViewById(R.id.sahne_enghelab);

        jame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSahnClick(SahnTypes.JAME);
                dismiss();
            }
        });

        azadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSahnClick(SahnTypes.AZADI);
                dismiss();
            }
        });

        jomhuri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSahnClick(SahnTypes.JOMHORI);
                dismiss();
            }
        });

        enghelab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSahnClick(SahnTypes.ENGHELAB);
                dismiss();
            }
        });

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialogFragment = super.onCreateDialog(savedInstanceState);
        dialogFragment.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof OnCardClick)
                listener = (OnCardClick) getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCardClick {
        void onSahnClick(SahnTypes sahn);
    }
}
