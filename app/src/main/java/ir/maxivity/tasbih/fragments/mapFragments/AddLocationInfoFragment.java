package ir.maxivity.tasbih.fragments.mapFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.CategorySpinnerAdapter;
import ir.maxivity.tasbih.interfaces.MapListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationInfoFragment extends Fragment {

    private static final int PICK_IMAGE = 200;
    private static final String IMAGE_FILE_DESTINATION = "nasim_cropped_image";
    private static final String TAG = "FUCK INFO";


    private MapListener listener;

    private View root;
    private Button submit, cancel;
    private Spinner category;
    private EditText homeName, websiteAddress, phoneNumber;
    private HashMap<String, String> infoFieldMap = new HashMap<>();
    private ImageView addImage;
    private String imageBase64Text;
    public AddLocationInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_location_info, container, false);
        bindViews();
        return root;
    }

    private void bindViews() {
        submit = root.findViewById(R.id.save_btn);
        cancel = root.findViewById(R.id.cancel_btn);
        category = root.findViewById(R.id.category_spinner);
        homeName = root.findViewById(R.id.location_name_edt);
        websiteAddress = root.findViewById(R.id.website_edt);
        phoneNumber = root.findViewById(R.id.phone_edt_txt);
        addImage = root.findViewById(R.id.add_image);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getContext(), R.layout.category_spinner_layout, categoryList());

        category.setAdapter(adapter);

        category.setSelection(0);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillInputData();
                listener.onAddLocationInfoSubmit(infoFieldMap);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddLocationInfoCancel();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });

    }

    private void fillInputData() {
        infoFieldMap.put("img_address", imageBase64Text);
        infoFieldMap.put("img_documents", category.getSelectedItem().toString());
        infoFieldMap.put("place_name", homeName.getText().toString());
        infoFieldMap.put("description", category.getSelectedItem().toString());
        infoFieldMap.put("phone", phoneNumber.getText().toString());
        infoFieldMap.put("web_address", websiteAddress.getText().toString());
    }

    private List<String> categoryList() {
        List<String> list = new ArrayList<>();
        list = Arrays.asList(getResources().getStringArray(R.array.category_list));
        return list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof MapListener)
                listener = (MapListener) getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void handleCropResult(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
            Picasso.get().load(uri).into(addImage);
            Log.v(TAG, "image" + uri);
            imageBase64Text = imageToBase64(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private String imageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String baseFormat = "data:image/png;base64,";
        return baseFormat + Base64.encodeToString(byteArray, Base64.DEFAULT);
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
}
