package ir.maxivity.tasbih;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> placeId;

    public MutableLiveData<String> getPlaceId() {
        if (placeId == null) {
            placeId = new MutableLiveData<String>();
        }
        return placeId;
    }

}
