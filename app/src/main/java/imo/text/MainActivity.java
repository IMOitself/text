package imo.text;

import android.app.Activity;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

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

        configKey(R.id.key_0, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_1, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_2, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_3, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_4, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_5, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_6, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_7, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_8, normalKeyWidth, numberKeyHeight);
        configKey(R.id.key_9, normalKeyWidth, numberKeyHeight);

        configKey(R.id.key_Q, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_W, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_E, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_R, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_T, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_Y, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_U, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_I, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_O, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_P, normalKeyWidth, normalKeyHeight);

        configKey(R.id.key_A, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_S, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_D, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_F, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_G, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_H, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_J, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_K, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_L, normalKeyWidth, normalKeyHeight);

        configKey(R.id.key_shift, wideKeyWidth, normalKeyHeight);
        configKey(R.id.key_Z, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_X, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_C, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_V, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_B, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_N, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_M, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_backspace, wideKeyWidth, normalKeyHeight);

        configKey(R.id.key_ctrl, wideKeyWidth, normalKeyHeight);
        configKey(R.id.key_comma, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_space, normalKeyWidth * 5, normalKeyHeight, (int) (normalKeyWidth * 0.1));
        configKey(R.id.key_period, normalKeyWidth, normalKeyHeight);
        configKey(R.id.key_go, wideKeyWidth, normalKeyHeight);
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