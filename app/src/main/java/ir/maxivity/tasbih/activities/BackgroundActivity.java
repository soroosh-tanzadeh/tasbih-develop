package ir.maxivity.tasbih.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.avi.AVLoadingIndicatorView;

import ir.maxivity.tasbih.BaseActivity;
import ir.maxivity.tasbih.R;
import ir.maxivity.tasbih.adapters.BackgroundAdapter;
import ir.maxivity.tasbih.models.GetBackgroundResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundActivity extends BaseActivity {

    private RecyclerView gridView;
    private String messageUrl, messageName;
    private AVLoadingIndicatorView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        progress = findViewById(R.id.progress_dialog);
        progress.setVisibility(View.VISIBLE);
        gridView = findViewById(R.id.backgrounds);
        gridView.setLayoutManager(new GridLayoutManager(this, 3));
        messageUrl = getIntent().getStringExtra("MESSAGE_URL");
        messageName = getIntent().getStringExtra("MESSAGE_NAME");

        getBackgrounds();
    }


    private void getBackgrounds() {
        application.api.getBackgrounds().enqueue(new Callback<GetBackgroundResponse>() {
            @Override
            public void onResponse(Call<GetBackgroundResponse> call, Response<GetBackgroundResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        if (response.body().result == 1) {
                            BackgroundAdapter adapter = new BackgroundAdapter(BackgroundActivity.this, response.body().data);
                            gridView.setAdapter(adapter);
                            adapter.setCLickListener(new BackgroundAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String url) {
                                    Intent intent = new Intent(BackgroundActivity.this, AddMessagesActivity.class);
                                    intent.putExtra("MESSAGE_URL", messageUrl);
                                    intent.putExtra("MESSAGE_NAME", messageName);
                                    intent.putExtra("BACKGROUND_URL", url);
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<GetBackgroundResponse> call, Throwable t) {

            }
        });
    }
}
