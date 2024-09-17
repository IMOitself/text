package imo.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Editor extends View {
    List<String> lines = new ArrayList<>();

    Paint mPaint;
    Rect textBounds;
    Rect cursorRect;
    
    int touchX = 0;
    int touchY = 0;

    public Editor(Context context) {
        super(context);
        init();
    }

    public Editor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Editor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mPaint = new Paint();
        textBounds = new Rect();
        cursorRect = new Rect();
        
        mPaint.setTextSize(50f);
        mPaint.setColor(Color.WHITE);
    }

    public void setText(String text){
        lines = Arrays.asList(text.split("\n"));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (lines.isEmpty()) return;

        int lastBottom = 0;
        int lineHeight = -1;
        int lineSpacing = 0;

        for (String line : lines) {
            // Measure text bounds
            mPaint.getTextBounds(line, 0, line.length(), textBounds);

            // Initialize line height and spacing (only once)
            if (lineHeight == -1) {
                lineHeight = textBounds.height();
                lineSpacing = lineHeight / 2;
            }

            // Calculate line position
            int lineTop = lastBottom;
            int lineBottom = lineTop + lineHeight + lineSpacing;

            // Check if line is touched
            if (isTouched(lineTop, lineBottom)) {
                highlightTouchedLine(canvas, lineTop, lineBottom);
                drawCursor(canvas, line, lineTop, lineBottom);
            }

            // Draw text
            drawText(canvas, line, lineBottom - lineSpacing);

            // Stop drawing if we're off the bottom of the view
            if (lineBottom > getHeight()) break;

            lastBottom = lineBottom;
        }
    }

    private boolean isTouched(int lineTop, int lineBottom) {
        return touchY <= lineBottom && touchY >= lineTop;
    }

    private void highlightTouchedLine(Canvas canvas, int lineTop, int lineBottom) {
        mPaint.setColor(Color.DKGRAY);
        canvas.drawRect(0, lineTop, getWidth(), lineBottom, mPaint);
    }

    private void drawCursor(Canvas canvas, String line, int lineTop, int lineBottom) {
        int overallWidth = 0;
        boolean isCharTouched = false;

        for (int i = 0; i < line.length(); i++) {
            float charWidth = mPaint.measureText(line, i, i + 1);
            overallWidth += (int) charWidth;

            if (touchX <= overallWidth) {
                cursorRect.left = overallWidth - (int) charWidth;
                cursorRect.right = overallWidth;
                isCharTouched = true;
                break;
            }
        }

        if (!isCharTouched) {
            cursorRect.left = overallWidth;
            cursorRect.right = overallWidth + (int) mPaint.measureText("a");
        }

        cursorRect.top = lineTop;
        cursorRect.bottom = lineBottom;

        mPaint.setColor(0xFF888888);
        canvas.drawRect(cursorRect, mPaint);
    }

    private void drawText(Canvas canvas, String line, int y) {
        mPaint.setColor(Color.WHITE);
        canvas.drawText(line, 0, y, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = (int) event.getX();
            touchY = (int) event.getY();
            invalidate(); // will call onDraw()
            return true;
        }
        return super.onTouchEvent(event);
    }
}