package ir.maxivity.tasbih.fragments.mapFragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.interfaces.MapListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {


    private MapListener listener;
    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filter, container, false);
        RelativeLayout restaurant = root.findViewById(R.id.restaurant_wrapper);
        RelativeLayout hotel = root.findViewById(R.id.hotel_wrapper);
        RelativeLayout cofeeShop = root.findViewById(R.id.coffee_shop_wrapper);
        RelativeLayout religious = root.findViewById(R.id.mosque_wrapper);
        RelativeLayout hospital = root.findViewById(R.id.hospital_wrapper);
        RelativeLayout university = root.findViewById(R.id.university_wrapper);
        RelativeLayout shop = root.findViewById(R.id.shop_wrapper);
        RelativeLayout gym = root.findViewById(R.id.gym_wrapper);
        RelativeLayout residency = root.findViewById(R.id.residency_wrapper);
        RelativeLayout dentistry = root.findViewById(R.id.dentistry_wrapper);
        RelativeLayout other = root.findViewById(R.id.other_wrapper);

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.restaurant));
            }
        });
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.hotel));
            }
        });
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.hospital));
            }
        });
        cofeeShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.coffee_shop));
            }
        });
        religious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.religious_places));
            }
        });
        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.university));
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.shop));
            }
        });
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.gym));
            }
        });
        residency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.residency));
            }
        });
        dentistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.dentistry));
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFilter(getString(R.string.other));
            }
        });



        return root;
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
}
