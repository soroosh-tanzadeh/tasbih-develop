package ir.maxivity.tasbih.interfaces;

import java.util.ArrayList;

import ir.maxivity.tasbih.models.GetPlaces;
import ir.maxivity.tasbih.models.GetQuranText;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {

    @Multipart
    @POST("get-places.php")
    Call<ArrayList<GetPlaces.response>> getPlaces(@Part("data") RequestBody body);

    @POST("get-qurantext.php")
    Call<GetQuranText> getQuranText(@Body int sura);
}
