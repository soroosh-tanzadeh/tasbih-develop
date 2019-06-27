package ir.maxivity.tasbih.models;

public class GetPlacesBody {

    private String distance;
    private String ulat;
    private String ulong;

    public GetPlacesBody(String distance, String ulat, String ulong) {
        this.distance = distance;
        this.ulat = ulat;
        this.ulong = ulong;
    }

}
