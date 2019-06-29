package tools;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import okhttp3.MediaType;

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


}
