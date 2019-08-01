package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.maxivity.tasbih.R;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundsViewHolder> {

    Context context;
    ArrayList<String> images;
    LayoutInflater inflater;
    private OnItemClickListener listener;

    public BackgroundAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BackgroundsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BackgroundsViewHolder(inflater.inflate(R.layout.background_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BackgroundsViewHolder holder, int position) {
        holder.setData(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class BackgroundsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public BackgroundsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.background_image);

        }

        public void setData(final String url) {
            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(url);
                    }
                }
            });
        }
    }

    public void setCLickListener(OnItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String url);
    }
}
