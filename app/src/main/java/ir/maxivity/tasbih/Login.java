package ir.maxivity.tasbih;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ir.maxivity.tasbih.dataAccess.VerficationResult;
import ir.maxivity.tasbih.models.LoginResponse;
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

    private VerficationResult verficationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        phonenumber = findViewById(R.id.phonenumber_input);
        verificationcode = findViewById(R.id.verification_input);
        sendVirificationbtn = findViewById(R.id.submitVerification_code);
        sendVirificationbtn.setVisibility(View.GONE);
        login_submit = findViewById(R.id.login_submit);
        loginLater = findViewById(R.id.login_later);
        phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
       /* final DataFileAccess fileAccess = new DataFileAccess(this);
        try {
            final LocalDB[] localDB = {fileAccess.readLocalDB()};
            if (true) {
                if (localDB[0] == null || !localDB[0].isLogedin()) {
                    goToPhoneInputMode();
                    login_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DBConnection dbConnection = new DBConnection(Login.this);
                            @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> at = new AsyncTask<String, String, String>() {
                                final ProgressDialog[] dialog = new ProgressDialog[1];

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    dialog[0] = ProgressDialog.show(Login.this, "",
                                            "لطفا شکیبا باشید...", true);
                                }

                                @Override
                                protected String doInBackground(String... uri) {
                                    try {
                                        VerficationResult result = dbConnection.RequestVerficiationCode(phonenumber.getText().toString());
                                        String sessionID = result.getData();
                                        Login.this.verficationResult = result;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                goToVerificationMode();
                                            }
                                        });
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    dialog[0].dismiss();
                                }
                            };
                            at.execute();
                        }
                    });

                    sendVirificationbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DBConnection dbConnection = new DBConnection(Login.this);
                            @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> at = new AsyncTask<String, String, String>() {
                                final ProgressDialog[] dialog = new ProgressDialog[1];

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    dialog[0] = ProgressDialog.show(Login.this, "",
                                            "لطفا شکیبا باشید...", true);
                                }

                                @Override
                                protected String doInBackground(String... uri) {
                                    try {
                                        VerficationResult result = dbConnection.VerifyAccess(Login.this.verficationResult.getPhoneNumber(), Login.this.verificationcode.getText().toString(),
                                                Login.this.verficationResult.getUserID(), Login.this.verficationResult.getData());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (localDB[0] == null) {
                                                    localDB[0] = new LocalDB();
                                                }
                                                localDB[0].setLogedin(true);
                                                try {
                                                    fileAccess.writeLocalDB(localDB[0]);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                Intent intent = new Intent(Login.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    dialog[0].dismiss();
                                }
                            };
                            at.execute();
                        }
                    });
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } catch (Exception ex) {
            Log.e("Login Page", ex.getMessage());
        }*/

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo move authorization out of the click
                if (application.getToken() == null)
                    loginRequest(phonenumber.getText().toString());
                else {
                    Log.v(TAG, "user id : " + application.getUserId());
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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

    private void loginRequest(String phone) {

        String body = Utilities.createBody(phone);
        final NasimDialog dialog = showLoadingDialog();
        dialog.show();
        application.api.doLogin(RequestBody.create(Utilities.TEXT, body)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                dialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().resultcode == 1) {
                            application.setToken(response.body().data);
                            application.setUserId(response.body().user_id);
                            application.setLoginLater(false);
                            Toast.makeText(getApplicationContext(), "result ok going to main", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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
