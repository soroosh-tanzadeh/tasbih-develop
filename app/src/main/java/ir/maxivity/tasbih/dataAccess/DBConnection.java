package ir.maxivity.tasbih.dataAccess;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DBConnection {

    private Context context;

    public DBConnection(Context context) {
        this.context = context;
    }

    public VerficationResult RequestVerficiationCode(String phonenumber) throws ExecutionException, InterruptedException, JSONException {
        String json = sendHttpRequest("http://tasbih.maxivityteam.ir/tasbih/WebService/doLogin.php?phone=" + phonenumber);
        JSONObject jsonObject = new JSONObject(json);
        String resultcode = jsonObject.getString("resultcode");
        if (Integer.parseInt(resultcode) == 1) {
            String userID = jsonObject.getString("user_id");
            String message = jsonObject.getString("msg");
            String sessionID = jsonObject.getString("data");
            VerficationResult result = new VerficationResult(userID, phonenumber, message, sessionID);
            return result;
        }
        return null;
    }

    public VerficationResult VerifyAccess(String phonenumber, String code, String userID, String sessionID) throws ExecutionException, InterruptedException, JSONException {
        String json = sendHttpRequest("http://tasbih.maxivityteam.ir/tasbih/WebService/VerifyCode.php?" +
                "phone=" + phonenumber +
                "&userid=" + userID +
                "&sessionid="
                + sessionID +
                "&code=" + code);

        JSONObject jsonObject = new JSONObject(json);
        String resultcode = jsonObject.getString("resultcode");
        if (Integer.parseInt(resultcode) == 1) {
            String message = jsonObject.getString("msg");
            String password = jsonObject.getString("data");
            VerficationResult result = new VerficationResult(userID, phonenumber, message, password);
            return result;
        }
        return null;
    }

    public String sendHttpRequest(String... uri){
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(uri[0]);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            StringBuilder sb = new StringBuilder();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                sb.append(current);
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
