package ir.maxivity.tasbih.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.card.MaterialCardView;

import ir.maxivity.tasbih.R;

public class SelectLanguageDialogFragment extends DialogFragment {

    private final int SELECTED_CARD_WIDTH = 3;
    private final int UNSELECTED_CARD_WIDTH = 0;
    boolean persianSelected = false;
    boolean arabicSelected = false;
    MaterialCardView persianCardView, arabicCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.select_language_layout, container, false);
        Button submit = root.findViewById(R.id.submit);
        persianCardView = root.findViewById(R.id.persian_card);
        arabicCardView = root.findViewById(R.id.arabic_card);


        persianCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arabicSelected) {
                    arabicCardView.setStrokeWidth(UNSELECTED_CARD_WIDTH);
                    persianCardView.setStrokeWidth(SELECTED_CARD_WIDTH);
                    persianSelected = true;
                    arabicSelected = false;
                } else if (!persianSelected) {
                    persianCardView.setStrokeWidth(SELECTED_CARD_WIDTH);
                    persianSelected = true;
                }
            }
        });

        arabicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (persianSelected) {
                    persianCardView.setStrokeWidth(UNSELECTED_CARD_WIDTH);
                    arabicCardView.setStrokeWidth(SELECTED_CARD_WIDTH);
                    arabicSelected = true;
                    persianSelected = false;
                } else if (!arabicSelected) {
                    arabicCardView.setStrokeWidth(SELECTED_CARD_WIDTH);
                    arabicSelected = true;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return root;

    }
}
