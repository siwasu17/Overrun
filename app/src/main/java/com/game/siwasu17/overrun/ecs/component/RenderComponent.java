package com.game.siwasu17.overrun.ecs.component;

/**
 * 描画があるユニットに持たせる
 */
public class RenderComponent extends BaseComponent {
    //描画対象の中心座標
    float x;
    float y;

    public RenderComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
