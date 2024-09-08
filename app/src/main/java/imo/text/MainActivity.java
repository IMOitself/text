package imo.text;

import android.app.Activity;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

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

        configKeyboard();
    }

    public void configKeyboard() {
        // Get screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Calculate keyboard height (1/3 of screen height)
        int keyboardHeight = screenHeight / 3;

        // Calculate key dimensions
        int normalKeyWidth = screenWidth / 10;
        int normalKeyHeight = keyboardHeight / 4; // (2/3 of keyboard height) / 5 rows
        int numberKeyHeight = normalKeyHeight * 3 / 4;
        int wideKeyWidth = normalKeyWidth + (normalKeyWidth / 2);
        
        List<TextView> allKeyViews = new ArrayList<>();
        
        int[] numberSizeKeys = {
            // first row
            R.id.key_1, 
            R.id.key_2,
            R.id.key_3,
            R.id.key_4,
            R.id.key_5,
            R.id.key_6,
            R.id.key_7,
            R.id.key_8,
            R.id.key_9,
            R.id.key_0
        };
        
        int[] normalSizeKeys = {
            // second row
            R.id.key_Q,
            R.id.key_W,
            R.id.key_E,
            R.id.key_R,
            R.id.key_T,
            R.id.key_Y,
            R.id.key_U,
            R.id.key_I,
            R.id.key_O,
            R.id.key_P,
            // third row
            R.id.key_A,
            R.id.key_S,
            R.id.key_D,
            R.id.key_F,
            R.id.key_G,
            R.id.key_H,
            R.id.key_J,
            R.id.key_K,
            R.id.key_L,
            // fourth row
            R.id.key_Z,
            R.id.key_X,
            R.id.key_C,
            R.id.key_V,
            R.id.key_B,
            R.id.key_N,
            R.id.key_M,
            // fifth row
            R.id.key_comma,
            R.id.key_period
        };
        
        int[] wideSizeKeys = {
            R.id.key_shift,
            R.id.key_backspace,
            R.id.key_ctrl,
            R.id.key_go
        };
        
        for(int id : numberSizeKeys){
            configKey(id, normalKeyWidth, numberKeyHeight);
            allKeyViews.add(findViewById(id));
        }
        for(int id : normalSizeKeys){
            configKey(id, normalKeyWidth, normalKeyHeight);
            allKeyViews.add(findViewById(id));
        }
        
        for(int id : wideSizeKeys){
            configKey(id, wideKeyWidth, normalKeyHeight);
            allKeyViews.add(findViewById(id));
        }
        
        configKey(R.id.key_space, normalKeyWidth * 5, normalKeyHeight, (int) (normalKeyWidth * 0.1));
        allKeyViews.add(findViewById(R.id.key_space));
    }

    private void configKey(int viewId, int width, int height) {
        configKey(viewId, width, height, -1);
    }

    private void configKey(int viewId, int width, int height, int custompaddingH){
        View view = findViewById(viewId);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.layer_key_bg);
        int paddingH = (int) (width * 0.1); // 10% of width
        int paddingV = (int) (height * 0.1); // 10% of height
        if (custompaddingH != -1) paddingH = custompaddingH;

        layerDrawable.setLayerInset(0, paddingH, paddingV, paddingH, paddingV);

        view.setBackground(layerDrawable);
    }
}