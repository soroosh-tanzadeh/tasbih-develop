package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import tools.Utilities;

public class ZekrsList extends BaseActivity {

    LinearLayout zekrs_list_layout;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View actionbarview = getSupportActionBar().getCustomView();
        ImageButton share_btn = actionbarview.findViewById(R.id.sharebtn);
        ImageButton settings_btn = actionbarview.findViewById(R.id.settingsbtn);
        TextView persianDate = actionbarview.findViewById(R.id.persian_date_txt);
        TextView arabicDate = actionbarview.findViewById(R.id.arabic_date_text);

        persianDate.setText(Utilities.getTodayJalaliDate(this));
        arabicDate.setText(Utilities.getTodayIslamicDate(this) + " / " + Utilities.getTodayGregortianDate(this));

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });

        ScrollView scrollView = new ScrollView(this);
        LinearLayout the_view = new LinearLayout(this);
        zekrs_list_layout = new LinearLayout(this);
        zekrs_list_layout.setOrientation(LinearLayout.VERTICAL);
        the_view.setOrientation(LinearLayout.VERTICAL);
        EditText search_zekrs_input = new EditText(this);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_search_vector);
        drawable.setBounds(0, 24, 0, 24);
        search_zekrs_input.setCompoundDrawables(null, null, drawable, null);
        search_zekrs_input.setCompoundDrawablePadding(8);
        search_zekrs_input.setTextColor(R.color.black);
        scrollView.addView(zekrs_list_layout);
        the_view.addView(search_zekrs_input);
        the_view.addView(scrollView);
        setContentView(the_view);
        search_zekrs_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                zekrs_list_layout.removeAllViews();
                addZekrs(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addZekrs("");
    }

    private void addZekrs(String search) {
        final ProgressDialog[] dialog = new ProgressDialog[1];

        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                dialog[0] = ProgressDialog.show(ZekrsList.this, "",
                        "درحال بارگیری اطلاعات", true);
                zekrs_list_layout.removeAllViews();
            }

            @SuppressLint("WrongThread")
            @Override
            protected Object doInBackground(Object... search) {
                try {
                    AssetManager assetManager = getAssets();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("zekrs.json")));
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
                            final String zekrName = surahObj.getString("zekr");
                            final int zekrID = Integer.parseInt(surahObj.getString("id"));
                            final Button zekrbtn = new Button(new ContextThemeWrapper(ZekrsList.this, R.style.sorehbtn));
                            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(8, 0, 8, 20);
                            zekrbtn.setText(zekrName);
                            zekrbtn.setTextSize(20f);
                            zekrbtn.setHeight(100);
                            zekrbtn.setBackground(getResources().getDrawable(R.drawable.ic_qlist_item));
                            zekrbtn.setId(zekrID);

                            zekrbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(ZekrsList.this, ZekrCounter.class);
                                    i.putExtra("code",String.valueOf(zekrID));
                                    i.putExtra("zekrname",zekrName);
                                    startActivity(i);
                                }
                            });

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    zekrs_list_layout.addView(zekrbtn, params);
                                }
                            });
                        }
                    } else {
                        Log.d("json", jsonFileData.toString());
                        JSONArray surahList = new JSONArray(jsonFileData.toString());
                        for (int i = 0; i < surahList.length(); i++) {
                            JSONObject surahObj = surahList.getJSONObject(i);
                            final String zekrName = surahObj.getString("zekr");
                            final int zekrID = Integer.parseInt(surahObj.getString("id"));
                            if (zekrName.contains((CharSequence) search[0])) {
                                final Button zekrbtn = new Button(new ContextThemeWrapper(ZekrsList.this, R.style.sorehbtn));
                                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(50, 0, 50, 20);
                                zekrbtn.setText(zekrName);
                                zekrbtn.setBackground(getResources().getDrawable(R.drawable.ic_qlist_item));
                                zekrbtn.setId(zekrID);
                                zekrbtn.setHeight(250);

                                zekrbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(ZekrsList.this, ZekrCounter.class);
                                        i.putExtra("code",String.valueOf(zekrID));
                                        i.putExtra("zekrname",zekrName);
                                        startActivity(i);
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        zekrs_list_layout.addView(zekrbtn, params);
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
