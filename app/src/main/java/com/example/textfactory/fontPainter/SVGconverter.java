package com.example.textfactory.fontPainter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SVGconverter {
    Context ctx;

    public SVGconverter(Context ctx)
    {
        this.ctx =  ctx;
    }

    public String convert(HashMap<String, String> coordMap, int brushSize, int screenWidth)//return svgPath
    {
        String res = "";
        float scalecoeff = 1;
        scalecoeff = screenWidth/300;
        for (Map.Entry<String, String> entry : coordMap.entrySet()
             ) {
                String svgCoords = "";
                float x1, x2, y1, y2;
                float mx, my;
                String[] xy1 = entry.getKey().split(":");
                String[] xy2 = entry.getValue().split(":");
                x1 = Float.valueOf(xy1[0]);
                x2 = Float.valueOf(xy2[0]);
                y1 = Float.valueOf(xy1[1]);
                y2 = Float.valueOf(xy2[1]);
                float dx = Math.abs(x1-x2);
                float dy = Math.abs(y1-y2);
                float l = (float) Math.sqrt(Math.pow(dx, 2)+Math.pow(dy,2));
                //calculate movement Ox
                if (dx==0)
                    mx=brushSize/2;
                else if (dx==l)
                    mx=0;
                else {
                        //float koeff = 100-((dx*100)/l);
                        float koeff = (dy * 100) / (dx + dy);
                        mx = (brushSize * koeff) / 200;
                }
                //calculate movement Oy
                if (dy==0)
                    my=brushSize/2;
                else if (dy==l)
                    my=0;
                else {
                        //float koeff = 100-((dy*100)/l);
                        float koeff=(dx*100)/(dx+dy);
                        my=(brushSize*koeff)/200;
                }
                //mx = Math.abs(mx);
                //my = Math.abs(my);
                //calculate svg coords
                float svgX1, svgY1, svgX2, svgY2, svgX3, svgY3, svgX4, svgY4;
                if (x1<x2&&y1<y2){
                    svgX1 = (x1-mx)/scalecoeff;
                    svgX2 = (x1+mx)/scalecoeff;
                    svgX3 = (x2-mx)/scalecoeff;
                    svgX4 = (x2+mx)/scalecoeff;
                    svgY1 = (y1+my)/scalecoeff;
                    svgY2 = (y1-my)/scalecoeff;
                    svgY3 = (y2+my)/scalecoeff;
                    svgY4 = (y2-my)/scalecoeff;
                }
                else if (x1>x2&&y1>y2){
                    svgX1 = (x1+mx)/scalecoeff;
                    svgX2 = (x1-mx)/scalecoeff;
                    svgX3 = (x2+mx)/scalecoeff;
                    svgX4 = (x2-mx)/scalecoeff;
                    svgY1 = (y1-my)/scalecoeff;
                    svgY2 = (y1+my)/scalecoeff;
                    svgY3 = (y2-my)/scalecoeff;
                    svgY4 = (y2+my)/scalecoeff;
                }
                else {
                    svgX1 = (x1+mx)/scalecoeff;
                    svgX2 = (x1-mx)/scalecoeff;
                    svgX3 = (x2+mx)/scalecoeff;
                    svgX4 = (x2-mx)/scalecoeff;
                    svgY1 = (y1+my)/scalecoeff;
                    svgY2 = (y1-my)/scalecoeff;
                    svgY3 = (y2+my)/scalecoeff;
                    svgY4 = (y2-my)/scalecoeff;
                }
                svgCoords = "M "+svgX1+" "+svgY1+" L "+svgX2+" "+svgY2+" L "+svgX4+" "+svgY4+" L "+svgX3+" "+svgY3+" L "+svgX1+" "+svgY1+" ";
                res+=svgCoords;
        }
        Log.i("result Path", res);
        return res;
    }


}
