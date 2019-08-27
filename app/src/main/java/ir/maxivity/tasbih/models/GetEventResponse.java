package ir.maxivity.tasbih.models;

import java.io.Serializable;
import java.util.ArrayList;

public class GetEventResponse implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    public int result;
    public ArrayList<EventResponse> data;


    public class EventResponse implements Serializable {
        private static final long serialVersionUID = -7060210544600464481L;

        public String id;
        public String thumbnail;
        public String place_id;
        public String offer_description;
        public String disable;
        public String indate;
        public int expire_by;
    }
}
