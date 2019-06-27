package ir.maxivity.tasbih;

import android.app.Application;

import ir.maxivity.tasbih.interfaces.API;
import ir.maxivity.tasbih.network.APIHelper;

public class NasimApplication extends Application {

    private final static String BASE_URL = "http://tasbih.maxivityteam.ir/tasbih/WebService/";
    public API api;

    @Override
    public void onCreate() {
        super.onCreate();
        initApiClient();
    }

    public void initApiClient() {
        APIHelper apiHelper = new APIHelper(this, BASE_URL, null);
        api = apiHelper.getClient(API.class);
    }
}
