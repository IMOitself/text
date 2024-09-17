package imo.text;

public class Line {
    String text;
    int bottom;
    int top;
    
    Line(String text){
        this.text = text;
    }
    
    boolean isTouched(int touchY){
        return touchY <= bottom && touchY >= top;
    }
    
    boolean isNotTouched(int touchY){
        return ! isTouched(touchY);
    }
    
}
