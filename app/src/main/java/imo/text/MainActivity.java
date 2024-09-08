package imo.text;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Editor editor = findViewById(R.id.editor);
        StringBuilder sb = new StringBuilder();
        sb.append("america ya!");
        for(int i = 0; i < 99; i++){
            sb.append("\nhallo:D");
        }
        editor.setText(sb.toString());


        View keyboard = findViewById(R.id.keyboard);
        keyboard.post(new Runnable(){
                @Override
                public void run(){
                    configKeyboard();
                }
            });
    }

    //TODO: move all this keyboard configuring stuff to another file
    int keyPaddingW, keyPaddingH;
    int keyCornerRadius;

    public void configKeyboard(){
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

        // Get screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Calculate keyboard height (1/3 of screen height)
        int keyboardHeight = screenHeight / 3;

        // Calculate key dimensions
        int normalKeyWidth = screenWidth / 10;
        int normalKeyHeight = keyboardHeight / 4;
        int numberKeyHeight = normalKeyHeight * 3 / 4;
        int wideKeyWidth = normalKeyWidth + (normalKeyWidth / 2);

        keyPaddingW = (int) (normalKeyWidth * 0.1f); // 10% width
        keyPaddingH = (int) (normalKeyHeight * 0.1f);// 10% height
        keyCornerRadius = normalKeyWidth / 8;

        // Set number keys size (shorter than normal)
        for(int id : numberSizeKeys){
            configKey(id, normalKeyWidth, numberKeyHeight, R.color.key_primary);
            allKeyViews.add((TextView) findViewById(id));
        }
        // Set default keys size
        for(int id : normalSizeKeys){
            configKey(id, normalKeyWidth, normalKeyHeight, R.color.key_primary);
            allKeyViews.add((TextView) findViewById(id));
        }
        // Set special keys size (wider than normal)
        for(int id : wideSizeKeys){
            configKey(id, wideKeyWidth, normalKeyHeight, R.color.key_secondary);
            allKeyViews.add((TextView) findViewById(id));
        }
        // Set space key size (5x wider than normal)
        configKey(R.id.key_space, normalKeyWidth * 5, normalKeyHeight, R.color.key_primary);
        allKeyViews.add((TextView) findViewById(R.id.key_space));
    }

    private void configKey(int viewId, int width, int height, int colorId){
        View view = findViewById(viewId);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        RectF rect = new RectF();
        rect.left = 0 + keyPaddingW;
        rect.top = 0 + keyPaddingH;
        rect.right = width - keyPaddingW;
        rect.bottom = height - keyPaddingH;
        
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(colorId));

        canvas.drawRoundRect(rect, keyCornerRadius, keyCornerRadius, paint);

        view.setBackground(new BitmapDrawable(getResources(), bitmap));
    }
}
