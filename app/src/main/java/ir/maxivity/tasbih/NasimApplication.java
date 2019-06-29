package ir.maxivity.tasbih;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ir.maxivity.tasbih.interfaces.API;
import ir.maxivity.tasbih.network.APIHelper;

public class NasimApplication extends Application {

    private final static String BASE_URL = "http://tasbih.maxivityteam.ir/tasbih/WebService/";
    public API api;
    private final String PREF_TOKEN_KEY = "TOKEN";
    private final String PREF_USER_ID_KEY = "USER_ID";
    private final String MAIN_PREF_NAME = "NASIM_PREF_FILE";


    @Override
    public void onCreate() {
        super.onCreate();
        initApiClient();
    }

    public void initApiClient() {
        APIHelper apiHelper = new APIHelper(this, BASE_URL, null);
        api = apiHelper.getClient(API.class);
    }


    public void setToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_TOKEN_KEY, null);
    }

    public void setUserId(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USER_ID_KEY, null);
    }




}
