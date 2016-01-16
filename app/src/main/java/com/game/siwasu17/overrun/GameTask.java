package com.game.siwasu17.overrun;

import android.graphics.Canvas;

/**
 * ゲームオブジェクトの更新と描画の抽象化
 */
public interface GameTask {
    void update();
    void draw(Canvas canvas);
}
