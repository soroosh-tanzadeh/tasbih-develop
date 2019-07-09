package ir.maxivity.tasbih.fragments.mapFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import ir.maxivity.tasbih.R;

public class AddEventDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE = 300;
    private static final String IMAGE_FILE_DESTINATION = "nasim_cropped_event_image";
    private static final String TAG = "FUCK INFO";


    private HashMap<String, String> fields = new HashMap<>();
    private Button submit;
    private ImageView eventImage;
    private Switch status;
    private EditText eventText;
    private String imageBase64Text;
    private OnSubmitButtonClick listener;
    private String placeId;

    public final static String MAP_STATUS_KEY = "disable";
    public final static String MAP_IMAGE_KEY = "thumbnail";
    public final static String MAP_TEXT_KEY = "text";


    public static AddEventDialogFragment newInstance(String id) {
        AddEventDialogFragment dialogFragment = new AddEventDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeId = getArguments().getString("ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_event_layout, container, false);
        initViews(root);
        return root;
    }

    private void initViews(View root) {
        status = root.findViewById(R.id.event_status_switch);
        final TextView statusText = root.findViewById(R.id.event_status_text);
        eventImage = root.findViewById(R.id.event_image);
        eventText = root.findViewById(R.id.event_text);
        submit = root.findViewById(R.id.submit_event);


        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fields.put(MAP_STATUS_KEY, "1");
                    statusText.setText(getString(R.string.enable_event));
                } else {
                    fields.put(MAP_STATUS_KEY, "0");
                    statusText.setText(getString(R.string.disable_event));
                }
            }
        });


        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fields.put(MAP_TEXT_KEY, eventText.getText().toString());
                fields.put(MAP_IMAGE_KEY, imageBase64Text);
                listener.onSubmitEventClick(placeId, fields);
            }
        });

    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void startCrop(Uri uri) {
        Date date = new Date();
        String time = date.getTime() + "";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), IMAGE_FILE_DESTINATION + "_" + time + ".png")));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(250, 250);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        uCrop.withOptions(options);
        uCrop.start(getActivity().getApplicationContext(), this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof OnSubmitButtonClick)
                listener = (OnSubmitButtonClick) getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private String imageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String baseFormat = "data:image/png;base64,";
        return baseFormat + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void handleCropResult(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
            Picasso.get().load(uri).into(eventImage);
            Log.v(TAG, "image" + uri);
            imageBase64Text = imageToBase64(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        startCrop(selectedImage);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.cannot_retrieve_image), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case UCrop.REQUEST_CROP:
                    handleCropResult(UCrop.getOutput(data));
                    break;
            }
        }
    }

    public interface OnSubmitButtonClick {
        void onSubmitEventClick(String id, HashMap<String, String> fields);
    }
}
