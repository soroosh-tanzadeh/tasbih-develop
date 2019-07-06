package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.GetMessages;
import tools.Utilities;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private Context context;
    private List<GetMessages.Message> messages;
    private LayoutInflater inflater;

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

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.message_text);
            image = itemView.findViewById(R.id.message_icon);

        }

        public void setData(GetMessages.Message message) {
            name.setText(message.name);
            Utilities.fetchSvg(context, message.url, image);
        }

    }
}