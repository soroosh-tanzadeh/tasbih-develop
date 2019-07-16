package ir.maxivity.tasbih.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.maxivity.tasbih.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class APIHelper {
    private Retrofit retrofitClient;

    public APIHelper(final Context context, String baseUrl, @Nullable final String language) {


        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor retryInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                int tryCount = 0;
                while (!response.isSuccessful() && tryCount < 3) {
                    Log.d("intercept", "Request is not successful - " + tryCount);
                    tryCount++;
                    response = chain.proceed(request);
                }

                return response;
            }
        };

     /*   Interceptor globalHeaderAndUnauthorizedHandler = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Accept-Language", language == null ? "fa" : language)
                        .build();
                Response response = chain.proceed(newRequest);
                if(response.code() == 401){
                    ((IOnUnauthorized)context).onUnauthorized();
                }
                return response;
            }
        };*/

  /*      Interceptor globalServerErrorHandlerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .build();
                Response response = chain.proceed(newRequest);
                if(response.code() == 500){
                    try{
                        ((IServerErrors)context).onInternalServerError();
                    }catch (Exception e){
                        Log.d("SERVER_ERROR", "intercept: 500");
                    }
                }
                if(response.code() == 503){
                    try{
                        ((IServerErrors)context).onServiceUnavailable();
                    }catch (Exception e){
                        Log.d("SERVER_ERROR", "intercept: 503");
                    }
                }
                return response;
            }
        };*/
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
/*        okHttpBuilder.addInterceptor(globalHeaderAndUnauthorizedHandler);
        okHttpBuilder.addInterceptor(globalServerErrorHandlerInterceptor);*/
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(logger);
        }
        okHttpBuilder.addInterceptor(retryInterceptor);
        okHttpBuilder.connectTimeout(20, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(20, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = okHttpBuilder.build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient);
        retrofitClient = builder.build();
    }

    public <T> T getClient(Class<T> service) {
        return retrofitClient.create(service);
    }

}
