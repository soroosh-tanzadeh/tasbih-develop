package ir.maxivity.tasbih.interfaces;

import ir.maxivity.tasbih.models.AddFavortiePlace;
import ir.maxivity.tasbih.models.AddNewEvent;
import ir.maxivity.tasbih.models.AddNewPlaceResponse;
import ir.maxivity.tasbih.models.GetBackgroundResponse;
import ir.maxivity.tasbih.models.GetFavoritePlaces;
import ir.maxivity.tasbih.models.GetMessages;
import ir.maxivity.tasbih.models.GetPlaces;
import ir.maxivity.tasbih.models.GetQuranText;
import ir.maxivity.tasbih.models.GetQuranVoice;
import ir.maxivity.tasbih.models.LoginResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API {

    @Multipart
    @POST("get-places.php")
    Call<GetPlaces> getPlaces(@Part("data") RequestBody body);

    @Multipart
    @POST("get-qurantext.php")
    Call<GetQuranText> getQuranText(@Part("sura") RequestBody body);


    @Multipart
    @POST("doLogin.php")
    Call<LoginResponse> doLogin(@Part("phone") RequestBody body);

    @Multipart
    @POST("new-place.php")
    Call<AddNewPlaceResponse> addPlace(@Part("data") RequestBody body);

    @GET("get-messages.php")
    Call<GetMessages> getMessages();

    @Multipart
    @POST("get-place.php")
    Call<GetPlaces> getPlace(@Part("s") RequestBody body);

    @Multipart
    @POST("add-favorite-place.php")
    Call<AddFavortiePlace> addFavoritePlace(@Part("user_id") RequestBody user_id
            , @Part("pass") RequestBody pass
            , @Part("place_id") RequestBody place_id);

    @Multipart
    @POST("get-favorite-places.php")
    Call<GetFavoritePlaces> getFavoritePlaces(@Part("user_id") RequestBody user_id
            , @Part("pass") RequestBody pass);


    @Multipart
    @POST("new-event.php")
    Call<AddNewEvent> addnewEvent(@Part("data") RequestBody body);


    @GET("get-qaudio.php")
    Call<GetQuranVoice> getQuranVoice(@Query("n") int number);

    @Multipart
    @POST("get-qurantranslate.php")
    Call<GetQuranText> getQuranTranslate(@Part("lang") RequestBody lang, @Part("sura") RequestBody sura);

    @Multipart
    @POST("verifycode.php")
    Call<LoginResponse> verfiyCode(@Part("phone") RequestBody phone,
                                   @Part("sessionid") RequestBody sessionId,
                                   @Part("code") RequestBody code,
                                   @Part("userid") RequestBody userId);

    @GET("get-background.php")
    Call<GetBackgroundResponse> getBackgrounds();

}
