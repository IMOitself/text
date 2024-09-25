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
        long initialTimeMillis = System.currentTimeMillis(); // DEBUGGING

        if (Lines.isEmpty()) return;

        Line dummyLine = Lines.get(0);
        if(dummyLine.top == null ||
           dummyLine.bottom == null)
           initLines(Lines);
        
        Line currLine = Lines.get(currLinePosition);
        
        int lastCharPosition = currLine.charRects.size() - 1;
        if(currCharPosition > lastCharPosition) currCharPosition = lastCharPosition;
        
        RectF currCharRect = currLine.charRects.get(currCharPosition);
        
        charCursor = currCharRect;
        
        mPaint.setColor(Color.DKGRAY);
        canvas.drawRect(0, currLine.top, getWidth(), currLine.bottom, mPaint);
        
        mPaint.setColor(0xFF888888);
        canvas.drawRect(charCursor, mPaint);
        
        drawTexts(canvas, Lines, lineSpacing);
        
        // DEBUGGING
        long currTimeMillis = System.currentTimeMillis();
        long drawnTimeMillis = currTimeMillis - initialTimeMillis;
        canvas.drawText("drawn: " + drawnTimeMillis + " ms", canvas.getWidth() / 3, canvas.getHeight() / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();
            
            int lineIndex = -1;
            int charIndex = -1;
            
            // find touched line
            for(Line line : Lines){
                lineIndex++;
                if(! line.isTouched(touchY)) continue;
                currLinePosition = lineIndex;
                
                // find touched char
                boolean hasTouchAnyChar = false;
                for(RectF charRect : line.charRects){
                    charIndex++;
                    if(! charRect.contains(touchX, touchY)) continue;
                    currCharPosition = charIndex;
                    hasTouchAnyChar = true;
                    break;
                }
                
                // if didnt touched any, just select the last char
                if(! hasTouchAnyChar){
                    int lastCharPosition = line.charRects.size() - 1;
                    currCharPosition = lastCharPosition;
                } 
                break;
            }
            invalidate(); // will call onDraw()
            return true;
        }
        return super.onTouchEvent(event);
    }
    
    
    void moveCursorX(int amount){
        Line currLine = Lines.get(currLinePosition);
        int newCharPosition = amount + currCharPosition;
        
        if(newCharPosition < 0) return;
        if(newCharPosition >= currLine.charRects.size()) return;
        
        currCharPosition = newCharPosition;
        invalidate();
    }
    
    void moveCursorToFirstChar(){
        currCharPosition = 0;
        invalidate();
    }
    
    void moveCursorY(int amount){
        int newCharPosition = amount + currLinePosition;
        
        if(newCharPosition < 0) return;
        
        currLinePosition = newCharPosition;
        invalidate();
    }
    
    
    
    
    
    
    void initLines(List<Line> Lines){
        // populate each line's variables except the 'text'
        // cos its already been set on setText()
        int lastBottom = 0;
        for(Line line : Lines){
            int cumulativeWidth = 0;

            // populate text bounds
            mPaint.getTextBounds(line.text, 0, line.text.length(), textBounds);

            // initialize line height and spacing (only once)
            if (lineHeight == -1) {
                lineHeight = textBounds.height();
                lineSpacing = lineHeight / 2;
            }

            line.top = lastBottom;
            line.bottom = line.top + lineHeight + lineSpacing;
            lastBottom = line.bottom;

            // get each char bounds as RectF
            for (int i = 0; i < line.text.length(); i++) {
                float charWidth = mPaint.measureText(line.text, i, i + 1);
                cumulativeWidth += (int) charWidth;

                RectF charRect = new RectF();
                charRect.top = line.top;
                charRect.bottom = line.bottom;
                charRect.left = cumulativeWidth - charWidth;
                charRect.right = cumulativeWidth;
                line.charRects.add(charRect);
            }
        }
    }
    
    void drawTexts(Canvas canvas, List<Line> Lines, int lineSpacing){
        for(Line line : Lines){
            mPaint.setColor(Color.WHITE);
            canvas.drawText(line.text, 0, line.bottom - lineSpacing, mPaint);

            // Stop drawing if we're off the bottom of the view
            if (line.bottom > getHeight()) break;
        }
    }
}
