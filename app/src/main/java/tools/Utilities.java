package tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.pixplicity.sharp.Sharp;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utilities {

    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String numberConvert_Fa2En(@NonNull String rawStr) {
        StringBuffer stringBuffer = new StringBuffer();
        char c;
        for (int i = 0; i < rawStr.length(); i++) {
            c = rawStr.charAt(i);
            if (c <= '۹' && c >= '۰')
                stringBuffer.append((char) (c - '۰' + '0'));
            else
                stringBuffer.append(c);
        }
        return String.valueOf(stringBuffer);
    }


    // a mapping which used in numberConvert_En2Fa
    private static String[] faDigits = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};


    public static String numberConvert_En2Fa(@NonNull String rawStr) {
        if (rawStr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        char c;
        for (int i = 0; i < rawStr.length(); i++) {
            c = rawStr.charAt(i);
            if (c <= '9' && c >= '0')
                stringBuffer.append(faDigits[c - '0']);
            else
                stringBuffer.append(c);

        }
        return String.valueOf(stringBuffer);
    }


    public static <T> String createBody(T body) {
        Gson gson = new Gson();
        return gson.toJson(body);
    }

    private static OkHttpClient httpClient;

    public static void fetchSvg(Context context, String url, final ImageView target) {
        if (httpClient == null) {
            // Use cache for performance and basic offline capability
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1014))
                    .build();
        }

        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // target.setImageDrawable(R.drawable.fallback_image);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream stream = response.body().byteStream();
                Sharp.loadInputStream(stream).into(target);
                stream.close();
            }
        });
    }


    public static Bitmap base64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
