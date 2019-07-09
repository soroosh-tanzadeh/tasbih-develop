package ir.maxivity.tasbih.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.NasimDialog;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.FavoritePlaceAdapter;
import ir.maxivity.tasbih.models.GetFavoritePlaces;
import ir.maxivity.tasbih.models.GetPlaceBody;
import ir.maxivity.tasbih.models.GetPlaces;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

public class FavoritePlacesActivity extends BaseActivity {

    private RecyclerView favoriteList;
    private ArrayList<GetPlaces.response> places = new ArrayList<>();
    private FavoritePlaceAdapter adapter;
    private TextView empty;
    NasimDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

        favoriteList = findViewById(R.id.favorite_place_recycler);
        empty = findViewById(R.id.empty_list);
        adapter = new FavoritePlaceAdapter(this, places);
        dialog = showLoadingDialog();
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
        application.api.getFavoritePlaces(application.getUserId(), "R&K7v2t9tQ*Pez@p").enqueue(new Callback<GetFavoritePlaces>() {
            @Override
            public void onResponse(Call<GetFavoritePlaces> call, Response<GetFavoritePlaces> response) {
                if (response.isSuccessful()) {
                    if (response.body().result == 1) {
                        for (GetFavoritePlaces.FavoriteResponse res : response.body().data) {
                            getPlacesById(res.place_id);
                        }
                        adapter.updateData(places);
                        if (places.size() > 0) {
                            empty.setVisibility(View.GONE);
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetFavoritePlaces> call, Throwable t) {
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
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPlaces> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
    }
}
