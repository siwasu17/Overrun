package com.game.siwasu17.overrun.ecs.manager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


import com.game.siwasu17.overrun.ecs.factory.EntityFactory;
import com.game.siwasu17.overrun.ecs.system.MoveSystem;
import com.game.siwasu17.overrun.ecs.system.RenderSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ゲーム全体を管理するためのクラス
 */
public class GameManager {
    private static final Paint TEXT_PAINT = new Paint();
    private static final Paint RECT_PAINT = new Paint();

    static {
        TEXT_PAINT.setColor(Color.WHITE);
        TEXT_PAINT.setTextSize(40f);

        RECT_PAINT.setColor(Color.RED);
        RECT_PAINT.setStyle(Paint.Style.STROKE);
    }

    Canvas canvas;
    //画面全体
    Rect viewRect;
    //達成タスク描画領域
    Rect parkRect;
    //未完了タスク描画領域
    Rect boxRect;

    //エンティティ管理系
    EntityManager entityManager;
    EntityFactory entityFactory;

    //システム系
    MoveSystem moveSystem;
    RenderSystem renderSystem;

    private final Random rand = new Random();

    public GameManager(Canvas canvas) {
        this.canvas = canvas;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        this.viewRect = new Rect(0,0,width,height);

        //画面分割率
        float horizonDivideRate = 0.4f;
        this.parkRect = new Rect(0,0,width,(int)(height * horizonDivideRate));
        this.boxRect = new Rect(0,parkRect.bottom,width,height);

        entityManager = new EntityManager();
        entityFactory = new EntityFactory(entityManager);
        moveSystem = new MoveSystem(entityManager);
        renderSystem = new RenderSystem(entityManager);

    }

    public void update() {
        //各システムのupdateを呼び出す
        //update,drawの順序依存が生じるようならGameManagerは捨てる
        moveSystem.update();
        renderSystem.update();
    }

    public void draw() {
        //背景
        canvas.drawColor(Color.BLACK);

        /*
        //TODO: ここのRectの型について調査
        canvas.drawRect(parkRect, Color.GREEN);
        canvas.drawRect(boxRect,Color.GRAY);
        */

        //エンティティ描画
        renderSystem.draw();

        showStatusText();
    }

    public void showStatusText() {
        //ballの状態を表示
        List<String> statTextList = new ArrayList<>();
        statTextList.add("[ABS] View left: " + viewRect.left);
        statTextList.add("[ABS] View top: " + viewRect.top);
        statTextList.add("[ABS] View right: " + viewRect.right);
        statTextList.add("[ABS] View bottom: " + viewRect.bottom);
        for (int i = 0; i < statTextList.size(); i++) {
            canvas.drawText(statTextList.get(i), 10, 100 + (50 * i), TEXT_PAINT);
        }
    }

    public void initializeWorld() {
        //システムとかいろいろ生成

    }

}
