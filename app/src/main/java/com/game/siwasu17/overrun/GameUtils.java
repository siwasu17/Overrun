package com.game.siwasu17.overrun;

/**
 * Created by yasu on 16/01/03.
 */
public class GameUtils {

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
