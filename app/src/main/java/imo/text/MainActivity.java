package imo.text;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Editor editor = findViewById(R.id.editor);

        String a = "";
        a += "never gonna give u up\n";
        a += "never gonna let u down\n";
        a += "never gonna run around and desert u\n";

        editor.setText(a);

        View keyboard = findViewById(R.id.keyboard);
        keyboard.post(new Runnable(){
                @Override
                public void run(){
                    VimMotion.mEditor = editor;
                    Keyboard.configKeyboard(MainActivity.this);
                }
            });
    }
}
