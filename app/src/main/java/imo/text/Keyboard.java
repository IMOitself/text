package imo.text;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Keyboard {
    static Activity mActivity;
    static Editor mEditor;
    static int keyPaddingW, keyPaddingH;
    static int keyCornerRadius;
    static float keyTextSize = -1;

    public static void configKeyboard(Activity activity){
        mActivity = activity;

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
            R.id.key_ctrl
        };

        // Get screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Calculate keyboard height (between 1/3 and 1/4 of screen)
        int keyboardHeight = (screenHeight * 7) / 24;

        // Calculate key dimensions
        int normalKeyWidth = screenWidth / 10;
        int normalKeyHeight = keyboardHeight / 4;
        int numberKeyHeight = normalKeyHeight * 3 / 4;
        int wideKeyWidth = normalKeyWidth + (normalKeyWidth / 2);

        keyPaddingW = (int) (normalKeyWidth * 0.1f); // 10% width
        keyPaddingH = (int) (normalKeyHeight * 0.1f);// 10% height
        keyCornerRadius = normalKeyWidth / 8;

        // Configure keys
        for (int id : numberSizeKeys)
            configKey(id, normalKeyWidth, numberKeyHeight, R.color.key_primary);

        for (int id : normalSizeKeys)
            configKey(id, normalKeyWidth, normalKeyHeight, R.color.key_primary);

        for (int id : wideSizeKeys)
            configKey(id, wideKeyWidth, normalKeyHeight, R.color.key_secondary);

        configKey(R.id.key_space, normalKeyWidth * 5, normalKeyHeight, R.color.key_primary);
        configKey(R.id.key_go, wideKeyWidth, normalKeyHeight, R.color.key_tertiary);

        TextView keyH = mActivity.findViewById(R.id.key_H);
        TextView keyL = mActivity.findViewById(R.id.key_L);
        TextView key0 = mActivity.findViewById(R.id.key_0);

        keyH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.moveCursorX(-1);
            }
        });
        keyL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.moveCursorX(1);
            }
        });
        key0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEditor.moveCursorToFirstChar();
                }
            });
    }

    private static void configKey(int viewId, int width, int height, int colorId){
        TextView view = mActivity.findViewById(viewId);
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
        paint.setColor(mActivity.getResources().getColor(colorId));

        canvas.drawRoundRect(rect, keyCornerRadius, keyCornerRadius, paint);

        if(keyTextSize != -1) keyTextSize = calculateTextSizeToFitRect(paint, rect);
        view.setTextSize(keyTextSize);
        
        view.setBackground(new BitmapDrawable(mActivity.getResources(), bitmap));
    }

    public static float calculateTextSizeToFitRect(Paint paint, RectF bounds) {
        float low = 1f, high = 100f, size = low;
        String text = "a";

        while (low <= high) {
            float mid = (low + high) / 2f;
            paint.setTextSize(mid);
            int lines = paint.breakText(text, true, bounds.width(), null);

            if (lines * paint.getFontSpacing() <= bounds.height() && lines > 0) {
                size = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return size;
    }
}
