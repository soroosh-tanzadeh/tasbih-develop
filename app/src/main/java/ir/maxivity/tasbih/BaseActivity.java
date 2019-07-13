package ir.maxivity.tasbih;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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

    public NasimDialog showServerErrorDialog() {
        NasimDialog dialog = new NasimDialog(this, NasimDialogTypes.WARNING_TYPE);
        dialog.setDialogTitle("ارتباط با سرور برقرار نیست!");
        dialog.setCancelable(false);
        return dialog;
    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public NasimDialog guestUserSignUpDialog(final Activity activity) {
        final NasimDialog nasimDialog = new NasimDialog(this, NasimDialogTypes.QUESTION_TYPE);
        nasimDialog.setDialogTitle("کاربر مهمان");
        nasimDialog.setDialogcontent("برای دیدن این قسمت باید ثبت نام کنید.");
        nasimDialog.setPositiveButton(getString(R.string.ok), new NasimDialog.OnPositiveButtonClick() {
            @Override
            public void onClick(View v) {
                nasimDialog.dismiss();
                finish();
                Intent intent = new Intent(activity, Login.class);
                startActivity(intent);
            }
        });

        nasimDialog.setNegativeButton(getString(R.string.no), new NasimDialog.OnNegativeButtonClick() {
            @Override
            public void onClick(View v) {
                nasimDialog.dismiss();
            }
        });

        return nasimDialog;
    }
}
