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
    
    int lineSpacing = -1;
    int lineHeight = -1;

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

        int lastBottom = 0;
        if(lines.isEmpty()) return;
        for (String line : lines) {
            mPaint.getTextBounds(line, 0, line.length(), textBounds);

            // populate only once
            if (lineHeight == -1) {
                lineHeight = textBounds.height();
                lineSpacing = lineHeight / 2;
            }

            int lineTop = lastBottom;
            int lineBottom = lineTop + lineHeight + lineSpacing;

            if (touchY <= lineBottom
            &&  touchY >= lineTop) {
                // highlight touched line
                mPaint.setColor(Color.DKGRAY);
                canvas.drawRect(0, lineTop, getWidth(), lineBottom, mPaint);

                // highlight touched char
                int cumulativeWidth = 0;
                boolean isCharTouched = false;
                for (int i = 0; i < line.length(); i++) {
                    float charWidth = mPaint.measureText(line, i, i + 1);
                    cumulativeWidth += (int) charWidth;

                    if (touchX > cumulativeWidth) continue;
                    
                    cursorRect.left = cumulativeWidth - (int) charWidth;
                    cursorRect.right = cumulativeWidth;
                    isCharTouched = true;
                    break;
                }
                if (!isCharTouched) { // didnt touched any char
                    cursorRect.left = cumulativeWidth;
                    cursorRect.right = cumulativeWidth + (int) mPaint.measureText("a");
                }
                
                cursorRect.top = lineTop;
                cursorRect.bottom = lineBottom;
                
                mPaint.setColor(0xFF888888);
                canvas.drawRect(cursorRect, mPaint);
            }
            
            // draw text
            mPaint.setColor(Color.WHITE);
            canvas.drawText(line, 0, lineBottom - lineSpacing, mPaint);

            // only draw visible lines
            if (lineBottom > getHeight()) break;
            
            // record last lineBottom for the next loop
            lastBottom = lineBottom;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = (int) event.getX();
            touchY = (int) event.getY();
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }
}