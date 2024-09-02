package imo.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init(){
        LinearLayout layout = new LinearLayout(this);
        layout.addView(new Editor(this, generateText()));
        setContentView(layout);
    }

    String generateText(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 100; i++){
            int randomInt = random.nextInt(10);
            for(int j = 0; j < randomInt; j++){
                sb.append("□");
            }
            sb.append("\n");
        }
        return sb.toString();
    }







    static class Editor extends View {
        List<String> lines;

        RectF rect;
        Paint mPaint;
        Rect textBounds;

        float lineSpacing;
        final float LINE_HEIGHT;

        public Editor(Context context, String text) {
            super(context);
            rect = new RectF();
            mPaint = new Paint();
            textBounds = new Rect();

            mPaint.setTextSize(50f);
            mPaint.setColor(Color.WHITE);

            lines = Arrays.asList(text.split("\n"));

            String dummy = lines.get(0);
            mPaint.getTextBounds(dummy, 0, dummy.length(), textBounds);
            LINE_HEIGHT = textBounds.height();
            lineSpacing = LINE_HEIGHT / 2f;
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);

            float lastBottom = 0;
            for(String line : lines){
                mPaint.getTextBounds(line, 0, line.length(), textBounds);

                rect.left = 0;
                rect.top = (int) (lastBottom + lineSpacing);
                rect.right = textBounds.width();
                rect.bottom = rect.top + textBounds.height();

                if(rect.top >= getHeight()) break;

                canvas.drawRect(rect, mPaint);
                canvas.drawText(line, rect.right, rect.top + -textBounds.top, mPaint);

                lastBottom = rect.bottom;
            }
        }
    }
}