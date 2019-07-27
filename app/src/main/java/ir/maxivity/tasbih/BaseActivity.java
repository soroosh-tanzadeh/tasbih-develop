package ir.maxivity.tasbih;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import ir.maxivity.tasbih.activities.FavoritePlacesActivity;
import ir.maxivity.tasbih.activities.MyPlacesActivity;
import ir.maxivity.tasbih.activities.ReminderActivity;
import ir.maxivity.tasbih.adapters.DrawerListAdapter;
import ir.maxivity.tasbih.tools.CreateDrawerItem;

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

    public void drawerActions() {
        ListView drawerItemList = findViewById(R.id.drawer_item_list);

        final CreateDrawerItem items = new CreateDrawerItem(this, getString(R.string.guest_user), application.getLoginLater());

        DrawerListAdapter drawerAdapter = new DrawerListAdapter(this, R.layout.drawer_item_layout, items.getItems());

        drawerItemList.setAdapter(drawerAdapter);

        drawerItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (items.getItems().get(i).getText() == getString(R.string.favorite)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(BaseActivity.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(BaseActivity.this, FavoritePlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.sign_up)) {
                    finish();
                    Intent intent = new Intent(BaseActivity.this, Login.class);
                    startActivity(intent);
                }
                if (items.getItems().get(i).getText() == getString(R.string.my_places)) {
                    if (application.getLoginLater()) {
                        NasimDialog dialog = guestUserSignUpDialog(BaseActivity.this);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(BaseActivity.this, MyPlacesActivity.class);
                        startActivity(intent);
                    }
                }
                if (items.getItems().get(i).getText() == getString(R.string.reminder)) {
                    Intent intent = new Intent(BaseActivity.this, ReminderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
