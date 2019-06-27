package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.maxivity.tasbih.R;

public class QuranAdapter extends RecyclerView.Adapter<QuranAdapter.QuranViewHolder> {

    private Context context;
    private JSONArray quran;
    private OnSurahClick listener;

    public QuranAdapter(Context context, JSONArray quran) {
        this.context = context;
        this.quran = quran;
    }

    @NonNull
    @Override
    public QuranViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QuranViewHolder(LayoutInflater.from(context).inflate(R.layout.quran_recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuranViewHolder quranViewHolder, int i) {
        try {
            quranViewHolder.setData(quran.getJSONObject(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return quran.length();
    }

    public void setOnSurahClickListener(OnSurahClick click) {
        this.listener = click;
    }

    public class QuranViewHolder extends RecyclerView.ViewHolder {

        TextView quranName;

        public QuranViewHolder(@NonNull View itemView) {
            super(itemView);
            quranName = itemView.findViewById(R.id.quran_name);
        }

        public void setData(final JSONObject object) {
            try {
                String name = object.getString("arabic");
                quranName.setText(name);
                quranName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            listener.onClick(Integer.parseInt(object.getString("id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public interface OnSurahClick {
        void onClick(int id);
    }
}
