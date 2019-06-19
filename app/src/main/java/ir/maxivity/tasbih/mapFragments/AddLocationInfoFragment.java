package ir.maxivity.tasbih.mapFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.interfaces.MapListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationInfoFragment extends Fragment {

    private MapListener listener;

    private View root;
    private Button submit, cancel;
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
