package ir.maxivity.tasbih.dataAccess;

import java.io.Serializable;

public class LocalDB implements Serializable {

    private String language = "fa";
    private int moazenCode = 0;
    private double[] city = new double[2];
    private boolean soundon = true;
    private boolean isLogedin = false;
    private String phoneNumber = null;
    private String password = null;
    private String email = null;
    private String userID = null;
    private boolean loginanothertime = false;

    public boolean isLoginanothertime() {
        return loginanothertime;
    }

    public void setLoginanothertime(boolean loginanothertime) {
        this.loginanothertime = loginanothertime;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getMoazenCode() {
        return moazenCode;
    }

    public void setMoazenCode(int moazenCode) {
        this.moazenCode = moazenCode;
    }

    public double[] getCity() {
        return city;
    }

    public void setCity(double[] city) {
        this.city = city;
    }

    public boolean isSoundon() {
        return soundon;
    }

    public void setSoundon(boolean soundon) {
        this.soundon = soundon;
    }

    public boolean isLogedin() {
        return isLogedin;
    }

    public void setLogedin(boolean logedin) {
        isLogedin = logedin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
