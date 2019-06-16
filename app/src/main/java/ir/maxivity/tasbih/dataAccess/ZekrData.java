package ir.maxivity.tasbih.dataAccess;

import java.io.Serializable;

public class ZekrData implements Serializable {

    int zekrnum = 0;
    private String zekr_code = "";

    public int getZekrnum() {
        return zekrnum;
    }

    public void setZekrnum(int zekrnum) {
        this.zekrnum = zekrnum;
    }

    public void setZekr_code(String zekr_code) {
        this.zekr_code = zekr_code;
    }

    public String getZekr_code() {
        return zekr_code;
    }
}
