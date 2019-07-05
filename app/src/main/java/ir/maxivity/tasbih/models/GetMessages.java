package ir.maxivity.tasbih.models;

import java.util.List;

public class GetMessages {

    public int result;
    public List<Message> data;


    public class Message {
        public String name;
        public String url;
    }
}
