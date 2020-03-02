package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This view implements the drawing canvas.
 * <p>
 * It handles all of the input events and drawing functions.
 */
public class PaintView extends View {
    private Paint paint;
    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;
    private Path path;
    private static final int ERASER = 137;
    private static final int PEN = 137;
    private int type = PEN;
    Region eraserRegion = new Region();


    Point eraserLast = new Point();
    List<Rect> eraserPath = new ArrayList<Rect>();
    private Path path2 = new Path();

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        path = new Path();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
        DisplayMetrics displayMetrics1 = getResources().getDisplayMetrics();
        int width = displayMetrics1.widthPixels;
        int height = displayMetrics1.heightPixels;
        cachebBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        //图层中添加白色背景
        cacheCanvas.drawColor(Color.TRANSPARENT);
        path.moveTo(200, 200);
        path.lineTo(200, 400);
//        paths.add(path);
        path2.moveTo(200, 300);
        path2.lineTo(200, 500);
        invalidate();
    }

    ArrayList<Path> paths = new ArrayList<>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                paths.add(path);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
//                if (type == PEN) {
                    cacheCanvas.drawPath(path, paint);
//                }
                break;
            case MotionEvent.ACTION_UP:
                path.reset();//清空path,和模式相关,不reset就是清楚全部了，原因就是因为之前我们的path其实是好多的线段，而现在画的线段是在原来的基础上moveto和lineto的，相交区域是所有
                break;

        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(20);
        canvas.drawBitmap(cachebBitmap, 0, 0, paint);

        //move时候画线
        for (int i = 0; i < paths.size(); i++) {
            paint.setColor(Color.RED);
//            paint.setColor(Color.BLUE & 0x22ffffff);
            canvas.drawPath(path, paint);
//            paint.setColor(Color.RED & 0x22ffffff);
//            canvas.drawPath(path2, paint);
        }
    }

    public void eraserDraw() {
        type = ERASER;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStrokeWidth(20);
    }

    public void penDraw() {
        paint.setXfermode(null);//NULL
        type = PEN;
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        paint.setAlpha((int) (0.5 * 255));
    }

    public void clearDraw() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        path.moveTo(200, 200);
        path.lineTo(500, 500);
        cacheCanvas.drawPath(path, paint);
//        cacheCanvas.drawColor(Color.parseColor("#eeffff00"));
        invalidate();
    }
}