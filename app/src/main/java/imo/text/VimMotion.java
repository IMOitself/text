package imo.text;

import java.util.List;

public class VimMotion {
    
    static Editor mEditor;
    
    static void moveCursorToFirstChar(){
        mEditor.currCharPosition = 0;
        mEditor.invalidate();
    }

    static void moveCursorY(int amount){
        int newLinePosition = amount + mEditor.currLinePosition;

        if(newLinePosition < 0) return;
        if(newLinePosition >= mEditor.Lines.size()) return;

        mEditor.currLinePosition = newLinePosition;

        // prevent overshoot if the previous line is longer than the new
        Line line = mEditor.Lines.get(newLinePosition);
        if(mEditor.currCharPosition >= line.charRects.size())
           mEditor.currCharPosition = line.charRects.size() - 1;
        mEditor.invalidate();
    }

    static void moveCursorX(int amount){
        int newCharPosition = amount + mEditor.currCharPosition;

        if(newCharPosition < 0) {
            int prevLinePosition = mEditor.currLinePosition - 1;
            if(prevLinePosition < 0) return;

            mEditor.currLinePosition = prevLinePosition;
            newCharPosition = mEditor.Lines.get(mEditor.currLinePosition).charRects.size() - 1;
        }
        if(newCharPosition >= mEditor.Lines.get(mEditor.currLinePosition).charRects.size()) {
            int nextLinePosition = mEditor.currLinePosition + 1;
            if(nextLinePosition >= mEditor.Lines.size()) return;

            mEditor.currLinePosition = nextLinePosition;
            newCharPosition = 0;
        }

        mEditor.currCharPosition = newCharPosition;
        mEditor.invalidate();
    }

    static void moveCursorToNextWord(){
        Line currLine = mEditor.Lines.get(mEditor.currLinePosition);

        int nextWordIndex = mEditor.currWordIndex + 1;
        if(nextWordIndex >= currLine.wordList.size()) { // over last word of the line
            int nextLinePosition = mEditor.currLinePosition + 1;
            if(nextLinePosition >= mEditor.Lines.size()) return;

            currLine = mEditor.Lines.get(nextLinePosition);
            nextWordIndex = 0;
            mEditor.currLinePosition = nextLinePosition;
        }

        List<Integer> nextWord = currLine.wordList.get(nextWordIndex);
        mEditor.currCharPosition = nextWord.get(0);
        mEditor.invalidate();
    }

    static void moveCursorToPrevWord(){
        Line currLine = mEditor.Lines.get(mEditor.currLinePosition);

        // if the cursor is still in the current word but not at the first char
        List<Integer> currWord = currLine.wordList.get(mEditor.currWordIndex);

        if(mEditor.currCharPosition > currWord.get(0)){
            mEditor.currCharPosition = currWord.get(0);
            mEditor.invalidate();
            return;
        }

        // only go to previous word if the cursor is at first char of current word
        int prevWordIndex = mEditor.currWordIndex - 1;
        if(prevWordIndex < 0) { // can't go backward any further
            int prevLinePosition = mEditor.currLinePosition - 1;
            if(prevLinePosition < 0) return;

            currLine = mEditor.Lines.get(prevLinePosition);
            prevWordIndex = currLine.wordList.size() - 1;
            mEditor.currLinePosition = prevLinePosition;
        }

        List<Integer> prevWord = currLine.wordList.get(prevWordIndex);
        mEditor.currCharPosition = prevWord.get(0);
        mEditor.invalidate();
    }

    static void moveCursorToNextWordEnd(){
        Line currLine = mEditor.Lines.get(mEditor.currLinePosition);

        // if the cursor is still in the current word but not at the last char
        List<Integer> currWord = currLine.wordList.get(mEditor.currWordIndex);
        int currLastChar = currWord.size() - 1;

        if(mEditor.currCharPosition < currWord.get(currLastChar)){
            mEditor.currCharPosition = currWord.get(currLastChar);
            mEditor.invalidate();
            return;
        }

        int nextWordIndex = mEditor.currWordIndex + 1;
        if(nextWordIndex >= currLine.wordList.size()) { // over last word of the line
            int nextLinePosition = mEditor.currLinePosition + 1;
            if(nextLinePosition >= mEditor.Lines.size()) return;

            currLine = mEditor.Lines.get(nextLinePosition);
            nextWordIndex = 0;
            mEditor.currLinePosition = nextLinePosition;

        }

        List<Integer> nextWord = currLine.wordList.get(nextWordIndex);
        int nextLastChar = nextWord.size() - 1;
        mEditor.currCharPosition = nextWord.get(nextLastChar);
        mEditor.invalidate();
    }
    
}
