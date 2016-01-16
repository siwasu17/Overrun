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
                    processGame(canvas);
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
            Ball ball = gm.getMainBall();
            ball.forceBoost();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.i(LOG_TAG, "[Flick] X:" + velocityX + " Y:" + velocityY);
            Ball ball = gm.getMainBall();
            ball.setVelocity(velocityX / 100, velocityY / 100);


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
    public interface Callback {
        void onGameOver();
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private final Handler handler;
    private boolean isGameOver;
    private GameManager gm;

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
     * ゲーム全体の進行・描画
     *
     * @param canvas 描画するキャンパス
     */
    public void processGame(Canvas canvas) {
        if (gm == null) {
            gm = new GameManager(canvas);
            gm.initializeWorld();
        }
        gm.update();
        gm.draw();
    }

    /**
     * Game Objects
     */
    private Ball ball;
    private ArrayList<Ball> followers = new ArrayList<>();
}
