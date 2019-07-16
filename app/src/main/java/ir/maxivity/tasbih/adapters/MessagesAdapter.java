package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.GetMessages;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private Context context;
    private List<GetMessages.Message> messages;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public MessagesAdapter(Context context, List<GetMessages.Message> messages) {
        this.context = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessagesViewHolder(inflater.inflate(R.layout.message_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder messagesViewHolder, int i) {
        messagesViewHolder.setData(messages.get(i));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateList(List<GetMessages.Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        RelativeLayout wrapper;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.message_text);
            image = itemView.findViewById(R.id.message_icon);
            wrapper = itemView.findViewById(R.id.message_wrapper);
        }

        public void setData(final GetMessages.Message message) {
            name.setText(message.name);
            Picasso.get().load(message.url).into(image);

            wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(message);
                    }
                }
            });
        }

    }

    public void setCLickListener(OnItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(GetMessages.Message message);
    }
}
