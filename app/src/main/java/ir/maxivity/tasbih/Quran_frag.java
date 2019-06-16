package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Quran_frag extends Fragment {

    private LinearLayout surah_list_layout;
    private EditText search_surah_input;
    public Quran_frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View the_view = inflater.inflate(R.layout.fragment_home_frag, container, false);
        surah_list_layout = the_view.findViewById(R.id.surah_list_layout);
        search_surah_input = the_view.findViewById(R.id.search_surah_input);
        return the_view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search_surah_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                surah_list_layout.removeAllViews();
                addSurahs(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addSurahs("");
    }

    private void addSurahs(String search) {
        final ProgressDialog[] dialog = new ProgressDialog[1];

        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                dialog[0] = ProgressDialog.show(getContext(), "",
                        "درحال بارگیری اطلاعات", true);
                surah_list_layout.removeAllViews();
            }

            @SuppressLint("WrongThread")
            @Override
            protected Object doInBackground(Object... search) {
                try {
                    AssetManager assetManager = getActivity().getAssets();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("surah_list.json")));
                    String s = "";
                    StringBuilder jsonFileData = new StringBuilder();
                    while ((s = bufferedReader.readLine()) != null) {
                        jsonFileData.append(s);
                    }
                    if (search.equals("")) {
                        Log.d("json", jsonFileData.toString());
                        JSONArray surahList = new JSONArray(jsonFileData.toString());
                        for (int i = 0; i < surahList.length(); i++) {
                            JSONObject surahObj = surahList.getJSONObject(i);
                            String surahName = surahObj.getString("arabic");
                            int surahID = Integer.parseInt(surahObj.getString("id"));
                            final Button surahbtn = new Button(new ContextThemeWrapper(getContext(), R.style.sorehbtn));
                            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(50, 0, 50, 20);
                            surahbtn.setText(surahName);
                            surahbtn.setHeight(250);
                            surahbtn.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_qlist_item));
                            surahbtn.setId(surahID);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    surah_list_layout.addView(surahbtn, params);
                                }
                            });
                        }
                    } else {
                        Log.d("json", jsonFileData.toString());
                        JSONArray surahList = new JSONArray(jsonFileData.toString());
                        for (int i = 0; i < surahList.length(); i++) {
                            JSONObject surahObj = surahList.getJSONObject(i);
                            String surahName = surahObj.getString("arabic");
                            int surahID = Integer.parseInt(surahObj.getString("id"));
                            if (surahName.contains((CharSequence) search[0])) {
                                final Button surahbtn = new Button(new ContextThemeWrapper(getContext(), R.style.sorehbtn));
                                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(50, 0, 50, 20);
                                surahbtn.setText(surahName);
                                surahbtn.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_qlist_item));
                                surahbtn.setId(surahID);
                                surahbtn.setHeight(250);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surah_list_layout.addView(surahbtn, params);
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {
                dialog[0].dismiss();
            }
        };
        task.execute(search);
    }
}
