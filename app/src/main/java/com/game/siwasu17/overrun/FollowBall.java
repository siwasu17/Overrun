package com.game.siwasu17.overrun;

import android.util.Log;

/**
 * メインキャラに追従するキャラ
 */
public class FollowBall extends Ball {
    private Ball parentBall; //追従対象
    boolean isFollowing = false;//追従中かどうか
    private final float offLimit = 100.0f; //近づける限界
    private final float inArea = 300.0f; //この範囲内に入ったら追従を開始する


    public FollowBall(float x, float y, float r) {
        super(x, y, r);
    }

    public void setParentBall(Ball ball) {
        this.parentBall = ball;
    }

    @Override
    public void move() {
        float distance = getDistanceFromAlpha();
        if(isFollowing){
            if(distance > offLimit){
                //近づく限界までは近づいて良い
                this.velX = (parentBall.centerX - this.centerX) / 20;
                this.velY = (parentBall.centerY - this.centerY) / 20;

                if(Math.abs(this.velX) > 1.0f) {
                    this.centerX += velX;
                }
                if(Math.abs(this.velY) > 1.0f) {
                    this.centerY += velY;
                }
            }
        }else{
            //追従対象の一定範囲内にはいったら追従開始
            if(distance <= inArea){
                isFollowing = true;
            }
        }
    }

    //追従対象からの距離を取得
    private float getDistanceFromAlpha(){
        return GameUtils.getDistance(this.centerX,this.centerY,parentBall.centerX,parentBall.centerY);
    }
}
