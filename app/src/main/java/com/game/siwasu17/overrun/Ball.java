package com.game.siwasu17.overrun;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 操作するメインキャラ
 */
public class Ball implements GameTask {
    protected final String LOG_TAG = getClass().getSimpleName();
    private final Paint paint = new Paint();
    //常に速度を減衰させるようにするモード
    boolean isBraking = false;
    final float defaultDecayRate = 0.97f;//減衰率
    final float forceBoostRate = 2.0f;//強制加速
    float decayRate = defaultDecayRate;
    final float stopVelRange = 0.5f;//停止する限界速度
    final float minReactDose = 5.0f; //スクロールで動かすときの最小反応量
    final float maxVelocity = 30.0f;
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
        paint.setColor(this.color.argb(255,100,255,255));
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
        float r = (float)Math.sqrt(vx*vx + vy*vy);
        if(r > this.minReactDose) {
            this.velX = vx;
            this.velY = vy;

            this.decayRate = defaultDecayRate;
        }
    }

    public void forceBoost(){
        this.decayRate = forceBoostRate;
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
        if(oblique > maxVelocity) {
            velX = maxVelocity * sin;
            velY = maxVelocity * cos;
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

    @Override
    public void update() {
        move();
    }

    @Override
    public void offset(float x, float y) {
        //強制的な座標補正
        this.centerX += x;
        this.centerY += y;
    }

    @Override
    public void draw(Canvas canvas){
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        if((centerX <= w) && (centerY <= h)) {
            canvas.drawCircle(centerX, centerY, radius, paint);
        }
    }
}
