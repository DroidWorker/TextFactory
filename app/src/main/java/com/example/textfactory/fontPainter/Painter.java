package com.example.textfactory.fontPainter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Painter extends View {
    Context ctx = null;
    Canvas canvas = null;
    Paint paint;
    Path path;
    String Spoint="";
    String Epoint="";

    HashMap<String, String> actionMap = new HashMap<>();//StartPoint, lineTo(x|y)
    private Stack<String[]> map = new Stack<>();

    public Painter(Context ctx)
    {
        super(ctx);
        this.ctx = ctx;
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
    }
    
    void update()
    {
        path.reset();
        for (Map.Entry<String, String> entry : actionMap.entrySet()) {
            float xPos = Float.valueOf(((entry.getKey()).split(":"))[0]);
            float yPos = Float.valueOf(((entry.getKey()).split(":"))[1]);
            path.moveTo(xPos, yPos);
            xPos = Float.valueOf(((entry.getValue()).split(":"))[0]);
            yPos = Float.valueOf(((entry.getValue()).split(":"))[1]);
            path.lineTo(xPos,yPos);
        }
        invalidate();
    }

    public void cancel()
    {
        if (map.size()==0) return;
        String[] key = map.pop();
        actionMap.remove(key[0]+":"+key[1]);
        update();
    }

    public void setImage(HashMap<String, String> imageCoords)
    {
        actionMap = imageCoords;
        update();
    }

    public void clearData()
    {
        actionMap = new HashMap<>();
        update();
    }

    public HashMap<String, String> getImageCoords()//returns coordinates of straightlines
    {
        return actionMap;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawPath(path, paint);
    }

    @Override
    public  boolean onTouchEvent(MotionEvent motionEvent){
        float xPos = motionEvent.getX();
        float yPos = motionEvent.getY();
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos, yPos);
                Spoint=xPos+":"+yPos;
                map.push(new String[]{String.valueOf(xPos),String.valueOf(yPos)});
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                Epoint=xPos+":"+yPos;
                if (!Spoint.equals("")&&!Epoint.equals("")) {
                    actionMap.put(Spoint, Epoint);
                }
                update();
                Spoint="";
                Epoint="";
                break;
            default: return false;
        }
        invalidate();
        return true;
    }
}
