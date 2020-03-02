package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class AlphaView extends View {

    // 实例化画笔
    private Paint mPaint = null;
    private Path mPath;// 路径对象
    private ArrayList<PathWrapper> mPaths = new ArrayList<>();
    private Canvas mCavas;
    private Bitmap mBitmap;
    public AlphaView(Context context) {
        super(context);
    }


    public AlphaView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化画笔
        initPaint();
    }

    private void initPaint() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);//对单独的View在运行时阶段禁用硬件加速
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(60);
//        post(new On)
        post(new Runnable() {
            @Override
            public void run() {

            }
        });
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
              mBitmap=  Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_4444);
              mCavas = new Canvas(mBitmap);
            }
        });

        mPaint.setColor(Color.parseColor("#88ff00ff"));
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        canvas.drawText("以客户为中心",300,300,paint);
        canvas.drawText("以奋斗者为本",300,500,paint);
        canvas.drawBitmap(mBitmap,0,0,null);
//        for (PathWrapper wrapper:mPaths){
//            mPaint.setColor(wrapper.getColor());
//            canvas.drawPath(wrapper.getPath(),mPaint);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mPath.moveTo(x,y);
                mPaths.add(new PathWrapper(mPath,mPaint.getColor()));
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
//                mPath.lineTo(x,y);
                mPath.lineTo(x,y);
                mCavas.drawPath(mPath,mPaint);
                break;
        }
        invalidate();
        return true;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }
}
