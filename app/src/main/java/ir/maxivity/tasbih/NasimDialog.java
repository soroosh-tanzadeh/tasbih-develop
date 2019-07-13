package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

public class NasimDialog extends Dialog {

    private NasimDialogTypes dialogType;
    private TextView title;
    private TextView content;
    private Button positiveButton, negativeButton;
    private OnPositiveButtonClick positiveListener;
    private OnNegativeButtonClick negativeListener;
    private ImageView dialogImage;
    private Context context;
    private ImageView divider, divider1;
    //private ProgressBar progressBar;
    private AVLoadingIndicatorView avi;

    public NasimDialog(@NonNull Context context, NasimDialogTypes dialogType) {
        super(context, R.style.nasimDialogTheme);
        this.dialogType = dialogType;
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.nasim_dialog);
        initViews();
        initType();
    }

    private void initWindow() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int wPixel = (int) (300 * Resources.getSystem().getDisplayMetrics().density);
        int hPixel = (int) (600 * Resources.getSystem().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = wPixel;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void initViews() {
        title = findViewById(R.id.dialog_title);
        content = findViewById(R.id.dialog_content);
        positiveButton = findViewById(R.id.dialog_positive_button);
        negativeButton = findViewById(R.id.dialog_negative_button);
        dialogImage = findViewById(R.id.dialog_image);
        avi = findViewById(R.id.progress_dialog);
    }

    public void setDialogTitle(String text) {
        title.setText(text);
    }

    public void setDialogcontent(String text) {
        content.setText(text);
    }

    public void initType() {
        switch (dialogType) {
            case SUCCESS_TYPE:
                //dialogImage.setImageResource(R.drawable.success);
                dialogImage.setVisibility(View.GONE);
                break;
            case ERROR_TYPE:
                //dialogImage.setImageResource(R.drawable.close1);
                dialogImage.setVisibility(View.GONE);
                break;
            case QUESTION_TYPE:
                //dialogImage.setImageResource(R.drawable.question);
                dialogImage.setVisibility(View.GONE);

                break;
            case WARNING_TYPE:
                //dialogImage.setImageResource(R.drawable.warning);
                dialogImage.setVisibility(View.GONE);
                break;
            case PROGRESS_TYPE:
                dialogImage.setVisibility(View.GONE);
                avi.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void setPositiveButton(String text, OnPositiveButtonClick listener) {
        positiveButton.setVisibility(View.VISIBLE);
        positiveButton.setText(text);
        positiveListener = listener;
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveListener.onClick(v);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    public void setPositiveButtonColor(int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && positiveButton instanceof AppCompatButton) {
            ((AppCompatButton) positiveButton).setSupportBackgroundTintList(ContextCompat.getColorStateList(context, color));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                positiveButton.setBackgroundTintList(ContextCompat.getColorStateList(context, color));
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public void setNegativeButtonColor(int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && negativeButton instanceof AppCompatButton) {
            ((AppCompatButton) negativeButton).setSupportBackgroundTintList(ContextCompat.getColorStateList(context, color));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                negativeButton.setBackgroundTintList(ContextCompat.getColorStateList(context, color));
            }
        }
    }

    public void setNegativeButton(String text, OnNegativeButtonClick listener) {
        negativeButton.setVisibility(View.VISIBLE);
        negativeButton.setText(text);
        negativeListener = listener;
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativeListener.onClick(v);
            }
        });

    }

    public void setCustomDrawable(int drawable) {
        dialogImage.setImageResource(drawable);
    }

    public void setMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int wPixel = (int) (250 * Resources.getSystem().getDisplayMetrics().density);
        int hPixel = (int) (600 * Resources.getSystem().getDisplayMetrics().density);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = wPixel;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void show() {
        super.show();
        setMetrics();
    }

    public interface OnPositiveButtonClick {
        void onClick(View v);

    }

    public interface OnNegativeButtonClick {
        void onClick(View v);

    }

}
