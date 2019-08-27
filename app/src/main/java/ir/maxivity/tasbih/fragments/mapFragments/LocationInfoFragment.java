package ir.maxivity.tasbih.fragments.mapFragments;


import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.interfaces.MapListener;
import ir.maxivity.tasbih.models.GetEventResponse;
import ir.maxivity.tasbih.models.GetPlaces;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationInfoFragment extends Fragment {


    private static final String MODEL_KEY = "MODEL_KEY";
    private static final String EVENT_KEY = "EVENT_KEY";
    private GetPlaces.response model;
    private GetEventResponse eventResponse;
    private RelativeLayout bottomSheet;
    private ImageView arrow, locationImage;
    private TextView name, description, persianName;
    private EditText locationName, locationAddress, locationWebsite, locationPhone;
    private BottomSheetBehavior behavior;
    private Button editBtn, addEventBtn;
    private LinearLayout webSiteWrapper, addFavoritePlaceWrapper;
    private MapListener listener;
    private RelativeLayout eventWrapper;
    private ImageView eventImage;
    private TextView eventDescription, eventStatus;

    public LocationInfoFragment() {
        // Required empty public constructor
    }

    public static LocationInfoFragment newInstance(GetPlaces.response model, GetEventResponse eventResponse) {
        LocationInfoFragment infoFragment = new LocationInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MODEL_KEY, model);
        bundle.putSerializable(EVENT_KEY, eventResponse);
        infoFragment.setArguments(bundle);
        return infoFragment;
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
        try {
            this.model = (GetPlaces.response) getArguments().getSerializable(MODEL_KEY);
            this.eventResponse = (GetEventResponse) getArguments().getSerializable(EVENT_KEY);
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        eventWrapper = view.findViewById(R.id.event_wrapper);
        eventImage = view.findViewById(R.id.event_image);
        eventDescription = view.findViewById(R.id.event_description);
        eventStatus = view.findViewById(R.id.event_staus);

        locationPhone.setEnabled(false);
        locationWebsite.setEnabled(false);
        locationAddress.setEnabled(false);
        locationName.setEnabled(false);

        viewActions();

        try {
            if (eventResponse.data.size() == 0) {
                eventWrapper.setVisibility(View.GONE);
            } else {
                GetEventResponse.EventResponse event = eventResponse.data.get(0);
                Picasso.get().load(event.thumbnail).fit().placeholder(R.drawable.placeholder).into(eventImage);
                eventDescription.setText(event.offer_description);
                if (Integer.parseInt(event.disable) == 1) {
                    eventStatus.setText("منقضی شده");
                } else {
                    eventStatus.setText("فعال");
                }
            }
        } catch (NullPointerException e) {
            eventWrapper.setVisibility(View.GONE);

        }


    }

    private void viewActions() {
        if (model != null) {
            try {
                Picasso.get().load(model.img_address).fit().into(locationImage);
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
                if (editBtn.getText() == getString(R.string.edit)) {
                    editPhase();
                } else if (editBtn.getText() == getString(R.string.save)) {
                    HashMap<String, String> fields = new HashMap<>();
                    fields.put("locationName", locationName.getText().toString());
                    fields.put("locationAddress", locationAddress.getText().toString());
                    fields.put("locationWebsite", locationWebsite.getText().toString());
                    fields.put("locationPhone", locationPhone.getText().toString());
                    listener.onEditSubmit(fields);
                }
            }
        });
    }

    private void editPhase() {
        locationName.setEnabled(true);
        locationAddress.setEnabled(true);
        locationWebsite.setEnabled(true);
        locationPhone.setEnabled(true);

        editBtn.setText(getString(R.string.save));
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
