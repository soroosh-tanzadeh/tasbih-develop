package ir.maxivity.tasbih;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected NasimApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (NasimApplication) getApplication();
    }


    public NasimDialog showLoadingDialog() {
        NasimDialog dialog = new NasimDialog(this, NasimDialogTypes.PROGRESS_TYPE);
        dialog.setDialogTitle(getString(R.string.waiting));
        dialog.setCancelable(false);
        return dialog;
    }
}
