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
        StringBuilder sb = new StringBuilder();
        sb.append("america ya! :3");
        for(int i = 0; i < 99; i++){
            sb.append("\nhallo:D");
        }
        editor.setText(sb.toString());


        View keyboard = findViewById(R.id.keyboard);
        keyboard.post(new Runnable(){
                @Override
                public void run(){
                    Keyboard.mEditor = editor;
                    Keyboard.configKeyboard(MainActivity.this);
                }
            });
    }
}
