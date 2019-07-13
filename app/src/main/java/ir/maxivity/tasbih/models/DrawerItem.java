package ir.maxivity.tasbih.models;

public class DrawerItem {

    private int drawable;
    private String text = "";

    public DrawerItem(int drawable, String text) {
        this.drawable = drawable;
        this.text = text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
