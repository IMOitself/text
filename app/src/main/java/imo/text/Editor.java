package imo.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class Editor extends View {
    List<Line> Lines = new ArrayList<>();
    int currLinePosition = 0;
    int currCharPosition = 0;
    
    Paint mPaint;
    Rect textBounds;
    RectF charCursor;
    
    int touchX = 0;
    int touchY = 0;
    
    int lineHeight = -1;
    int lineSpacing = 0;

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
        charCursor = new RectF();
        
        mPaint.setTextSize(50f);
        mPaint.setColor(Color.WHITE);
    }

    public void setText(String text){
        for(String textLine : text.split("\n")){
            Lines.add(new Line(textLine));
        }
        invalidate(); // will call onDraw()
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Lines.isEmpty()) return;

        int lastBottom = 0;
        int lineIndex = 0;
        int charIndex = 0;
        
        for (Line line : Lines) {
            String lineText = line.text;
            int cumulativeWidth = 0;

            // measure text bounds
            mPaint.getTextBounds(lineText, 0, lineText.length(), textBounds);
            
            // initialize line height and spacing (only once)
            if (lineHeight == -1) {
                lineHeight = textBounds.height();
                lineSpacing = lineHeight / 2;
            }

            line.top = lastBottom;
            line.bottom = line.top + lineHeight + lineSpacing;
            
            // finish early
            if(lineText.isEmpty()) {
                lastBottom = line.bottom;
                continue;
            }

            // get each char bounds
            for (int i = 0; i < lineText.length(); i++) {
                float charWidth = mPaint.measureText(lineText, i, i + 1);
                cumulativeWidth += (int) charWidth;
                
                RectF charRect = new RectF();
                charRect.top = line.top;
                charRect.bottom = line.bottom;
                charRect.left = cumulativeWidth - charWidth;
                charRect.right = cumulativeWidth;
                line.charRects.add(charRect);
            }
            
            // highlight the line and char
            if(line.isTouched(touchY)){
                mPaint.setColor(Color.DKGRAY);
                canvas.drawRect(0, line.top, getWidth(), line.bottom, mPaint);
                
                boolean hasTouchChar = false;
                
                for(RectF charRect : line.charRects){
                    if(charRect.contains(touchX, touchY)){
                        hasTouchChar = true;
                        charCursor = charRect;
                        break;
                    }
                    charIndex++;
                }
                // draw cursor at the last char
                if(! hasTouchChar) charCursor = line.charRects.get(line.charRects.size() - 1);
                
                mPaint.setColor(0xFF888888);
                canvas.drawRect(charCursor, mPaint);
                
                currLinePosition = lineIndex;
                currCharPosition = charIndex;
                lineIndex++;
            }

            // Draw text
            mPaint.setColor(Color.WHITE);
            canvas.drawText(lineText, 0, line.bottom - lineSpacing, mPaint);

            // Stop drawing if we're off the bottom of the view
            if (line.bottom > getHeight()) break;
            lastBottom = line.bottom;
        }
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
    
    void moveCursorX(int amount){
        
    }
}
