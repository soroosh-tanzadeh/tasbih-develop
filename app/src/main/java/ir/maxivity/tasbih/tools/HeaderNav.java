package ir.maxivity.tasbih.tools;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import ir.maxivity.tasbih.BuildConfig;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.Sidebar;

public class HeaderNav {

    public void loadHeadernav(final AppCompatActivity cc){
        cc.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        cc.getSupportActionBar().setDisplayShowCustomEnabled(true);
        cc.getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = cc.getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "تسبیح");
                    String shareMessage = "\nLاپلیکیشن تسبیح را دانلود کنید\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    cc.startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cc, Sidebar.class);
                cc.startActivity(i);
            }
        });
    }
}
