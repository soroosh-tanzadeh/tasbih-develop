package ir.maxivity.tasbih;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MessagesList extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


    }
}
