package com.game.siwasu17.overrun;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by yasu on 15/11/30.
 */
public class Ball {
    protected final String LOG_TAG = getClass().getSimpleName();
    private final Paint paint = new Paint();
    //常に速度を減衰させるようにするモード
    boolean isBraking = false;
    final float decayRate = 0.97f;//減衰率
    final float stopVelRange = 0.5f;//停止する限界速度
    final float MAX_VELOCITY = 30.0f;
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
        //ブレーキON
        this.isBraking = true;

        //初期色
        paint.setColor(color.argb(255,100,255,255));
        paint.setAntiAlias(true);
    }

    public void setColor(int color){
        this.paint.setColor(color);
    }

    public void setAccel(float ax,float ay){
        this.accelX = ax;
        this.accelY = ay;
    }

    public void setVelocity(float vx,float vy){
        this.velX = vx;
        this.velY = vy;
    }

    public void decayVelocity(){
        //ブレーキ機能がオンの時
        velX *= decayRate;
        if(Math.abs(velX) < stopVelRange){
            velX = 0;
        }

        velY *= decayRate;
        if(Math.abs(velY) < stopVelRange){
            velY = 0;
        }
    }

    public void limitMaxSpeed(){
        //ベクトルの最大の長さが一定に収まるように調整
        float oblique = (float)Math.sqrt(velX*velX + velY*velY);
        float sin = velX / oblique;
        float cos = velY / oblique;
        if(oblique > MAX_VELOCITY) {
            velX = MAX_VELOCITY * sin;
            velY = MAX_VELOCITY * cos;
        }
    }

    public void move(){
        if(isBraking){
            decayVelocity();
            limitMaxSpeed();

            centerX += velX;
            centerY += velY;
        }else {
            velX += accelX;
            velY += accelY;

            centerX += velX;
            centerY += velY;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(centerX,centerY,radius,paint);
    }
}
