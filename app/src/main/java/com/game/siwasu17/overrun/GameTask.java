package com.game.siwasu17.overrun;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * ゲームオブジェクトの更新と描画の抽象化
 */
public interface GameTask {
    void update();
    void draw(Canvas canvas,Rect viewRect);
    void offset(float x,float y);
}
