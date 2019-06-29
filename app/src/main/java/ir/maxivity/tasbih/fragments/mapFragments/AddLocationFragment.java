package ir.maxivity.tasbih.fragments.mapFragments;


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
public class AddLocationFragment extends Fragment {


    private View root;
    private Button submit , cancel;
    private MapListener listener;

    public AddLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_location, container, false);
        initViews();
        return root;
    }

    private void initViews(){
        submit = root.findViewById(R.id.submit_location_btn);
        cancel = root.findViewById(R.id.cancel_location_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddLocationSubmit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddLocationCancel();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof MapListener)
                listener = (MapListener) getParentFragment();
        }
        catch (Exception  e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
