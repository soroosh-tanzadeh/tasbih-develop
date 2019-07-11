package ir.maxivity.tasbih.fragments.mapFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.interfaces.MapListener;
import ir.maxivity.tasbih.models.GetPlaces;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationInfoFragment extends Fragment {


    private static final String MODEL_KEY = "MODEL_KEY";
    private GetPlaces.response model;
    private RelativeLayout bottomSheet;
    private ImageView arrow, locationImage;
    private TextView name, description, persianName;
    private EditText locationName, locationAddress, locationWebsite, locationPhone;
    private BottomSheetBehavior behavior;
    private Button editBtn, addEventBtn;
    private LinearLayout webSiteWrapper, addFavoritePlaceWrapper;
    private MapListener listener;

    public LocationInfoFragment() {
        // Required empty public constructor
    }

    public static LocationInfoFragment newInstance(GetPlaces.response model) {
        LocationInfoFragment infoFragment = new LocationInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MODEL_KEY, model);
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.model = (GetPlaces.response) getArguments().getSerializable(MODEL_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_info, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        bottomSheet = view.findViewById(R.id.bottom_sheet_info);
        behavior = BottomSheetBehavior.from(bottomSheet);
        locationImage = view.findViewById(R.id.location_image);
        name = view.findViewById(R.id.location_name);
        description = view.findViewById(R.id.location_description);
        persianName = view.findViewById(R.id.location_persian_name);
        locationName = view.findViewById(R.id.location_name_text);
        locationAddress = view.findViewById(R.id.address_name_text);
        locationWebsite = view.findViewById(R.id.website_name_text);
        locationPhone = view.findViewById(R.id.phone_number_text);
        arrow = view.findViewById(R.id.view_arrow);
        addFavoritePlaceWrapper = view.findViewById(R.id.favorite_place_wrapper);
        editBtn = view.findViewById(R.id.edit_btn);
        addEventBtn = view.findViewById(R.id.add_event_btn);
        webSiteWrapper = view.findViewById(R.id.website_wrapper);

        locationPhone.setEnabled(false);
        locationWebsite.setEnabled(false);
        locationAddress.setEnabled(false);
        locationName.setEnabled(false);


        viewActions();


    }

    private void viewActions() {
        if (model != null) {
            try {
                Picasso.get().load(model.img_address).into(locationImage);
            } catch (IllegalArgumentException e) {
                Toast.makeText(getContext(), "فایل عکس خراب است", Toast.LENGTH_SHORT).show();
            }

            name.setText(model.place_name);
            description.setText(model.description);
            persianName.setText(model.img_documents);
            locationName.setText(model.place_name);
            locationPhone.setText(model.phone);
            locationAddress.setText(model.description);
            locationWebsite.setText(model.web_address);
        }

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                if (state == behavior.STATE_COLLAPSED) {
                    arrow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_up));
                }

                if (state == behavior.STATE_EXPANDED) {
                    arrow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.arrow_down));
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        addFavoritePlaceWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFavoritePlaceClick(model.id);
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddEventClick(model.id);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPhase();
            }
        });
    }

    private void editPhase() {
        locationName.setEnabled(true);
        locationAddress.setEnabled(true);
        locationWebsite.setEnabled(true);
        locationPhone.setEnabled(true);

        editBtn.setText("ذخیره");
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

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
