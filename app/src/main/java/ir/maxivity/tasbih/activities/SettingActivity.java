package ir.maxivity.tasbih.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.fragments.AzanDialogFragment;

public class SettingActivity extends AppCompatActivity {

    RelativeLayout azan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        azan = findViewById(R.id.azan_settings);

        azan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new AzanDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "AZAN");
            }
        });
    }


}
