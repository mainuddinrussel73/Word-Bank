package com.example.mainuddin.myapplication34.ui.Quiz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.example.mainuddin.myapplication34.ui.Quiz.Quiz_match.a;
import static com.example.mainuddin.myapplication34.ui.Quiz.Quiz_match.b;
import static com.example.mainuddin.myapplication34.ui.Quiz.Quiz_match.s2;
import static com.example.mainuddin.myapplication34.ui.Quiz.Quiz_match.s3;
import static com.example.mainuddin.myapplication34.ui.Quiz.Quiz_match.words;

public class DrawView extends View {


    private List<Float> position1=new ArrayList<Float>();
    private List<Float> position2=new ArrayList<Float>();;

    List<Paint> paints = new ArrayList<>();

    public DrawView(Context context) {
        super(context);
        invalidate();
        Log.d("drawview","In DrawView class position1:"+position1+" position2:"+position2) ;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("on draw","IN onDraw() position1:"+position1+" position2:"+position2);

        assert position1.size() == position2.size();

        for (int i = 0; i < position1.size(); i += 2) {
            float x1 = position1.get(i);
            float y1 = position1.get(i + 1);
            float x2 = position2.get(i);
            float y2 = position2.get(i + 1);

            Paint paint = new Paint();
            if(words[a].getMEANING().equals(s2[b])){
                paint.setPathEffect(new CornerPathEffect(10));
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(13);
                paints.add(paint);
            }else{
                paint.setPathEffect(new CornerPathEffect(10));
                paint.setColor(Color.RED);
                paint.setStrokeWidth(13);
                paints.add(paint);

            }



            canvas.drawLine(x1,y1, x2,y2, paints.get(i));
        }
    }

    public void addSourcePoint(float x1, float y1) {
        if(!position1.contains(y1) &&  !position1.contains(y1)){
            position1.add(x1);
            position1.add(y1);
        }
    }

    public void addDestinationPoint(float x2, float y2) {
       if(!position2.contains(y2) && !position2.contains(y2)){
         position2.add(x2);
         position2.add(y2);

       }
        invalidate();
    }
}