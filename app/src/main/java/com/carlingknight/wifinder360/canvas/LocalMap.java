package com.carlingknight.wifinder360.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.carlingknight.wifinder360.map.MapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carling Knight on 04/04/2017.
 */

public class LocalMap extends View {

    Paint paint = new Paint();
    Canvas thisCanvas = new Canvas();

    List<MapObject> objects = new ArrayList<>();

    public LocalMap(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public LocalMap(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        thisCanvas = canvas;
        for (MapObject mapObject : objects){
            thisCanvas.drawRect(mapObject.x, mapObject.y, mapObject.size_x, mapObject.size_y, paint);
        }
    }


    public void addObject(MapObject object){
        objects.add(object);
    }

    public void refreshCanvas(){
        draw(thisCanvas);
    }

}
