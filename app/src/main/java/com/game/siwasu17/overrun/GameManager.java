package com.game.siwasu17.overrun;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ゲーム全体を管理するためのクラス
 */
public class GameManager {
    private static final int POWER_GAUGE_HEIGHT = 30;
    private static final Paint PAINT_POWER_GAUGE = new Paint();
    private static final Paint TEXT_PAINT = new Paint();

    static {
        PAINT_POWER_GAUGE.setColor(Color.RED);
        TEXT_PAINT.setColor(Color.WHITE);
        TEXT_PAINT.setTextSize(40f);
    }

    int maxWorldWidth;
    int maxWorldHeight;

    //ゲーム領域全体の原点に対するオフセット
    float offsetX;
    float offsetY;

    Canvas canvas;
    int viewWidth;
    int viewHeight;

    private final Random rand = new Random();

    Ball mainBall;//操作キャラはゲーム全体に影響があるのでここで個別管理

    //ゲームオブジェクト一覧
    ArrayList<GameTask> taskList = new ArrayList<>();

    public GameManager(Canvas canvas){
        this.canvas = canvas;
        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();

        maxWorldHeight = 1000;
        maxWorldWidth = viewWidth;

        offsetX = 0;
        offsetY = 500;
    }

    public void update(){
        mainBall.update();

        if(mainBall.centerX > viewWidth){
            mainBall.centerX = viewWidth;
        }else if(mainBall.centerX < 0){
            mainBall.centerX = 0;
        }
        if(mainBall.centerY > viewHeight){
            mainBall.centerY = viewHeight;
        }else if(mainBall.centerY < 0){
            mainBall.centerY = 0;
        }

        for(GameTask task: taskList){
            task.update();
        }
    }

    public void draw(){
        //背景
        canvas.drawColor(Color.BLACK);

        //メインキャラ
        mainBall.draw(canvas);

        for(GameTask task: taskList){
            task.draw(canvas);
        }


        //ballの状態を表示
        List<String> statTextList = new ArrayList<>();
        statTextList.add("X: " + mainBall.centerX);
        statTextList.add("Y: " + mainBall.centerY);
        statTextList.add("VX: " + mainBall.velX);
        statTextList.add("VY: " + mainBall.velY);
        for (int i = 0; i < statTextList.size(); i++) {
            canvas.drawText(statTextList.get(i), 10, 100 + (50 * i), TEXT_PAINT);

        }

        //ゲージを表示
        /*
        if(touchDownStartTime > 0){
            float elapsedTime = System.currentTimeMillis() - touchDownStartTime;
            canvas.drawRect(0,0,width * (elapsedTime / MAX_TOUCH_TIME),POWER_GAUGE_HEIGHT,PAINT_POWER_GAUGE);
        }
        */
    }

    public void initializeWorld(){
        float halfWidth = viewWidth/2;
        float halfHeight = viewHeight/2;
        float ballR = 10;
        //メインキャラ生成
        mainBall = createBall(halfWidth, halfHeight, ballR);

        //追従者を生成
        for (int i = 0; i < 20; i++) {
            float offsetX = rand.nextInt(viewWidth);
            float offsetY = rand.nextInt(viewHeight);

            createFollowBall(offsetX, offsetY, ballR, mainBall);
        }
    }


    public Ball getMainBall(){
        return mainBall;
    }

    public Ball createBall(float x,float y,float r){
        if(mainBall == null){
            mainBall = new Ball(x,y,r);
        }
        return mainBall;
    }

    public FollowBall createFollowBall(float x,float y,float r,Ball b){
        FollowBall fb  = new FollowBall(x,y,r);
        fb.setParentBall(b);
        fb.setColor(Color.YELLOW);
        taskList.add(fb);
        return fb;
    }
}
