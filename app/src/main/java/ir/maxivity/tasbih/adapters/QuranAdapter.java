package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.models.QuranTextModel;

public class QuranAdapter extends RecyclerView.Adapter<QuranAdapter.QuranViewHolder> {

    private List<QuranTextModel> texts;
    private Context context;
    private LayoutInflater inflater;

    public QuranAdapter(List<QuranTextModel> texts, Context context) {
        this.context = context;
        this.texts = texts;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public QuranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuranViewHolder(inflater.inflate(R.layout.quran_text_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuranViewHolder holder, int position) {
        holder.setData(texts.get(position));
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    class QuranViewHolder extends RecyclerView.ViewHolder {
        TextView arabic, persian, english;

        public QuranViewHolder(@NonNull View itemView) {
            super(itemView);
            arabic = itemView.findViewById(R.id.quran_arabic);
            persian = itemView.findViewById(R.id.quran_persian);
            english = itemView.findViewById(R.id.quran_english);
        }

        public void setData(QuranTextModel model) {
            Log.d("FUCK QURAN ADAPter", model.getArabic() + "");
            arabic.setText(model.getArabic());
            english.setText(model.getEnglish());
            persian.setText(model.getPersian());
        }
    }

}
