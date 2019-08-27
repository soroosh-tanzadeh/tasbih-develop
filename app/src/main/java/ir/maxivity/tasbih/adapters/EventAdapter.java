package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.GetEventResponse;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    Context context;
    List<GetEventResponse.EventResponse> events;
    LayoutInflater inflater;

    public EventAdapter(Context context, List<GetEventResponse.EventResponse> events) {
        this.context = context;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(inflater.inflate(R.layout.cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.setData(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView6);
            textView = itemView.findViewById(R.id.textView6);
        }

        public void setData(GetEventResponse.EventResponse item) {
            Picasso.get().load(item.thumbnail).fit().placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(item.offer_description);
        }
    }
}
