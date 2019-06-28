package ir.maxivity.tasbih.mapFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.interfaces.MapListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationInfoFragment extends Fragment {

    private MapListener listener;

    private View root;
    private Button submit, cancel;
    private Spinner category;
    private EditText homeName, websiteAddress, phoneNumber;
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                categoryList());

        arrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        category.setAdapter(arrayAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddLocationInfoCancel();
            }
        });

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
}
