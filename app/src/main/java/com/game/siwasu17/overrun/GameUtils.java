package com.game.siwasu17.overrun;

/**
 * 各種ユーティリティ関数用
 */
public class GameUtils {

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
