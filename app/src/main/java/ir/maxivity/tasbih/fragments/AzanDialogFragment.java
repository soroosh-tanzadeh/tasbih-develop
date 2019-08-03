package ir.maxivity.tasbih.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ir.maxivity.tasbih.R;

public class AzanDialogFragment extends DialogFragment {

    CheckBox shiaa, sonni, sobh, zohr, asr, maqrib, ishaa, eqame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.azan_dialog_fragment, container, false);
        initViews(root);
        Button submit = root.findViewById(R.id.submit_azan);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return root;
    }

    private void initViews(View root) {
        shiaa = root.findViewById(R.id.shiaa);
        sonni = root.findViewById(R.id.soni);
        sobh = root.findViewById(R.id.sobh_azan_check);
        zohr = root.findViewById(R.id.zohr_azan_check);
        asr = root.findViewById(R.id.asr_azan_checked);
        maqrib = root.findViewById(R.id.maqrib_azan_checked);
        ishaa = root.findViewById(R.id.ishaa_azan_checked);
        eqame = root.findViewById(R.id.eqame_check);

        shiaa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (sonni.isChecked()) {
                        sonni.setChecked(false);
                    }
                }
            }
        });

        sonni.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (shiaa.isChecked()) {
                        shiaa.setChecked(false);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialogFragment = super.onCreateDialog(savedInstanceState);
        dialogFragment.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialogFragment;
    }
}
