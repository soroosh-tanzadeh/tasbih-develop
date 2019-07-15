package ir.maxivity.tasbih.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import ir.maxivity.tasbih.R;

public class AddReminderDialogFragment extends DialogFragment {

    public String time;
    private TextView textView;
    private Button submit;
    private EditText reminderText;
    private OnSubmitClick listener;
    private TimePicker timePicker;
    private EditText timePickerEdtTxt;


    public static AddReminderDialogFragment newInstance(String time) {
        AddReminderDialogFragment dialogFragment = new AddReminderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TIME", time);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            time = getArguments().getString("TIME");
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_reminder_layout, container, false);
        textView = root.findViewById(R.id.reminder_title_txt);
        textView.setText(time);
        submit = root.findViewById(R.id.submit_reminder);
        reminderText = root.findViewById(R.id.reminder_text);
        timePicker = root.findViewById(R.id.time_picker_spinner);
        timePickerEdtTxt = root.findViewById(R.id.time_picker_edt);
        timePicker.setIs24HourView(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePickerEdtTxt.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
        } else {
            timePickerEdtTxt.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminderText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.reminder_text_validation), Toast.LENGTH_SHORT).show();
                } else {
                    String time = "";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        time = timePicker.getHour() + ":" + timePicker.getMinute();
                    } else {
                        time = timePickerEdtTxt.getText().toString();
                    }
                    listener.onAddReminderClick(reminderText.getText().toString(), time);
                }
            }
        });

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialogFragment = super.onCreateDialog(savedInstanceState);
        dialogFragment.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialogFragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnSubmitClick) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnSubmitClick {
        void onAddReminderClick(String message, String time);
    }
}
