package ir.maxivity.tasbih.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.NasimDialog;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.FavoritePlaceAdapter;
import ir.maxivity.tasbih.models.GetFavoritePlaces;
import ir.maxivity.tasbih.models.GetPlaceBody;
import ir.maxivity.tasbih.models.GetPlaces;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

public class FavoritePlacesActivity extends BaseActivity {

    private RecyclerView favoriteList;
    private ArrayList<GetPlaces.response> places = new ArrayList<>();
    private FavoritePlaceAdapter adapter;
    private LinearLayout empty;
    NasimDialog dialog;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

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

        favoriteList = findViewById(R.id.favorite_place_recycler);
        empty = findViewById(R.id.empty_list);
        adapter = new FavoritePlaceAdapter(this, places);
        dialog = showLoadingDialog();

        getSupportActionBar().setTitle("علاقه مندی ها");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getFavoritePlaces();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void getFavoritePlaces() throws NullPointerException {
        dialog.show();
        RequestBody pass = RequestBody.create(MediaType.parse("text/plain"), "R&K7v2t9tQ*Pez@p");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), application.getUserId());
        application.api.getFavoritePlaces(userId, pass).enqueue(new Callback<GetFavoritePlaces>() {
            @Override
            public void onResponse(Call<GetFavoritePlaces> call, Response<GetFavoritePlaces> response) {
                if (response.isSuccessful()) {
                    if (response.body().result == 1) {
                        for (GetFavoritePlaces.FavoriteResponse res : response.body().data) {
                            getPlacesById(res.place_id);
                        }
                        if (response.body().data.size() > 0) {
                            empty.setVisibility(View.GONE);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetFavoritePlaces> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }

    private void getPlacesById(String id) throws NullPointerException {
        GetPlaceBody body = new GetPlaceBody();
        body.id = id;
        application.api.getPlace(RequestBody.create(Utilities.JSON, Utilities.createBody(body))).
                enqueue(new Callback<GetPlaces>() {
                    @Override
                    public void onResponse(Call<GetPlaces> call, Response<GetPlaces> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().result == 1) {
                                places.add(response.body().data.get(0));
                            }
                            adapter.updateData(places);
                            favoriteList.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPlaces> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
    }
}
