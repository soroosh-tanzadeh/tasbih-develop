package ir.maxivity.tasbih;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import ir.maxivity.tasbih.models.GetQuranText;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

public class QuranAdyehTextActivity extends BaseActivity {

    private static final String TAG = "FUCK QURAN";
    private int id;
    private String name;
    private TextView header, content;
    private Boolean quranType;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_adyeh_text);
        header = findViewById(R.id.header_name_txt);
        content = findViewById(R.id.content_txt);


        id = getIntent().getIntExtra("ID", 0);
        name = getIntent().getStringExtra("NAME");
        quranType = getIntent().getBooleanExtra("QURAN", true);

        if (quranType)
            getSuraText();
        else
            getAdyehText();

        header.setText(name);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setContentText(ArrayList<GetQuranText> quranTexts) {
        StringBuilder builder = new StringBuilder();

        for (GetQuranText quran : quranTexts) {
            builder.append(quran.text);
            builder.append(" (");
            builder.append(Utilities.numberConvert_En2Fa(quran.aya));
            builder.append(")");
            builder.append("\n");
        }

        content.setText(builder.toString());
    }


    private void getSuraText() {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        String data = gson.toJson(id);

        application.api.getQuranText(RequestBody.create(JSON, data)).enqueue(new Callback<ArrayList<GetQuranText>>() {
            @Override
            public void onResponse(Call<ArrayList<GetQuranText>> call, Response<ArrayList<GetQuranText>> response) {
                try {

                    if (response.isSuccessful()) {
                        setContentText(response.body());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(QuranAdyehTextActivity.this, "مشکلی بوجود آمده دوباره تلاش کنید", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetQuranText>> call, Throwable t) {
                Log.v(TAG, "fail : " + t.getMessage());
            }
        });
    }

    private void getAdyehText() {
        Log.v(TAG, "adyeh");
    }
}
