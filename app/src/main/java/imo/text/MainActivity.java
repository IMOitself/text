package imo.text;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Editor editor = findViewById(R.id.editor);
        StringBuilder sb = new StringBuilder();
        sb.append("america ya!");
        for (int i = 0; i < 99; i++) {
            sb.append("\nhallo:D");
        }
        editor.setText(sb.toString());
    }
}