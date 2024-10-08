package imo.text;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    static Activity mActivity;
    static int keyPaddingW, keyPaddingH;
    static int keyCornerRadius;
    static float keyTextSize = -1;
    
    static final int NO_SHIFT = 0, SINGLE_SHIFT = 1, LONG_SHIFT = 2;
    static int shiftMode = NO_SHIFT;

    public static void configKeyboard(Activity activity){
        mActivity = activity;

        ViewGroup keyboard = mActivity.findViewById(R.id.keyboard);

        TextView key1 = mActivity.findViewById(R.id.key_1);
        TextView key2 = mActivity.findViewById(R.id.key_2);
        TextView key3 = mActivity.findViewById(R.id.key_3);
        TextView key4 = mActivity.findViewById(R.id.key_4);
        TextView key5 = mActivity.findViewById(R.id.key_5);
        TextView key6 = mActivity.findViewById(R.id.key_6);
        TextView key7 = mActivity.findViewById(R.id.key_7);
        TextView key8 = mActivity.findViewById(R.id.key_8);
        TextView key9 = mActivity.findViewById(R.id.key_9);
        TextView key0 = mActivity.findViewById(R.id.key_0);

        TextView keyQ = mActivity.findViewById(R.id.key_Q);
        TextView keyW = mActivity.findViewById(R.id.key_W);
        TextView keyE = mActivity.findViewById(R.id.key_E);
        TextView keyR = mActivity.findViewById(R.id.key_R);
        TextView keyT = mActivity.findViewById(R.id.key_T);
        TextView keyY = mActivity.findViewById(R.id.key_Y);
        TextView keyU = mActivity.findViewById(R.id.key_U);
        TextView keyI = mActivity.findViewById(R.id.key_I);
        TextView keyO = mActivity.findViewById(R.id.key_O);
        TextView keyP = mActivity.findViewById(R.id.key_P);
        TextView keyA = mActivity.findViewById(R.id.key_A);
        TextView keyS = mActivity.findViewById(R.id.key_S);
        TextView keyD = mActivity.findViewById(R.id.key_D);
        TextView keyF = mActivity.findViewById(R.id.key_F);
        TextView keyG = mActivity.findViewById(R.id.key_G);
        TextView keyH = mActivity.findViewById(R.id.key_H);
        TextView keyJ = mActivity.findViewById(R.id.key_J);
        TextView keyK = mActivity.findViewById(R.id.key_K);
        TextView keyL = mActivity.findViewById(R.id.key_L);
        TextView keyZ = mActivity.findViewById(R.id.key_Z);
        TextView keyX = mActivity.findViewById(R.id.key_X);
        TextView keyC = mActivity.findViewById(R.id.key_C);
        TextView keyV = mActivity.findViewById(R.id.key_V);
        TextView keyB = mActivity.findViewById(R.id.key_B);
        TextView keyN = mActivity.findViewById(R.id.key_N);
        TextView keyM = mActivity.findViewById(R.id.key_M);

        final TextView key_comma = mActivity.findViewById(R.id.key_comma);
        final TextView key_period = mActivity.findViewById(R.id.key_period);
        final TextView key_shift = mActivity.findViewById(R.id.key_shift);
        final TextView key_backspace = mActivity.findViewById(R.id.key_backspace);
        final TextView key_ctrl = mActivity.findViewById(R.id.key_ctrl);

        final TextView key_space = mActivity.findViewById(R.id.key_space);
        final TextView key_enter = mActivity.findViewById(R.id.key_enter);

        final TextView[] numberSizeKeys = {
                key1, key2, key3, key4, key5,
                key6, key7, key8, key9, key0
        };

        final TextView[] normalSizeKeys = {
                keyQ, keyW, keyE, keyR, keyT, keyY,
                keyU, keyI, keyO, keyP, keyA, keyS,
                keyD, keyF, keyG, keyH, keyJ, keyK,
                keyL, keyZ, keyX, keyC, keyV, keyB,
                keyN, keyM, key_comma, key_period
        };

        final TextView[] wideSizeKeys = {
                key_shift, key_backspace, key_ctrl
        };
        
        // any keys that can be lower or upper case
        final List<TextView> alphabetKeys = new ArrayList<>();
        for(TextView normalSizeKey : normalSizeKeys){
            alphabetKeys.add(normalSizeKey);
        }
        alphabetKeys.add(key_shift);
        alphabetKeys.add(key_backspace);
        alphabetKeys.add(key_ctrl);
        alphabetKeys.add(key_enter);
        

        // Get screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Calculate keyboard height (between 1/3 and 1/4 of screen)
        int keyboardHeight = (screenHeight * 7) / 24;

        // Calculate key dimensions
        final int normalKeyWidth = screenWidth / 10;
        final int normalKeyHeight = keyboardHeight / 4;
        final int numberKeyHeight = normalKeyHeight * 3 / 4;
        final int wideKeyWidth = normalKeyWidth + (normalKeyWidth / 2);

        keyPaddingW = (int) (normalKeyWidth * 0.1f); // 10% width
        keyPaddingH = (int) (normalKeyHeight * 0.1f);// 10% height
        keyCornerRadius = normalKeyWidth / 8;

        // Configure keys
        for (TextView textview : numberSizeKeys)
            configKey(textview, normalKeyWidth, numberKeyHeight, R.color.key_primary);

        for (TextView textview : normalSizeKeys)
            configKey(textview, normalKeyWidth, normalKeyHeight, R.color.key_primary);

        for (TextView textview : wideSizeKeys)
            configKey(textview, wideKeyWidth, normalKeyHeight, R.color.key_secondary);

        configKey(key_space, normalKeyWidth * 5, normalKeyHeight, R.color.key_primary);
        configKey(key_enter, wideKeyWidth, normalKeyHeight, R.color.key_tertiary);

        // disable clicking through the keyboard
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {}
        });

        keyH.setOnTouchListener(continuousClick(new Runnable() {
                    @Override
                    public void run() {
                        VimMotion.moveCursorX(-1);
                    }
                }));
        keyL.setOnTouchListener(continuousClick(new Runnable() {
                    @Override
                    public void run() {
                        VimMotion.moveCursorX(1);
                    }
                }));
        key0.setOnTouchListener(continuousClick(new Runnable() {
                    @Override
                    public void run() {
                        VimMotion.moveCursorToFirstChar();
                    }
                }));
        keyJ.setOnTouchListener(continuousClick(new Runnable() {
                    @Override
                    public void run() {
                        VimMotion.moveCursorY(1);
                    }
                }));
        keyK.setOnTouchListener(continuousClick(new Runnable() {
                    @Override
                    public void run() {
                        VimMotion.moveCursorY(-1);
                    }
                }));
        keyW.setOnTouchListener(continuousClick(new Runnable() {
            @Override
            public void run() {
                VimMotion.moveCursorToNextWord();
            }
        }));
        keyB.setOnTouchListener(continuousClick(new Runnable() {
            @Override
            public void run() {
                VimMotion.moveCursorToPrevWord();
            }
        }));
        keyE.setOnTouchListener(continuousClick(new Runnable() {
            @Override
            public void run() {
                VimMotion.moveCursorToNextWordEnd();
            }
        }));
        key_space.setOnTouchListener(continuousClick(new Runnable(){
                    @Override
                    public void run(){
                        VimMotion.moveCursorX(1);
                    }
                }));
        key_backspace.setOnTouchListener(continuousClick(new Runnable(){
                    @Override
                    public void run(){
                        VimMotion.moveCursorX(-1);
                    }
                }));
        key_enter.setOnTouchListener(continuousClick(new Runnable(){
                    @Override
                    public void run(){
                        VimMotion.moveCursorY(1);
                    }
                }));
        key_shift.setOnTouchListener(continuousClick(new Runnable(){
                    @Override
                    public void run() {
                        shiftMode++;
                        switch(shiftMode){
                            case SINGLE_SHIFT:
                                setAllKeysToUppercase(alphabetKeys);
                                break;
                            case LONG_SHIFT:
                                setAllKeysToUppercase(alphabetKeys);
                                configKey(key_shift, wideKeyWidth, normalKeyHeight, R.color.key_tertiary);
                                break;
                            default:
                                if(shiftMode > LONG_SHIFT) shiftMode = NO_SHIFT;
                                setAllKeysToLowercase(alphabetKeys);
                                configKey(key_shift, wideKeyWidth, normalKeyHeight, R.color.key_secondary);
                                
                        }
                    }
                }));
    }

    private static void configKey(TextView view, int width, int height, int colorId){
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
    
    public static void setAllKeysToUppercase(List<TextView> keys){
        for(TextView key : keys){
            String getText = key.getText().toString();
            key.setText(getText.toUpperCase());
        }
    }
    
    public static void setAllKeysToLowercase(List<TextView> keys){
        for(TextView key : keys){
            String getText = key.getText().toString();
            key.setText(getText.toLowerCase());
        }
    }
    
    public static View.OnTouchListener continuousClick(final Runnable performAction) {
        return new View.OnTouchListener() {
            private final Handler handler = new Handler();
            private static final int INITIAL_DELAY = 500; // milliseconds
            private static final int REPEAT_DELAY = 50; // milliseconds

            private final Runnable actionRunnable = new Runnable() {
                @Override
                public void run() {
                    performAction.run();
                    handler.postDelayed(this, REPEAT_DELAY);
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        performAction.run();
                        handler.postDelayed(actionRunnable, INITIAL_DELAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(actionRunnable);
                        return true;
                }
                return false;
            }
        };
    }
}
