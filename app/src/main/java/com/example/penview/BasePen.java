package com.example.penview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePen {

    protected String TAG = getClass().getName();

    protected Paint mPaint;
    protected Canvas mCanvas;
    private Bitmap mBitmap;

    private List<PointF> mPoints;

    public BasePen(int w, int h) {
        init(w, h);
    }

    private void init(int w, int h) {
        mPoints = new ArrayList<>();
        //特定的笔刷样式有特定的Paint
        mPaint = generateSpecificPaint();
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void setPenWidth(@FloatRange(from = 1, to = 100) float width) {

    }

    @NonNull
    protected abstract Paint generateSpecificPaint();

    public void onTouchEvent(MotionEvent event1) {
        Log.d(TAG, "onTouchEvent: " + event1.getActionMasked());
        switch (event1.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                clearPoints();
                handlePoints(event1);
                break;
            case MotionEvent.ACTION_MOVE:
                handlePoints(event1);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    private void handlePoints(MotionEvent event1) {
        float x = event1.getX();
        float y = event1.getY();
        if (x > 0 && y > 0) {
            mPoints.add(new PointF(x, y));
        }
    }

    public void onDraw(Canvas canvas) {
        if (mPoints != null && !mPoints.isEmpty()) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
            drawDetail(canvas);
        }
    }

    //由各个画笔实现,参数为所依赖的view的canvas
    protected abstract void drawDetail(Canvas canvas);

    private void clearPoints() {
        if (mPoints == null) {
            return;
        }
        mPoints.clear();
    }

    protected PointF getCurPoint() {
        if (mPoints == null || mPoints.isEmpty()) {
            return new PointF(0, 0);
        }
        return mPoints.get(mPoints.size() - 1);
    }

    protected List<PointF> getPoints() {
        ArrayList<PointF> points = new ArrayList<>();
        if (mPoints == null) {
            return points;
        }
        for (PointF point : mPoints) {
            points.add(point);
        }
        return points;
    }

    /**
     * @return 此次move的距离
     */
    protected double getLastDis() {
        return getIndexDis(getPoints().size() - 1);
    }

    private double getIndexDis(@IntRange(from = 1) int index) {
        if (getPoints().size() <= 1) {
            return 0;
        }
        if (index < 1 || index >= getPoints().size()) {
            return 0;
        }
        PointF indexP = getPoints().get(index);
        PointF lastP = getPoints().get(index - 1);
        return Math.hypot(indexP.x - lastP.x, indexP.y - lastP.y);
    }

    protected double getIndexRadians(@IntRange(from = 1) int index) {
        if (getPoints().size() <= 1) {
            return -1;
        }
        if (index < 1 || index >= getPoints().size()) {
            return -1;
        }
        PointF indexP = getPoints().get(index);
        PointF lastP = getPoints().get(index - 1);
        float gapX = lastP.x - indexP.x;
        float gapY = lastP.y - indexP.y;
        return Math.toRadians(Math.toDegrees(Math.atan2(gapY, gapX)));
    }

    /**
     * @return 滑动总距离
     */
    public double getTotalDis() {
        if (getPoints().isEmpty() || getPoints().size() == 1) {
            return 0;
        }
        double total = 0;
        for (int i = 1; i < getPoints().size(); i++) {
            total += getIndexDis(i);
        }
        return total;
    }

    public void clearDraw() {
        if (mCanvas == null) {
            return;
        }
        clearPoints();
        mCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
    }

    public void release() {
        if (mPoints != null) {
            mPoints.clear();
            mPoints = null;
        }
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}