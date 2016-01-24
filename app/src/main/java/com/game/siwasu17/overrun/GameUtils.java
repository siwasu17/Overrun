package com.game.siwasu17.overrun;

import android.graphics.Rect;

/**
 * 各種ユーティリティ関数用
 */
public class GameUtils {

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //矩形の左上の点の間の差分
    public static float getDistance(Rect r1,Rect r2){
        return getDistance(r1.left,r1.top,r2.left,r2.top);
    }

    //矩形の左上の点の間の差分(X軸)
    public static float getDistanceX(Rect r1,Rect r2){
        return (float)Math.abs(r1.left - r2.left);
    }

    //矩形の左上の点の間の差分(Y軸)
    public static float getDistanceY(Rect r1,Rect r2){
        return (float)Math.abs(r1.top - r2.top);
    }

}
