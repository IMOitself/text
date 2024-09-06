package imo.text;

import android.app.Activity;
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

        resizeKeyboard();
    }

    public void resizeKeyboard() {
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

        // Resize number keys (1st row)
        resizeView(R.id.key_0, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_1, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_2, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_3, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_4, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_5, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_6, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_7, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_8, normalKeyWidth, numberKeyHeight);
        resizeView(R.id.key_9, normalKeyWidth, numberKeyHeight);

        // Resize letter keys (2nd row)
        resizeView(R.id.key_Q, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_W, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_E, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_R, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_T, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_Y, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_U, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_I, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_O, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_P, normalKeyWidth, normalKeyHeight);

        // Resize letter keys (3rd row)
        resizeView(R.id.key_A, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_S, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_D, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_F, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_G, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_H, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_J, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_K, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_L, normalKeyWidth, normalKeyHeight);

        // Resize letter keys (4th row)
        resizeView(R.id.key_shift, wideKeyWidth, normalKeyHeight);
        resizeView(R.id.key_Z, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_X, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_C, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_V, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_B, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_N, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_M, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_delete, wideKeyWidth, normalKeyHeight);

        // Resize special keys (5th row)
        resizeView(R.id.key_ctrl, wideKeyWidth, normalKeyHeight);
        resizeView(R.id.key_comma, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_space, normalKeyWidth * 5, normalKeyHeight);
        resizeView(R.id.key_period, normalKeyWidth, normalKeyHeight);
        resizeView(R.id.key_go, wideKeyWidth, normalKeyHeight);
    }

    private void resizeView(int viewId, int width, int height) {
        View view = findViewById(viewId);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }
}