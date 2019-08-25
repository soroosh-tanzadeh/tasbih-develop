package ir.maxivity.tasbih;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import ir.maxivity.tasbih.dataAccess.VerficationResult;
import ir.maxivity.tasbih.models.LoginResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

public class Login extends BaseActivity {

    private static final String TAG = "FUCK LOGIN";
    private EditText phonenumber;
    private EditText verificationcode;
    private Button sendVirificationbtn;
    private Button login_submit;
    private TextView loginLater;
    private LoginResponse mResponse;
    private ScrollView loginScroll;
    private NestedScrollView parentScroll;

    private VerficationResult verficationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        phonenumber = findViewById(R.id.phonenumber_input);
        phonenumber.setHint(Utilities.numberConvert_En2Fa("09361234567"));
        phonenumber.setHintTextColor(ContextCompat.getColor(this, R.color.gray_400));
        verificationcode = findViewById(R.id.verification_input);
        sendVirificationbtn = findViewById(R.id.submitVerification_code);
        sendVirificationbtn.setVisibility(View.GONE);
        login_submit = findViewById(R.id.login_submit);
        loginLater = findViewById(R.id.login_later);
        loginScroll = findViewById(R.id.login_form);
        parentScroll = findViewById(R.id.parent_scroll);
        mResponse = new LoginResponse();
        phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });

        KeyboardVisibilityEvent.setEventListener(
                this, new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            loginScroll.fullScroll(View.FOCUS_DOWN);
                            parentScroll.fullScroll(View.FOCUS_DOWN);
                        }
                    }
                }
        );

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest(phonenumber.getText().toString());
            }
        });

        sendVirificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificationcode.getText().length() == 4) {
                    verificationRequest(mResponse, verificationcode.getText().toString());
                } else {
                    showShortToast("لطفا کد را وارد کنید");
                }
            }
        });

        loginLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.setLoginLater(true);
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void verificationRequest(LoginResponse response, String verifyCode) {
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), response.phoneNumber);
        RequestBody sessionId = RequestBody.create(Utilities.TEXT, response.data);
        RequestBody userId = RequestBody.create(Utilities.TEXT, response.user_id);
        RequestBody code = RequestBody.create(Utilities.TEXT, verifyCode);
        final NasimDialog dialog = showLoadingDialog();
        dialog.show();

        application.api.verfiyCode(phone, sessionId, code, userId).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    application.setToken(response.body().data);
                    application.setUserId(response.body().user_id);
                    application.setLoginLater(false);
                    //Toast.makeText(getApplicationContext(), "result ok going to main", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showShortToast("در حال حاظر امکان ثبت نام وجود ندارد");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dialog.dismiss();
                showShortToast(t.getMessage());
            }
        });
    }
    private void loginRequest(String phone) {

        String body = phone;
        final NasimDialog dialog = showLoadingDialog();
        dialog.show();
        application.api.doLogin(RequestBody.create(Utilities.TEXT, body)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                dialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().resultcode == 1) {
                           /* application.setToken(response.body().data);
                            application.setUserId(response.body().user_id);
                            application.setLoginLater(false);
                            Toast.makeText(getApplicationContext(), "result ok going to main", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                            goToVerificationMode();
                            mResponse = response.body();

                        } else {
                            Toast.makeText(getApplicationContext(), response.body().msg, Toast.LENGTH_SHORT).show();

                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "some thing is null from server", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dialog.dismiss();
                Log.v(TAG, "fail : " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        phonenumber.clearComposingText();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onBackPressed();
    }

    private void goToPhoneInputMode() {
        phonenumber.setVisibility(View.VISIBLE);
        login_submit.setVisibility(View.VISIBLE);

        verificationcode.setVisibility(View.GONE);
        sendVirificationbtn.setVisibility(View.GONE);
    }

    private void goToVerificationMode() {
        phonenumber.setVisibility(View.GONE);
        login_submit.setVisibility(View.GONE);

        verificationcode.setVisibility(View.VISIBLE);
        sendVirificationbtn.setVisibility(View.VISIBLE);
    }

}
