package ir.maxivity.tasbih.interfaces;

import java.util.HashMap;

public interface MapListener {

    void onAddLocationSubmit();
    void onAddLocationCancel();
    void onAddLocationInfoSubmit(HashMap<String , String> fields);
    void onAddLocationInfoCancel();

    void onSelectFilter(String name);

    void onFavoritePlaceClick(String id);

    void onAddEventClick(String id);

    void onEditSubmit(HashMap<String, String> fields);


}
