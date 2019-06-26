package ir.maxivity.tasbih.models;

import java.util.ArrayList;

public class GetPlaces {

    private Body data;

    public GetPlaces(Body data) {
        this.data = data;
    }

    ArrayList<response> places = new ArrayList<>();


    class response {
        public String id;
        public String user_id;
        public String img_address;
        public String verify;
        public String img_documents;
        public String place_name;
        public String description;
        public String phone;
        public String web_address;
        public String indate;
        public String lat;
        public String lon;
    }

    class Body {
        public String distance;
        public String ulat;
        public String ulong;
    }
}
