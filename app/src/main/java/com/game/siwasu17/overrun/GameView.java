package com.game.siwasu17.overrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * ゲーム画面
 */
public class GameView extends SurfaceView
        implements SurfaceHolder.Callback, SensorEventListener {
    private static final String LOG_TAG = GameView.class.getSimpleName();

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
        //startSensor();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //NOTHING
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
        //stopSensor();
    }

    /**
     * ジェスチャー認識
     */
    private GestureDetector mDetector;
    private OnGestureListener mGestureListener
            = new OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(LOG_TAG, "Down");
            /*
            if (ball != null) {
                ball.forceBoost();
            }
            */
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(LOG_TAG, "Tap");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /*
            Log.i(LOG_TAG,"[Scroll] X:" + distanceX + " Y:" + distanceY);
            if (ball != null) {
                ball.setVelocity(-distanceX, -distanceY);
            }
            */

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(LOG_TAG, "LongPress!!!");
            if (ball != null) {
                ball.forceBoost();
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.i(LOG_TAG, "[Flick] X:" + velocityX + " Y:" + velocityY);
            if (ball != null) {
                ball.setVelocity(velocityX, velocityY);
            }

            return false;
        }
    };

    //Activityのタッチイベントから呼んでもらうため
    public GestureDetector getGestureDetector() {
        return this.mDetector;
    }

    /**
     * 加速度センサー処理系
     */
    private float[] sensorValues = null;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorValues == null) {
            sensorValues = new float[3];
        }
        sensorValues[0] = event.values[0];
        sensorValues[1] = event.values[1];
        sensorValues[2] = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startSensor() {
        sensorValues = null;
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSensor() {
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }

    /**
     * Game Main
     */
    private static final int POWER_GAUGE_HEIGHT = 30;
    private static final Paint PAINT_POWER_GAUGE = new Paint();
    private static final Paint TEXT_PAINT = new Paint();

    static {
        PAINT_POWER_GAUGE.setColor(Color.RED);
        TEXT_PAINT.setColor(Color.WHITE);
        TEXT_PAINT.setTextSize(40f);
    }

    public interface Callback {
        void onGameOver();
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private final Handler handler;
    private boolean isGameOver;
    private final Random rand = new Random();

    private static final int MAX_TOUCH_TIME = 500; //msec
    private long touchDownStartTime;

    public GameView(Context context) {
        super(context);
        mDetector = new GestureDetector(context, mGestureListener);
        handler = new Handler();
        getHolder().addCallback(this);
    }


    private void gameOver() {
        if (isGameOver) {
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


    /**
     * 全体の描画系
     *
     * @param canvas 描画するキャンパス
     */
    public void drawGame(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawColor(Color.BLACK);

        if (ball == null) {
            float halfWidth = width/2;
            float halfHeight = height/2;
            float ballR = 10;

            ball = new Ball(halfWidth,halfHeight,ballR);

            //追従者を生成
            for (int i = 0; i < 20; i++) {
                float offsetX = rand.nextInt(width);
                float offsetY = rand.nextInt(height);

                //FollowBall fb  = new FollowBall(halfWidth + offsetX, halfHeight + offsetY,ballR);
                FollowBall fb  = new FollowBall(offsetX,offsetY,ballR);
                fb.setParentBall(ball);
                fb.setColor(Color.YELLOW);
                followers.add(fb);
            }

        } else {
            //ballの状態を表示
            //メッセージ領域のクラスを作ってもいいかも
            List<String> statTextList = new ArrayList<>();
            statTextList.add("X: " + ball.centerX);
            statTextList.add("Y: " + ball.centerY);
            statTextList.add("VX: " + ball.velX);
            statTextList.add("VY: " + ball.velY);
            for (int i = 0; i < statTextList.size(); i++) {
                canvas.drawText(statTextList.get(i), 10, 100 + (50 * i), TEXT_PAINT);
            }
        }

        //加速度を表示
        /*
        if(sensorValues != null){
            canvas.drawText("sensor[0]: " + sensorValues[0],10,150,TEXT_PAINT);
            canvas.drawText("sensor[1]: " + sensorValues[1],10,200,TEXT_PAINT);
            canvas.drawText("sensor[2]: " + sensorValues[2],10,250,TEXT_PAINT);

            //ボールに加速度を加える
            ball.setAccel(-(sensorValues[0]/10),sensorValues[1]/10);
        }
        */


        ball.move();
        //領域外に出ないように調整
        if(ball.centerX > width){
            ball.centerX = width;
        }else if(ball.centerX < 0){
            ball.centerX = 0;
        }
        if(ball.centerY > height){
            ball.centerY = height;
        }else if(ball.centerY < 0){
            ball.centerY = 0;
        }

        ball.draw(canvas);



        for(Ball b : followers){
            b.move();
            b.draw(canvas);
        }

        //ゲージを表示
        /*
        if(touchDownStartTime > 0){
            float elapsedTime = System.currentTimeMillis() - touchDownStartTime;
            canvas.drawRect(0,0,width * (elapsedTime / MAX_TOUCH_TIME),POWER_GAUGE_HEIGHT,PAINT_POWER_GAUGE);
        }
        */
    }

    /**
     * Game Objects
     */
    private Ball ball;
    private ArrayList<Ball> followers = new ArrayList<>();
}
