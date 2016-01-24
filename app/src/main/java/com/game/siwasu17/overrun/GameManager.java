package com.game.siwasu17.overrun;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ゲーム全体を管理するためのクラス
 */
public class GameManager {
    private static final Paint TEXT_PAINT = new Paint();
    private static final Paint RECT_PAINT = new Paint();

    //世界の大きさ
    private final int WORLD_MAX_WIDTH = 8000;
    private final int WORLD_MAX_HEIGHT = 8000;

    //世界における描画領域の位置
    private final int VIEW_OFFSET_X = 2000;
    private final int VIEW_OFFSET_Y = 2000;

    //描画領域における可動範囲の位置
    private final int MOVABLE_OFFSET_X;
    private final int MOVABLE_OFFSET_Y;
    private final int MOVABLE_MAX_WIDTH;
    private final int MOVABLE_MAX_HEIGHT;

    static {
        TEXT_PAINT.setColor(Color.WHITE);
        TEXT_PAINT.setTextSize(40f);

        RECT_PAINT.setColor(Color.RED);
        RECT_PAINT.setStyle(Paint.Style.STROKE);
    }

    Canvas canvas;

    /**
     * 領域系三兄弟
     */
    Rect worldRect;
    Rect viewRect;
    Rect movableRect;


    int viewWidth;
    int viewHeight;

    private final Random rand = new Random();

    Ball mainBall;//操作キャラはゲーム全体に影響があるのでここで個別管理

    //ゲームオブジェクト一覧
    ArrayList<GameTask> taskList = new ArrayList<>();

    public GameManager(Canvas canvas){
        this.canvas = canvas;

        /**
         * 座標系は世界の原点を0,0とした絶対座標系
         * 描画時にオフセットをかけることで画面における相対座標系へ変換する
         */
        //世界の大きさ
        worldRect = new Rect();
        worldRect.left = 0;
        worldRect.top = 0;
        worldRect.right = WORLD_MAX_WIDTH;
        worldRect.bottom = WORLD_MAX_HEIGHT;

        //描画領域の大きさ
        viewRect = new Rect();
        viewRect.left = VIEW_OFFSET_X;
        viewRect.top = VIEW_OFFSET_Y;
        viewRect.right = VIEW_OFFSET_X + canvas.getWidth();
        viewRect.bottom = VIEW_OFFSET_Y + canvas.getHeight();

        //可動領域の左上は描画領域の大きさから算出
        MOVABLE_OFFSET_X = viewRect.width() / 4;
        MOVABLE_OFFSET_Y = viewRect.height() / 4;
        MOVABLE_MAX_WIDTH = viewRect.width() / 2;
        MOVABLE_MAX_HEIGHT = viewRect.height() / 2;

        //可動領域の大きさ
        movableRect = new Rect();
        movableRect.left = viewRect.left + MOVABLE_OFFSET_X;
        movableRect.top = viewRect.top + MOVABLE_OFFSET_Y;
        movableRect.right = movableRect.left + MOVABLE_MAX_WIDTH;
        movableRect.bottom = movableRect.top + MOVABLE_MAX_HEIGHT;
    }

    //相対座標に変換した時の可動領域
    public Rect getRelativeMovableRect(){
        return new Rect(
                (int)GameUtils.getDistanceX(viewRect,movableRect),
                (int)GameUtils.getDistanceY(viewRect,movableRect),
                MOVABLE_OFFSET_X + movableRect.width(),
                MOVABLE_OFFSET_Y + movableRect.height());
    }

    public void update(){

        mainBall.update();

        //可動領域を超えたか判定
        if(!movableRect.contains((int)mainBall.centerX,(int)mainBall.centerY)){
            int deltaX = 0;
            int deltaY = 0;
            if(movableRect.left > mainBall.centerX){
                deltaX = (int)Math.floor(mainBall.centerX - movableRect.left);
            }
            if(movableRect.right < mainBall.centerX){
                deltaX = (int)Math.ceil(mainBall.centerX - movableRect.right);
            }
            if(movableRect.top > mainBall.centerY){
                deltaY = (int)Math.floor(mainBall.centerY - movableRect.top);
            }
            if(movableRect.bottom < mainBall.centerY){
                deltaY = (int)Math.ceil(mainBall.centerY - movableRect.bottom);
            }

            //描画、可動領域を一緒に移動
            viewRect.offset(deltaX,deltaY);
            movableRect.offset(deltaX,deltaY);

        }

        for(GameTask task: taskList){
            task.update();
        }
    }

    public void draw(){
        //背景
        canvas.drawColor(Color.BLACK);

        //可動領域
        canvas.drawRect(getRelativeMovableRect(), RECT_PAINT);

        //メインキャラ
        mainBall.draw(canvas,viewRect);

        //その他
        for(GameTask task: taskList){
            task.draw(canvas,viewRect);
        }

        showStatusText();

    }

    public void showStatusText(){
        //ballの状態を表示
        List<String> statTextList = new ArrayList<>();
        statTextList.add("[ABS] X: " + mainBall.centerX);
        statTextList.add("[ABS] Y: " + mainBall.centerY);
        statTextList.add("VX: " + mainBall.velX);
        statTextList.add("VY: " + mainBall.velY);

        statTextList.add("Canvas W: " + canvas.getWidth());
        statTextList.add("Canvas H: " + canvas.getHeight());

        statTextList.add("[ABS] View left: " + viewRect.left);
        statTextList.add("[ABS] View top: " + viewRect.top);
        statTextList.add("[ABS] View right: " + viewRect.right);
        statTextList.add("[ABS] View bottom: " + viewRect.bottom);

        Rect rmr = getRelativeMovableRect();
        statTextList.add("[REL] Mova left: " + rmr.left);
        statTextList.add("[REL] Mova top: " + rmr.top);
        statTextList.add("[REL] Mova right: " + rmr.right);
        statTextList.add("[REL] Mova bottom: " + rmr.bottom);


        for (int i = 0; i < statTextList.size(); i++) {
            canvas.drawText(statTextList.get(i), 10, 100 + (50 * i), TEXT_PAINT);
        }
    }

    public void initializeWorld(){
        float halfWidth = viewWidth/2;
        float halfHeight = viewHeight/2;
        float ballR = 10;
        //メインキャラ生成
        mainBall = createBall(VIEW_OFFSET_X + halfWidth,VIEW_OFFSET_Y +  halfHeight, ballR);

        //追従者を生成

        for (int i = 0; i < 500; i++) {
            createFollowBallRandom(WORLD_MAX_WIDTH, WORLD_MAX_HEIGHT);
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

    public FollowBall createFollowBallRandom(int rangeX,int rangeY){
        float fbx = rand.nextInt(rangeX);
        float fby = rand.nextInt(rangeY);
        float fbr = 10;
        return createFollowBall(fbx, fby, fbr, mainBall);
    }

    //一定確率のくじを引く
    public boolean lot(int hit,int max){
        int r = rand.nextInt(max) + 1;
        return (r <= hit);
    }
}
