package ir.maxivity.tasbih;

import android.os.Bundle;
import android.widget.TextView;

public class QuranAdyehTextActivity extends BaseActivity {

    private int id;
    private String name;
    private TextView header, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_adyeh_text);
        header = findViewById(R.id.header_name_txt);
        content = findViewById(R.id.content_txt);
    }


    private void setContentText() {
        StringBuilder builder = new StringBuilder();

    }
}
