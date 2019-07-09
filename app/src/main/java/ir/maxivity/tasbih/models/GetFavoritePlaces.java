package ir.maxivity.tasbih.models;

import java.util.ArrayList;

public class GetFavoritePlaces {

    public int result;
    public ArrayList<FavoriteResponse> data;


    public class FavoriteResponse {
        public String id;
        public String user_id;
        public String place_id;
    }

}
