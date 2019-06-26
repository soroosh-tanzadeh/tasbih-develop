package ir.maxivity.tasbih.interfaces;

import ir.maxivity.tasbih.models.GetPlaces;
import retrofit2.Call;
import retrofit2.http.POST;

public interface API {


    @POST("get-places.php")
    Call<GetPlaces> getPlaces();
}
