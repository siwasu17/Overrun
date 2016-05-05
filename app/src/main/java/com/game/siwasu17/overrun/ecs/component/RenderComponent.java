package com.game.siwasu17.overrun.ecs.component;

/**
 * 描画があるユニットに持たせる
 */
public class RenderComponent extends BaseComponent {
    //描画対象の中心座標
    float x;
    float y;

    //アニメーション系情報
    //TODO:画像のストックを管理する方法を整理(あとで)

    public RenderComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
