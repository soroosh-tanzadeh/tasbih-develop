package ir.maxivity.tasbih;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ir.maxivity.tasbih.dataAccess.DBConnection;
import ir.maxivity.tasbih.dataAccess.DataFileAccess;
import ir.maxivity.tasbih.dataAccess.LocalDB;
import ir.maxivity.tasbih.dataAccess.VerficationResult;

public class Login extends AppCompatActivity {

    private EditText phonenumber;
    private EditText verificationcode;
    private Button sendVirificationbtn;
    private Button login_submit;

    private VerficationResult verficationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        phonenumber = findViewById(R.id.phonenumber_input);
        verificationcode = findViewById(R.id.verification_input);
        sendVirificationbtn = findViewById(R.id.submitVerification_code);
        login_submit = findViewById(R.id.login_submit);
        phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
        final DataFileAccess fileAccess = new DataFileAccess(this);
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
        }
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
