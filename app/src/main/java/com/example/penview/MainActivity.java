package com.example.penview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.sub.crop.activity.PictureCropActivity;

public class MainActivity extends AppCompatActivity {

    PullView pullView;
//    private PaintView mPenView;
    private PaintView2 mPenView;
    OpenGLVIew openGLVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mPenView = findViewById(R.id.gl);
//        openGLVIew = findViewById(R.id.gl);
        findViewById(R.id.clear_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPenView.eraserDraw();
                mPenView.setMode(PaintView2.Eraser);
            }
        });
        startActivity(new Intent(this, PictureCropActivity.class));
        findViewById(R.id.clear_tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPenView.penDraw();
                mPenView.setMode(PaintView2.Pen);
            }
        });
        findViewById(R.id.clear_tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPenView.clearDraw();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        openGLVIew.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        openGLVIew.onPause();
    }

    private float mTouchStartY;
    private static final float TOUCH_MOVE_MAX_Y = 300;
    private static final float SLIPPAGE_FACTOR = 0.5f;// 拖动阻力因子 0~1

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }


    /**
     * 使用surfacevIew效率更高
     */
    public class MyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

        /**
         * 每50帧刷新一次屏幕
         **/
        public static final int TIME_IN_FRAME = 50;

        /**
         * 游戏画笔
         **/
        Paint mPaint = null;
        Paint mTextPaint = null;
        SurfaceHolder mSurfaceHolder = null;

        /**
         * 控制游戏更新循环 ，当为false时候，不刷新页面
         **/
        boolean mRunning = false;

        /**
         * 游戏画布
         **/
        Canvas mCanvas = null;

        /**
         * 控制游戏循环
         **/
        boolean mIsRunning = false;

        /**
         * 曲线方向
         **/
        private Path mPath;

        private float mposX, mposY;

        public MyView(Context context) {
            super(context);
            /** 设置当前View拥有控制焦点 **/
            this.setFocusable(true);
            /** 设置当前View拥有触摸事件 **/
            this.setFocusableInTouchMode(true);
            /** 拿到SurfaceHolder对象 **/
            mSurfaceHolder = this.getHolder();
            /** 将mSurfaceHolder添加到Callback回调函数中 **/
            mSurfaceHolder.addCallback(this);
            /** 创建画布 **/
            mCanvas = new Canvas();
            /** 创建曲线画笔 **/
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            /**设置画笔抗锯齿**/
            mPaint.setAntiAlias(true);
            /**画笔的类型**/
            mPaint.setStyle(Paint.Style.STROKE);
            /**设置画笔变为圆滑状**/
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            /**设置线的宽度**/
            mPaint.setStrokeWidth(5);
            /**创建路径对象**/
            mPath = new Path();
            /** 创建文字画笔 **/
            mTextPaint = new Paint();
            /**设置颜色**/
            mTextPaint.setColor(Color.BLACK);
            /**设置文字大小**/
            mTextPaint.setTextSize(15);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            /** 拿到触摸的状态 **/
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();
            switch (action) {
                // 触摸按下的事件
                case MotionEvent.ACTION_DOWN:
                    /**设置曲线轨迹起点 X Y坐标**/
                    mPath.moveTo(x, y);
                    break;
                // 触摸移动的事件
                case MotionEvent.ACTION_MOVE:
                    /**设置曲线轨迹**/
                    //参数1 起始点X坐标
                    //参数2 起始点Y坐标
                    //参数3 结束点X坐标
                    //参数4 结束点Y坐标
                    mPath.quadTo(mposX, mposY, x, y);
                    break;
                // 触摸抬起的事件
                case MotionEvent.ACTION_UP:
                    /**按键抬起后清空路径轨迹**/
//                    mPath.reset();
                    break;
            }
            //记录当前触摸X Y坐标
            mposX = x;
            mposY = y;
            return true;
        }

        private void Draw() {
            /**清空画布**/
            mCanvas.drawColor(Color.WHITE);
            /**绘制曲线**/
            mCanvas.drawPath(mPath, mPaint);

            /**记录当前触点位置**/
            mCanvas.drawText("当前触笔 X：" + mposX, 0, 20, mTextPaint);
            mCanvas.drawText("当前触笔 Y：" + mposY, 0, 40, mTextPaint);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            /**开始游戏主循环线程**/
            mIsRunning = true;
            new Thread(this).start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mIsRunning = false;
        }

        /**
         * 线程运行原理surface创建的时候，开启线程，先锁定surfaceholder.lockcanvas()
         * 然后做绘制操作draw()
         * 最后解除锁定surfaceholder.unlockandpost()
         */
        @Override
        public void run() {

            while (mIsRunning) {

                /** 取得更新游戏之前的时间 **/
                long startTime = System.currentTimeMillis();

                /** 在这里加上线程安全锁 **/
                synchronized (mSurfaceHolder) {
                    /** 拿到当前画布 然后锁定 **/
                    mCanvas = mSurfaceHolder.lockCanvas();
                    Draw();
                    /** 绘制结束后解锁显示在屏幕上 **/
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }

                /** 取得更新游戏结束的时间 **/
                long endTime = System.currentTimeMillis();

                /** 计算出游戏一次更新的毫秒数 **/
                int diffTime = (int) (endTime - startTime);

                /** 确保每次更新时间为50帧 **/
                while (diffTime <= TIME_IN_FRAME) {
                    diffTime = (int) (System.currentTimeMillis() - startTime);
                    /** 线程等待 **/
                    Thread.yield();
                }

            }

        }
    }
}
