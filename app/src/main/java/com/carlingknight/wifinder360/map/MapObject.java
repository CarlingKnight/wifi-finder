package com.carlingknight.wifinder360.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Carling Knight on 04/04/2017.
 */

public class MapObject {

    Canvas parentCanvas = new Canvas();
    public Integer x;
    public Integer y;
    public Integer size_x;
    public Integer size_y;

    Paint paint = new Paint();

    public MapObject(Integer x, Integer y, Integer size_x, Integer size_y){
        this.x = x;
        this.y = y;
        this.size_x = size_x;
        this.size_y = size_y;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
    }

    public void setCanvas(Canvas parentCanvas){
        this.parentCanvas = parentCanvas;
    }
}
