package com.game.siwasu17.overrun;

import android.util.Log;

/**
 * Created by yasu on 15/12/06.
 */
public class FollowBall extends Ball {
    private Ball parentBall; //追従対象
    private final float offLimit = 200.0f; //近づける限界


    public FollowBall(float x, float y, float r) {
        super(x, y, r);
    }

    public void setParentBall(Ball ball) {
        this.parentBall = ball;
    }

    @Override
    public void move() {

        //if(getDistance() > offLimit) {
            this.velX = (parentBall.centerX - this.centerX) / 10;
            this.velY = (parentBall.centerY - this.centerY) / 10;

            this.centerX += velX;
            this.centerY += velY;
        //}
    }

    private float getDistance(){
        //この辺りの処理もっと楽にできるような気がする
        return getDistance(this.centerX,this.centerY,parentBall.centerX,parentBall.centerY);
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        //TODO: Util化
        return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
