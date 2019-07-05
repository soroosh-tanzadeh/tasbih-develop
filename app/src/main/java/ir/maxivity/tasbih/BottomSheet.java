package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class BottomSheet extends BottomSheetDialogFragment {

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet, null);
        dialog.setContentView(view);
        Button showqiblah_btn =  view.findViewById(R.id.showqiblah_btn);
        Button showquran_btn =  view.findViewById(R.id.showquran_btn);
        Button showzekrcounter_btn =  view.findViewById(R.id.showzekrcounter_btn);
        Button showZiaratBtn = view.findViewById(R.id.showziartonline_btn);
        Button showMsgBtn = view.findViewById(R.id.showmsg_btn);

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

    private void navigate(Class<?> classOf) {
        Intent i = new Intent(getActivity(), classOf);
        startActivity(i);
    }
}