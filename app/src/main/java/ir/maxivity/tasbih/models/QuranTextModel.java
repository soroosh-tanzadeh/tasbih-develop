package ir.maxivity.tasbih.models;

public class QuranTextModel {

    private String arabic, persian, english;

    public QuranTextModel(String arabic, String persian, String english) {
        this.arabic = arabic;
        this.persian = persian;
        this.english = english;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getPersian() {
        return persian;
    }

    public void setPersian(String persian) {
        this.persian = persian;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
