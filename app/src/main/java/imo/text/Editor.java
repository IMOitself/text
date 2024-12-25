package imo.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class Editor extends View {
    List<Line> Lines = new ArrayList<>();
    int currLineIndex = 0;
    int currCharIndex = 0;
    int currWordIndex = 0;
    
    Paint mPaint;
    Rect textBounds;
    RectF charCursor;
    
    int lineHeight = -1;
    int lineSpacing = 0;
	
	long touchDownTime = 0;
	boolean isLongClick = false;
	Handler longClickHandler;

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
		
		longClickHandler = new Handler(Looper.getMainLooper());
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
        
        Line currLine = Lines.get(currLineIndex);
        RectF currCharRect = currLine.charRects.get(currCharIndex);
        
        charCursor = currCharRect;
        
        // find the current word by current char index
        int wordIndex = 0;
        boolean hasFoundCurrWord = false;
		
        for(List<Integer> word : currLine.wordList){
            for(int charIndex : word){
                if(charIndex == currCharIndex){
                    currWordIndex = wordIndex;
                    hasFoundCurrWord = true;
                    break;
                }
            }
            wordIndex++;
        }
        
        // highlights line
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, currLine.top, getWidth(), currLine.bottom, mPaint);
        
        // highlights char
        mPaint.setColor(0xFF888888);
        canvas.drawRect(charCursor, mPaint);
        
        // highlights word
        mPaint.setColor(0xFF888888);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
		if(isLongClick){
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL);
		}
        
		RectF currWordRect = null;
		boolean isCurrWordOneLetter = false;
		
        if(hasFoundCurrWord){
			List<Integer> currWordCharIndexes = currLine.wordList.get(currWordIndex);
			isCurrWordOneLetter = currWordCharIndexes.size() == 1;
            int lastCharIndexOfWord = currWordCharIndexes.size() - 1;
			
			currWordRect = new RectF();
            currWordRect.left = currLine.charRects.get(currWordCharIndexes.get(0)).left;
            currWordRect.right = currLine.charRects.get(currWordCharIndexes.get(lastCharIndexOfWord)).right;
            currWordRect.top = currLine.top;
            currWordRect.bottom = currLine.bottom;
            canvas.drawRect(currWordRect, mPaint);
        }
		
		// draw selection handle
		float handleSize = currCharRect.height() / 2;
		
		if ((!hasFoundCurrWord || isCurrWordOneLetter) && isLongClick){
			RectF singleHandle = new RectF();
			singleHandle.top = currCharRect.bottom;
			singleHandle.bottom = singleHandle.top + handleSize;
			singleHandle.left = currCharRect.left - (handleSize / 2);
			singleHandle.right = currCharRect.left + (handleSize / 2);
			canvas.drawRect(singleHandle, mPaint);
		}
		else 
		if (hasFoundCurrWord && isLongClick) {
			RectF leftHandle = new RectF();
			RectF rightHandle = new RectF();

			leftHandle.top = currWordRect.bottom;
			leftHandle.bottom = leftHandle.top + handleSize;
			leftHandle.left = currWordRect.left - handleSize;
			leftHandle.right = currWordRect.left;

			rightHandle.top = currWordRect.bottom;
			rightHandle.bottom = leftHandle.top + handleSize;
			rightHandle.left = currWordRect.right;
			rightHandle.right = currWordRect.right + handleSize;
			
			if (leftHandle.left <= 0) {
				leftHandle.left = 0;
				leftHandle.right = handleSize;
			}
			if (rightHandle.right >= getWidth()){
				rightHandle.left = getWidth() - handleSize;
				rightHandle.right = getWidth();
			}

			canvas.drawRect(leftHandle, mPaint);
			canvas.drawRect(rightHandle, mPaint);
		}
		
        // draw text
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        drawTexts(canvas, Lines, lineSpacing, mPaint);
		
		if(isLongClick) isLongClick = false;
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		// long click logic
		if (MotionEvent.ACTION_DOWN == event.getAction())
			longClickHandler.postDelayed(onLongClick, 250);
		if (MotionEvent.ACTION_UP == event.getAction() || 
			MotionEvent.ACTION_CANCEL == event.getAction())
            longClickHandler.removeCallbacks(onLongClick);
		
        if (MotionEvent.ACTION_DOWN != event.getAction()) return super.onTouchEvent(event);

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        int lineIndex = -1;
        int charIndex = -1;

        // find touched line
        for(Line line : Lines){
            lineIndex++;
            if(! line.isTouched(touchY)) continue;
            currLineIndex = lineIndex;

            // find touched char
            boolean hasTouchAnyChar = false;
            for(RectF charRect : line.charRects){
                charIndex++;
                if(! charRect.contains(touchX, touchY)) continue;
                currCharIndex = charIndex;
                hasTouchAnyChar = true;
                break;
            }

            // if didn't touched any, just select the last char
            if(! hasTouchAnyChar) currCharIndex = charIndex;

            break;
        }
        invalidate();
        return true;
    }
    
	private final Runnable onLongClick = new Runnable() {
        @Override
        public void run() {
			isLongClick = true;
            invalidate();
        }
    };
    
    
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
            List<Integer> charIndexesOfWord = new ArrayList<>();

            for (int i = 0; i < line.text.length(); i++) {
                float charWidth = mPaint.measureText(line.text, i, i + 1);
                cumulativeWidth += (int) charWidth;

                RectF charRect = new RectF();
                charRect.top = line.top;
                charRect.bottom = line.bottom;
                charRect.left = cumulativeWidth - charWidth;
                charRect.right = cumulativeWidth;
                line.charRects.add(charRect);

                charIndexesOfWord.add(i);

                if(Character.isWhitespace(chars[i]) || i == line.text.length() - 1){

                    // remove the whitespace char in the end of the word
                    if(Character.isWhitespace(chars[charIndexesOfWord.get(charIndexesOfWord.size() - 1)]))
                        charIndexesOfWord.remove(charIndexesOfWord.size() - 1);

                    line.wordList.add(new ArrayList<>(charIndexesOfWord));
                    charIndexesOfWord.clear();
                }
            }
        }
    }

    void drawTexts(Canvas canvas, List<Line> Lines, int lineSpacing, Paint mPaint){
        for(Line line : Lines){
            
            canvas.drawText(line.text, 0, line.bottom - lineSpacing, mPaint);

            // Stop drawing if we're off the bottom of the view
            if (line.bottom > getHeight()) break;
        }
    }
}
