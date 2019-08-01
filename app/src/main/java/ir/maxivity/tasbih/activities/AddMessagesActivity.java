package ir.maxivity.tasbih.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.R;
import tools.Utilities;

public class AddMessagesActivity extends BaseActivity {

    ImageView background, message, whiteColor, blackColor;
    TextView messageName;
    SeekBar font;
    FrameLayout imageWrapper;
    Bitmap imageBitmap;
    Button send;

    String backgorundUrl, messageUrl, messageNameText;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_messages_drawer_layout);

        background = findViewById(R.id.background_image);
        message = findViewById(R.id.message_image);
        messageName = findViewById(R.id.message_name);
        font = findViewById(R.id.font_seek_bar);
        imageWrapper = findViewById(R.id.image_wrapper);
        whiteColor = findViewById(R.id.white_color);
        blackColor = findViewById(R.id.black_color);
        send = findViewById(R.id.send_btn);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);

        TextView englishDate = actionbarview.findViewById(R.id.english_date_text);
        persianDate.setText(Utilities.getTodayJalaliDate(this));
        arabicDate.setText(Utilities.getTodayIslamicDate(this));
        englishDate.setText(Utilities.getTodayGregortianDate(this));


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = viewToBitmap(imageWrapper);
                saveImage(bitmap);
                shareImage();
            }
        });


        drawerActions();
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        if (getIntent() != null) {
            backgorundUrl = getIntent().getStringExtra("BACKGROUND_URL");
            messageUrl = getIntent().getStringExtra("MESSAGE_URL");
            messageNameText = getIntent().getStringExtra("MESSAGE_NAME");

        }

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                message.setImageBitmap(bitmap);
                imageBitmap = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.get().load(backgorundUrl).fit().into(background);
        Picasso.get().load(messageUrl).fit().into(message);
        messageName.setText(messageNameText);

        whiteColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                message.setColorFilter(R.color.gray_100, PorterDuff.Mode.SRC_ATOP);
            }
        });

        blackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                message.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
            }
        });

        float fs = 20f;
        font.setProgress((int) fs);
        messageName.setTextSize(TypedValue.COMPLEX_UNIT_PX, font.getProgress());

        font.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                messageName.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void saveImage(Bitmap bitmap) {
        try {

            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void shareImage() {
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, "ir.maxivity.tasbih.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            String shareMessage = "\nاپلیکیشن تسبیح را دانلود کنید\n\n";
            shareMessage = shareMessage + "https://tasbihapp.ir" + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    public void changeBitmapColor(Bitmap bm, int color) {

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(bm);
        canvas.drawBitmap(bm, 0, 0, paint);
    }




}
