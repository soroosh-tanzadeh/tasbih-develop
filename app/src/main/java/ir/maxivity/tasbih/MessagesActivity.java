package ir.maxivity.tasbih;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.maxivity.tasbih.adapters.MessagesAdapter;
import ir.maxivity.tasbih.models.GetMessages;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<GetMessages.Message> messages;
    private MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messages = new ArrayList<>();

        initViews();

        getMessagesRequest();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.messages_list);
        adapter = new MessagesAdapter(this, messages);
        recyclerView.setAdapter(adapter);
    }


    private void getMessagesRequest() {
        final NasimDialog loading = showLoadingDialog();
        loading.show();
        final NasimDialog error = showServerErrorDialog();
        application.api.getMessages().enqueue(new Callback<GetMessages>() {
            @Override
            public void onResponse(Call<GetMessages> call, Response<GetMessages> response) {
                loading.dismiss();
                try {
                    if (response.body().result == 1) {
                        messages = response.body().data;
                        adapter.updateList(messages);
                    } else {
                        error.setPositiveButton(getString(R.string.ok), new NasimDialog.OnPositiveButtonClick() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                        error.show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetMessages> call, Throwable t) {

                showShortToast(getString(R.string.request_failure_text));
            }
        });
    }
}
