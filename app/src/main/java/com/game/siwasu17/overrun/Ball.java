package com.game.siwasu17.overrun;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by yasu on 15/11/30.
 */
public class Ball {
    private final Paint paint = new Paint();
    float centerX;
    float centerY;
    float radius;
    float accelX;
    float accelY;
    float velX;
    float velY;
    private final Color color = new Color();

    public Ball(float x,float y,float r) {
        this.centerX = x;
        this.centerY = y;
        this.radius = r;

        //加速
        this.accelX = 0;
        this.accelY = 0;
        //速度
        this.velX = 0;
        this.velY = 0;

        //初期色
        paint.setColor(color.argb(255,100,255,255));
        paint.setAntiAlias(true);
    }

    public void setAccel(float ax,float ay){
        this.accelX = ax;
        this.accelY = ay;
    }

    public void move(){
        velX += accelX;
        velY += accelY;

        centerX += velX;
        centerY += velY;
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(centerX,centerY,radius,paint);
    }
}
