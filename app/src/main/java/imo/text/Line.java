package imo.text;

import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;

public class Line {
    List<RectF> charRects = new ArrayList<>();
    String text;
    Integer bottom;
    Integer top;

    Line(String text){
        this.text = text;
    }
    
    boolean isTouched(int touchY){
        return touchY <= bottom && touchY >= top;
    }


    
}
