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
    int currWordIndex = 0;
    
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
        
        mPaint.setTextSize(40f);
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

        Line dummyLine = Lines.get(0);
        if(dummyLine.top == null ||
           dummyLine.bottom == null)
           initLines(Lines);
        
        Line currLine = Lines.get(currLinePosition);
        RectF currCharRect = currLine.charRects.get(currCharPosition);
        
        charCursor = currCharRect;
        
        // find the current word by current char position
        int wordIndex = 0;
        for(List<Integer> word : currLine.wordList){
            for(int charPosition : word){
                if(charPosition == currCharPosition){
                    currWordIndex = wordIndex;
                    break;
                }
            }
            wordIndex++;
        }
        
        mPaint.setColor(Color.DKGRAY);
        canvas.drawRect(0, currLine.top, getWidth(), currLine.bottom, mPaint);
        
        mPaint.setColor(0xFF888888);
        canvas.drawRect(charCursor, mPaint);
        
        drawTexts(canvas, Lines, lineSpacing);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event);

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

            // if didn't touched any, just select the last char
            if(! hasTouchAnyChar) currCharPosition = charIndex;

            break;
        }
        invalidate();
        return true;
    }
    
    void moveCursorX(int amount){
        int newCharPosition = amount + currCharPosition;
        
        if(newCharPosition < 0) return;
        if(newCharPosition >= Lines.get(currLinePosition).charRects.size()) return;
        
        currCharPosition = newCharPosition;
        invalidate();
    }
    
    void moveCursorToFirstChar(){
        currCharPosition = 0;
        invalidate();
    }
    
    void moveCursorToNextWordStart(){
        Line currLine = Lines.get(currLinePosition);
        int nextWordIndex = currWordIndex + 1;
        if(nextWordIndex > currLine.wordList.size() - 1) return; // over last word

        List<Integer> nextWord = currLine.wordList.get(nextWordIndex);
        currCharPosition = nextWord.get(0);
        invalidate();
    }

    void moveCursorToPrevWordStart(){
        Line currLine = Lines.get(currLinePosition);

        // if the cursor is still in the current word but not at the first char
        List<Integer> currWord = currLine.wordList.get(currWordIndex);

        if(currCharPosition > currWord.get(0)){
            currCharPosition = currWord.get(0);
            invalidate();
            return;
        }

        // only go to previous word if the cursor is at first char of current word
        int prevWordIndex = currWordIndex - 1;
        if(prevWordIndex < 0) return;

        List<Integer> prevWord = currLine.wordList.get(prevWordIndex);
        currCharPosition = prevWord.get(0);
        invalidate();
    }

    void moveCursorToNextWordEnd(){
        Line currLine = Lines.get(currLinePosition);

        // if the cursor is still in the current word but not at the last char
        List<Integer> currWord = currLine.wordList.get(currWordIndex);
        int currLastChar = currWord.size() - 1;

        if(currCharPosition < currWord.get(currLastChar)){
            currCharPosition = currWord.get(currLastChar);
            invalidate();
            return;
        }

        int nextWordIndex = currWordIndex + 1;
        if(nextWordIndex > currLine.wordList.size() - 1) return; // over last word

        List<Integer> nextWord = currLine.wordList.get(nextWordIndex);
        int nextLastChar = nextWord.size() - 1;
        currCharPosition = nextWord.get(nextLastChar);
        invalidate();
    }

    void moveCursorY(int amount){
        int newLinePosition = amount + currLinePosition;
        
        if(newLinePosition < 0) return;
        
        currLinePosition = newLinePosition;
        
        // prevent overshoot if the previous line is longer than the new
        Line line = Lines.get(newLinePosition);
        if(currCharPosition >= line.charRects.size())
            currCharPosition = line.charRects.size() - 1;
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
            lastBottom = line.bottom; // NOTE: don't use lastBottom below, its already changed

            // get each char bounds as RectF
            char[] chars = line.text.toCharArray();
            List<Integer> charPositionsOfWord = new ArrayList<>();

            for (int i = 0; i < line.text.length(); i++) {
                float charWidth = mPaint.measureText(line.text, i, i + 1);
                cumulativeWidth += (int) charWidth;

                RectF charRect = new RectF();
                charRect.top = line.top;
                charRect.bottom = line.bottom;
                charRect.left = cumulativeWidth - charWidth;
                charRect.right = cumulativeWidth;
                line.charRects.add(charRect);

                charPositionsOfWord.add(i);

                if(Character.isWhitespace(chars[i]) || i == line.text.length() - 1){

                    // remove the whitespace char in the end of the word
                    if(Character.isWhitespace(chars[charPositionsOfWord.get(charPositionsOfWord.size() - 1)]))
                        charPositionsOfWord.remove(charPositionsOfWord.size() - 1);

                    line.wordList.add(new ArrayList<>(charPositionsOfWord));
                    charPositionsOfWord.clear();
                }
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
