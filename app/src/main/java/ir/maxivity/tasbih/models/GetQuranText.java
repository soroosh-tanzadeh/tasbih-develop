package ir.maxivity.tasbih.models;

import java.util.ArrayList;

public class GetQuranText {

    public int result;
    public ArrayList<QuranResponse> data;


    public class QuranResponse {
        public String index;
        public String sura;
        public String aya;
        public String text;
    }

}
