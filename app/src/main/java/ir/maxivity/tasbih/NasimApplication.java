package ir.maxivity.tasbih;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ir.maxivity.tasbih.interfaces.API;
import ir.maxivity.tasbih.network.APIHelper;

public class NasimApplication extends Application {

    private final static String BASE_URL = "http://webflax.ir/tasbih/tasbih/WebService/";
    public API api;
    private final String PREF_TOKEN_KEY = "TOKEN";
    private final String PREF_USER_ID_KEY = "USER_ID";
    private final String PREF_LANGUAGE_KEY = "LANGUAGE";
    private final String PREF_LOGIN_LATER_KEY = "LOGIN_LATER";
    private final String PREF_AZAN_IDS_KEY = "AZAN_ID";
    private final String PREF_AZAN_REFRESH_KEY = "AZAN_REFRESH";
    private final String PREF_APP_SOUND = "SOUND_MODE";
    private final String PREF_AZAN_REFRESH_ID = "REFRESH_ID";

    public static final String PREF_USER_LOCATION_KEY = "USER_LOCATION";
    public static final String MAIN_PREF_NAME = "NASIM_PREF_FILE";


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

    public void setLanguage(String language) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LANGUAGE_KEY, language);
        editor.apply();
    }

    public String getLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_LANGUAGE_KEY, null);
    }

    public void setLoginLater(boolean loginLater) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_LOGIN_LATER_KEY, loginLater);
        editor.apply();
    }

    public boolean getLoginLater() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_LOGIN_LATER_KEY, false);
    }

    public void setUserLocation(String location) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USER_LOCATION_KEY, location);
        editor.apply();
    }

    public String getUserLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USER_LOCATION_KEY, null);
    }

    public void setAzanReminderIds(String Ids) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_AZAN_IDS_KEY, Ids);
        editor.apply();
    }

    public String getReminderIds() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_AZAN_IDS_KEY, null);
    }

    public void setAzanRefreshId(int id) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_AZAN_REFRESH_ID, id);
        editor.apply();
    }

    public int getAzanRefreshId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(PREF_AZAN_REFRESH_ID, 0);
    }


    public void setAzanRefreshKey(long day) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(PREF_AZAN_REFRESH_KEY, day);
        editor.apply();
    }

    public long getAzanRefresh() {
        long def = 0L;
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getLong(PREF_AZAN_REFRESH_KEY, def);
    }

    public void setApplicationSound(String mode) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_APP_SOUND, mode);
        editor.apply();
    }

    public String getSoundMode() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_APP_SOUND, null);
    }

}
