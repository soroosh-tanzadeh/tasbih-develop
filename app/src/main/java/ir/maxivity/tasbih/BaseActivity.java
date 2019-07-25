package ir.maxivity.tasbih;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

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


    public long daysBetween(Long d1, Long d2) {
        long diffInMillis = Math.abs(d2 - d1);
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public void shareIntent() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "تسبیح");
            String shareMessage = "\nLاپلیکیشن تسبیح را دانلود کنید\n\n";
            shareMessage = shareMessage + "https://tasbihapp.ir" + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
}
