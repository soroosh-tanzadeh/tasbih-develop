package ir.maxivity.tasbih.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.maxivity.tasbih.R;

public class QuranAdapter extends RecyclerView.Adapter<QuranAdapter.QuranViewHolder>
        implements Filterable {

    private Context context;
    private JSONArray quran;
    private OnSurahClick listener;
    private ArrayList<ListItemObject> items = new ArrayList<>();
    private ArrayList<ListItemObject> itemsFilter = new ArrayList<>();

    public QuranAdapter(Context context, JSONArray quran) {
        this.context = context;
        this.quran = quran;

        for (int i = 0; i < quran.length(); i++) {
            ListItemObject obj = new ListItemObject();
            JSONObject jsonObject = null;
            try {
                jsonObject = quran.getJSONObject(i);
                obj.name = jsonObject.getString("arabic");
                obj.id = Integer.parseInt(jsonObject.getString("id"));
                items.add(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        itemsFilter = items;
    }

    @NonNull
    @Override
    public QuranViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QuranViewHolder(LayoutInflater.from(context).inflate(R.layout.quran_recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuranViewHolder quranViewHolder, int i) {
      /*  try {
            quranViewHolder.setData(quran.getJSONObject(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        quranViewHolder.setListData(itemsFilter.get(i));
    }

    @Override
    public int getItemCount() {
        return itemsFilter.size();
    }

    public void setOnSurahClickListener(OnSurahClick click) {
        this.listener = click;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                if (query.isEmpty()) {
                    itemsFilter = items;
                } else {
                    ArrayList<ListItemObject> filter = new ArrayList<>();
                    for (ListItemObject object : items) {
                        if (object.name.toLowerCase().contains(query.toLowerCase())) {
                            filter.add(object);
                        }
                    }

                    itemsFilter = filter;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFilter = (ArrayList<ListItemObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

        public void setListData(final ListItemObject object) {
            quranName.setText(object.name);
            quranName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(object.id);
                }
            });
        }
    }


    public interface OnSurahClick {
        void onClick(int id);
    }


    class ListItemObject {
        public String name;
        public int id;
    }
}
