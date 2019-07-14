package ir.maxivity.tasbih.reminderTools;

public class Reminder {
    private int mID;
    private String mTitle;
    private String mDate;
    private String mTime;
    private String mRepeat = "2";
    private String mRepeatNo = "2";
    private String mRepeatType = "Minute";
    private String mActive = "true";

    public Reminder(int ID, String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active) {
        mID = ID;
        mTitle = Title;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatNo = RepeatNo;
        mRepeatType = RepeatType;
        mActive = Active;
    }

    public Reminder(String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active) {
        mTitle = Title;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatNo = RepeatNo;
        mRepeatType = RepeatType;
        mActive = Active;
    }

    public Reminder(String Title, String Date, String Time) {
        mTitle = Title;
        mDate = Date;
        mTime = Time;
    }

    public Reminder() {
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String mRepeat) {
        this.mRepeat = mRepeat;
    }

    public String getRepeatNo() {
        return mRepeatNo;
    }

    public void setRepeatNo(String mRepeatNo) {
        this.mRepeatNo = mRepeatNo;
    }

    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String mRepeatType) {
        this.mRepeatType = mRepeatType;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String mActive) {
        this.mActive = mActive;
    }
}
