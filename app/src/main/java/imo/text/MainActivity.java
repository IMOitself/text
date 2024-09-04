package imo.text;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import java.util.Arrays;
import java.util.List;
import android.widget.LinearLayout;
import android.view.MotionEvent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        StringBuilder sb = new StringBuilder();
        sb.append("america ya!");
        for (int i = 0; i < 99; i++) {
            sb.append("\nhallo:D");
        }
        root.addView(new Editor(this, sb.toString()));
        setContentView(root);
    }

    static class Editor extends View {
        List<String> lines;

        Paint mPaint;
        Rect textBounds;
        Rect cursorRect;

        int lineSpacing = -1;
        int lineHeight = -1;

        int touchX = 0;
        int touchY = 0;

        public Editor(Context context, String text) {
            super(context);
            mPaint = new Paint();
            textBounds = new Rect();
            cursorRect = new Rect();

            mPaint.setTextSize(50f);
            mPaint.setColor(Color.WHITE);

            lines = Arrays.asList(text.split("\n"));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int lastBottom = 0;
            for (String line : lines) {
                mPaint.getTextBounds(line, 0, line.length(), textBounds);

                if (lineHeight == -1) {
                    // Populate only once
                    lineHeight = textBounds.height();
                    lineSpacing = lineHeight / 2;
                }

                int lineTop = lastBottom;
                int lineBottom = lineTop + lineHeight + lineSpacing;

                if (touchY >= lineTop &&
                        touchY <= lineBottom) {
                    mPaint.setColor(Color.DKGRAY);
                    canvas.drawRect(0, lineTop, getWidth(), lineBottom, mPaint);

                    int cumulativeWidth = 0;
                    for (int i = 0; i < line.length(); i++) {
                        float charWidth = mPaint.measureText(line, i, i + 1);
                        cumulativeWidth += charWidth;

                        if (touchX > cumulativeWidth) continue;
                        Rect charRect = cursorRect;
                        mPaint.getTextBounds(line, i, i + 1, charRect);
                        charRect.left = cumulativeWidth - (int) charWidth;
                        charRect.right = cumulativeWidth;
                        charRect.top = lineTop;
                        charRect.bottom = lineBottom;

                        mPaint.setColor(0xFF888888);
                        canvas.drawRect(charRect, mPaint);
                        break;
                    }
                }

                mPaint.setColor(Color.WHITE);
                canvas.drawText(line, 0, lineBottom - lineSpacing, mPaint);

                lastBottom = lineBottom;
                if (lastBottom > getHeight()) break; //only draw visible lines
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = (int)event.getX();
                    touchY = (int)event.getY();
                    invalidate();
                    return true;

                default:
                    return super.onTouchEvent(event);
            }
        }
    }
}