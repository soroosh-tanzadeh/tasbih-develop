package ir.maxivity.tasbih.dataAccess;

public class VerficationResult {
    private String userID;
    private String phoneNumber;
    private String message;
    private String data;

    public VerficationResult(String userID, String phoneNumber, String message, String data) {
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.data = data;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
