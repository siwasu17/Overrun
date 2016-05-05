package com.game.siwasu17.overrun.ecs.component;

import android.graphics.Rect;

/**
 * 衝突があるユニットに持たせる
 */
public class CollisionComponent extends BaseComponent {
    //衝突判定のある領域
    Rect solidSize;
    //多分Renderコンポーネントと関わるのがいろいろあるはず
}
