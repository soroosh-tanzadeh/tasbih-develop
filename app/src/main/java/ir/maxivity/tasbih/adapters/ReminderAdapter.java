package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.reminderTools.Reminder;
import tools.Utilities;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {


    Context context;
    LayoutInflater inflater;
    List<Reminder> reminders;
    private OnDeleteClick listener;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReminderViewHolder(inflater.inflate(R.layout.reminder_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder reminderViewHolder, int i) {
        reminderViewHolder.setData(reminders.get(i));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void removeItem(Reminder reminder) {
        Log.v("FUCK RECYCLER", reminders.size() + " before");
        int index = reminders.indexOf(reminder);
        reminders.remove(reminder);
        notifyItemRemoved(index);
        Log.v("FUCK RECYCLER", reminders.size() + " after");
        notifyItemRangeChanged(index, reminders.size());
    }

    public void updateList(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    public void setOnDeleteListener(OnDeleteClick deleteListener) {
        this.listener = deleteListener;
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView time, title, firstWord;
        CardView card;
        ImageView delete;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.reminder_time);
            title = itemView.findViewById(R.id.reminder_title);
            firstWord = itemView.findViewById(R.id.title_first_word);
            card = itemView.findViewById(R.id.circle_colored_card);
            delete = itemView.findViewById(R.id.delete_btn);
        }

        public void setData(final Reminder reminder) {
            title.setText(reminder.getTitle());
            String date = Utilities.numberConvert_En2Fa(reminder.getTime() + "  " + reminder.getDate());
            time.setText(date);
            String first = reminder.getTitle().charAt(0) + "";
            firstWord.setText(first);

            int random = new Random().nextInt(4);

            switch (random) {
                case 0:
                    card.setCardBackgroundColor(Color.parseColor("#d32f2f"));
                    break;
                case 1:
                    card.setCardBackgroundColor(Color.parseColor("#c2185b"));
                    break;
                case 2:
                    card.setCardBackgroundColor(Color.parseColor("#7b1fa2"));
                    break;
                case 3:
                    card.setCardBackgroundColor(Color.parseColor("#00796b"));
                    break;

            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onDelete(reminder.getID() + "");
                }
            });
        }
    }

    public interface OnDeleteClick {
        void onDelete(String id);
    }
}
