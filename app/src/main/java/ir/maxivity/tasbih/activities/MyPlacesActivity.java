package ir.maxivity.tasbih.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.NasimDialog;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.FavoritePlaceAdapter;
import ir.maxivity.tasbih.models.GetPlaceBody;
import ir.maxivity.tasbih.models.GetPlaces;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

public class MyPlacesActivity extends BaseActivity {

    private RecyclerView myPlaceRecycler;
    private FavoritePlaceAdapter adapter;
    private ArrayList<GetPlaces.response> places = new ArrayList<>();
    private TextView empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);

        settings_btn.setVisibility(View.GONE);
        TextView englishDate = actionbarview.findViewById(R.id.english_date_text);
        persianDate.setText(Utilities.getTodayJalaliDate(this));
        arabicDate.setText(Utilities.getTodayIslamicDate(this));
        englishDate.setText(Utilities.getTodayGregortianDate(this));


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });

        myPlaceRecycler = findViewById(R.id.my_place_recycler);
        empty = findViewById(R.id.empty_list);
        adapter = new FavoritePlaceAdapter(this, places);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlacesById();
    }

    private void getPlacesById() throws NullPointerException {
        final NasimDialog dialog = showLoadingDialog();
        dialog.show();
        GetPlaceBody body = new GetPlaceBody();
        body.user_id = application.getUserId();
        application.api.getPlace(RequestBody.create(Utilities.JSON, Utilities.createBody(body))).enqueue(new Callback<GetPlaces>() {
            @Override
            public void onResponse(Call<GetPlaces> call, Response<GetPlaces> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().result == 1) {
                        places.addAll(response.body().data);
                    }
                    adapter.updateData(places);
                }
            }

            @Override
            public void onFailure(Call<GetPlaces> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


}
