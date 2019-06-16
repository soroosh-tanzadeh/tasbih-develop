
package ir.maxivity.tasbih;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Sidebar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);
        this.overridePendingTransition(R.anim.slide_right,
                R.anim.fixed_anime);
        Button dologin = findViewById(R.id.dologin);
        dologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Sidebar.this,Login.class);
                startActivity(t);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_to_right);
    }
}
