package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    private clickListener listener;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Set the custom view

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_reworked, null);
        dialog.setContentView(view);
        Button showqiblah_btn =  view.findViewById(R.id.showqiblah_btn);
        Button showquran_btn =  view.findViewById(R.id.showquran_btn);
        Button showzekrcounter_btn =  view.findViewById(R.id.showzekrcounter_btn);
        Button showZiaratBtn = view.findViewById(R.id.showziartonline_btn);
        Button showMsgBtn = view.findViewById(R.id.showmsg_btn);
        Button showMap = view.findViewById(R.id.showmap_btn);

        showqiblah_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(Qiblah_compass.class);
            }
        });

        showquran_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(QuranAdyeh.class);
            }
        });

        showzekrcounter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(ZekrsList.class);
            }
        });

        showZiaratBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(ZiaratOnlineActivity.class);
            }
        });

        showMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(MessagesActivity.class);
            }
        });


        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMapClick();
                dismiss();
            }
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (clickListener) context;
        } catch (Exception e) {

        }
    }

    private void navigate(Class<?> classOf) {
        Intent i = new Intent(getActivity(), classOf);
        startActivity(i);
    }

    public interface clickListener {
        void onMapClick();
    }
}