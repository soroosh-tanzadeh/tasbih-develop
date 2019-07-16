package ir.maxivity.tasbih.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.Login;
import ir.maxivity.tasbih.R;

public class SelectLanguageActivity extends BaseActivity {


    private final int SELECTED_CARD_WIDTH = 3;
    private final int UNSELECTED_CARD_WIDTH = 0;
    boolean persianSelected = false;
    boolean arabicSelected = false;
    MaterialCardView persianCardView, arabicCardView;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        persianCardView = findViewById(R.id.persian_card);
        arabicCardView = findViewById(R.id.arabic_card);

        submit = findViewById(R.id.submit_language);


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
                if (persianSelected || arabicSelected) {
                    Intent intent = new Intent(SelectLanguageActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    showShortToast(getString(R.string.select_language_warning));
                }
            }
        });


    }
}
