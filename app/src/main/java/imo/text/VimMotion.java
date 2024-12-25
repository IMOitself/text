package imo.text;

import java.util.List;

public class VimMotion {
    
    static Editor mEditor;
    
    static void moveCursorToFirstChar(){
        mEditor.currCharIndex = 0;
        mEditor.invalidate();
    }

    static void moveCursorY(int amount){
        int newLineIndex = amount + mEditor.currLineIndex;

        if(newLineIndex < 0) return;
        if(newLineIndex >= mEditor.Lines.size()) return;

        mEditor.currLineIndex = newLineIndex;

        // prevent overshoot if the previous line is longer than the new
        Line line = mEditor.Lines.get(newLineIndex);
        if(mEditor.currCharIndex >= line.charRects.size())
           mEditor.currCharIndex = line.charRects.size() - 1;
        mEditor.invalidate();
    }

    static void moveCursorX(int amount){
        int newCharIndex = amount + mEditor.currCharIndex;

        if(newCharIndex < 0) {
            int prevLineIndex = mEditor.currLineIndex - 1;
            if(prevLineIndex < 0) return;

            mEditor.currLineIndex = prevLineIndex;
            newCharIndex = mEditor.Lines.get(mEditor.currLineIndex).charRects.size() - 1;
        }
        if(newCharIndex >= mEditor.Lines.get(mEditor.currLineIndex).charRects.size()) {
            int nextLineIndex = mEditor.currLineIndex + 1;
            if(nextLineIndex >= mEditor.Lines.size()) return;

            mEditor.currLineIndex = nextLineIndex;
            newCharIndex = 0;
        }

        mEditor.currCharIndex = newCharIndex;
        mEditor.invalidate();
    }

    static void moveCursorToNextWord(){
        Line currLine = mEditor.Lines.get(mEditor.currLineIndex);

        int nextWordIndex = mEditor.currWordIndex + 1;
        if(nextWordIndex >= currLine.wordList.size()) { // over last word of the line
            int nextLineIndex = mEditor.currLineIndex + 1;
            if(nextLineIndex >= mEditor.Lines.size()) return;

            currLine = mEditor.Lines.get(nextLineIndex);
            nextWordIndex = 0;
            mEditor.currLineIndex = nextLineIndex;
        }

        List<Integer> nextWord = currLine.wordList.get(nextWordIndex);
        mEditor.currCharIndex = nextWord.get(0);
        mEditor.invalidate();
    }

    static void moveCursorToPrevWord(){
        Line currLine = mEditor.Lines.get(mEditor.currLineIndex);

        // if the cursor is still in the current word but not at the first char
        List<Integer> currWord = currLine.wordList.get(mEditor.currWordIndex);

        if(mEditor.currCharIndex > currWord.get(0)){
            mEditor.currCharIndex = currWord.get(0);
            mEditor.invalidate();
            return;
        }

        // only go to previous word if the cursor is at first char of current word
        int prevWordIndex = mEditor.currWordIndex - 1;
        if(prevWordIndex < 0) { // can't go backward any further
            int prevLineIndex = mEditor.currLineIndex - 1;
            if(prevLineIndex < 0) return;

            currLine = mEditor.Lines.get(prevLineIndex);
            prevWordIndex = currLine.wordList.size() - 1;
            mEditor.currLineIndex = prevLineIndex;
        }

        List<Integer> prevWord = currLine.wordList.get(prevWordIndex);
        mEditor.currCharIndex = prevWord.get(0);
        mEditor.invalidate();
    }

    static void moveCursorToNextWordEnd(){
        Line currLine = mEditor.Lines.get(mEditor.currLineIndex);

        // if the cursor is still in the current word but not at the last char
        List<Integer> currWord = currLine.wordList.get(mEditor.currWordIndex);
        int currLastChar = currWord.size() - 1;

        if(mEditor.currCharIndex < currWord.get(currLastChar)){
            mEditor.currCharIndex = currWord.get(currLastChar);
            mEditor.invalidate();
            return;
        }

        int nextWordIndex = mEditor.currWordIndex + 1;
        if(nextWordIndex >= currLine.wordList.size()) { // over last word of the line
            int nextLineIndex = mEditor.currLineIndex + 1;
            if(nextLineIndex >= mEditor.Lines.size()) return;

            currLine = mEditor.Lines.get(nextLineIndex);
            nextWordIndex = 0;
            mEditor.currLineIndex = nextLineIndex;

        }

        List<Integer> nextWord = currLine.wordList.get(nextWordIndex);
        int nextLastChar = nextWord.size() - 1;
        mEditor.currCharIndex = nextWord.get(nextLastChar);
        mEditor.invalidate();
    }
    
}
