package com.game.siwasu17.overrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by yasu on 21/11/27.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Game Loop
     */
    private static final long FPS = 60;

    private class DrawThread extends Thread {
        boolean isFinished;

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();
            while (!isFinished) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawGame(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private DrawThread drawThread;

    public void startDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }

        drawThread.isFinished = true;
        drawThread = null;
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //NOTHING
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }

    /**
     * Game Main
     */

    private static final int POWER_GAUGE_HEIGHT = 30;
    private static final Paint PAINT_POWER_GAUGE = new Paint();
    static {
        PAINT_POWER_GAUGE.setColor(Color.RED);
    }

    public interface Callback{
        void onGameOver();
    }

    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private final Handler handler;
    private boolean isGameOver;
    private final Random rand = new Random();

    private static final int MAX_TOUCH_TIME = 500; //msec
    private long touchDownStartTime;

    public GameView(Context context) {
        super(context);

        handler = new Handler();
        getHolder().addCallback(this);
    }

    private void gameOver(){
        if(isGameOver){
            return;
        }
        isGameOver = true;

        //非同期にメインスレッドに通知するためHandlerを使う
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onGameOver();
            }
        });
    }

    private boolean isTouchDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownStartTime = System.currentTimeMillis();
                isTouchDown = true;
                return true;
            case MotionEvent.ACTION_UP:
                isTouchDown = false;
                touchDownStartTime = 0;
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void drawGame(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawColor(Color.WHITE);

        //ゲージを表示
        if(touchDownStartTime > 0){
            float elapsedTime = System.currentTimeMillis() - touchDownStartTime;
            canvas.drawRect(0,0,width * (elapsedTime / MAX_TOUCH_TIME),POWER_GAUGE_HEIGHT,PAINT_POWER_GAUGE);
        }

    }

}
