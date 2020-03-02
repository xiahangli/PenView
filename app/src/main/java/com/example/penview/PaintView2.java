package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Henry
 * @Date 2020-03-03  03:25
 * @Email 2427417167@qq.com
 */
public class PaintView2 extends View {
    public static int SCREEN_W;
    public static int SCREEN_H;
    public static int Pen = 1;
    public static int Eraser = 2;

    public PaintView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void setScreenWH() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        SCREEN_W = screenWidth;
        SCREEN_H = screenHeight;
    }

    //设置绘制模式是“画笔”还是“橡皮擦”
    public void setMode(int mode) {
        this.mMode = mode;
    }

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mEraserPaint;
    private Paint mPaint;
    private int mMode = 1;
    private Path mPath;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void initPaint() {
        setScreenWH();
        //画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(255);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//        mEraserPaint.setAlpha(255);
        mPaint.setStrokeWidth(10);
        //橡皮擦
        mEraserPaint = new Paint();
        mEraserPaint.setAlpha(0);
        //这个属性是设置paint为橡皮擦重中之重
        //这是重点
        //下面这句代码是橡皮擦设置的重点
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeWidth(30);

        mPath = new Path();

        mBitmap = Bitmap.createBitmap(SCREEN_W, SCREEN_H, Bitmap.Config.ARGB_8888);
        mBitmap.setHasAlpha(true);//告诉图片没有alpha通道,即使配置有alpha的通道
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.BLUE);
    }

    private void init() {

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            if (mMode == Pen) {
                mCanvas.drawPath(mPath, mPaint);
            }
            if (mMode == Eraser) {
                mCanvas.drawPath(mPath, mEraserPaint);
            }
        }
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        //如果是“画笔”模式就用mPaint画笔进行绘制
        if (mMode == Pen) {
            mCanvas.drawPath(mPath, mPaint);
        }
        //如果是“橡皮擦”模式就用mEraserPaint画笔进行绘制DST_IN模式
        if (mMode == Eraser) {
            mCanvas.drawPath(mPath, mEraserPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        if (mMode == Pen) {
            mCanvas.drawPath(mPath, mPaint);
        }
        if (mMode == Eraser) {
            mCanvas.drawPath(mPath, mEraserPaint);
        }
    }
}
