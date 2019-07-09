package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.GetPlaces;

public class FavoritePlaceAdapter extends RecyclerView.Adapter<FavoritePlaceAdapter.FavoriteViewHolder> {

    private Context context;
    private ArrayList<GetPlaces.response> places;
    private LayoutInflater layoutInflater;

    public FavoritePlaceAdapter(Context context, ArrayList<GetPlaces.response> places) {
        this.context = context;
        this.places = places;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateData(ArrayList<GetPlaces.response> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FavoriteViewHolder(layoutInflater.inflate(R.layout.favorite_place_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        favoriteViewHolder.setData(places.get(i));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, navigation, callPlace;
        TextView name, site;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.favorite_place_image);
            name = itemView.findViewById(R.id.favorite_place_name);
            site = itemView.findViewById(R.id.favorite_place_website);
            navigation = itemView.findViewById(R.id.favorite_place_navigation);
            callPlace = itemView.findViewById(R.id.favorite_place_call);
        }

        public void setData(final GetPlaces.response response) {
            Picasso.get().load(response.img_address).into(imageView);
            name.setText(response.place_name);
            site.setText(response.web_address);
            navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String nav = "geo:" + response.lat + "," + response.lon;
                    intent.setData(Uri.parse(nav));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });

            callPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + response.phone));
                    context.startActivity(intent);
                }
            });
        }
    }
}
