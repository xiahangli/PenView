package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class XfermodeView extends View {

    Bitmap mBgBitmap, mFgBitmap;
    Paint mPaint;
    Canvas mCanvas;
    Path mPath;

    public XfermodeView(Context context) {
        super(context);
        init();
    }

    public XfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /*
     *   让它的笔触和连接处能更加圆滑一点，即Paint.Join.ROUND和Paint.Cap.ROUND属性
     *   最关键的一步是：需要将画笔的透明度设置为0，这样才能显示出擦除的效果，因为在使用PorterDuffXfermode进行图层混合时，并不是简单地进行图层的计算，同时也会去计算透明度通道的值
     *
     */

    Bitmap rectBitmap;
    Bitmap circleBitmap;

    private void init() {
        mPaint = new Paint();
//        rectBitmap = Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
//        mCanvas
//        rectBitmap
//        mPaint.setAlpha(0);
        setBackgroundColor(0xff00ffff);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(50);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath = new Path();
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dest);
        mBgBitmap = mBgBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap bitmap =null;
        bitmap    = BitmapFactory.decodeResource(getResources(), R.drawable.destination);
        bitmap = Bitmap.createScaledBitmap(bitmap,200,200,true);
        mFgBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager systemService = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        Display defaultDisplay = systemService.getDefaultDisplay();
//        defaultDisplay.getMetrics(displayMetrics);
//
        DisplayMetrics displayMetrics1 = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics1.widthPixels  ;
//        getDisplay().getMetrics();
//        defaultDisplay.getMetrics();
        mCanvas = new Canvas(mFgBitmap);
        //使用srcover的模式，添加颜色层
//        mCanvas.drawColor(Color.WHITE);
//        mCanvas.drawARGB(125,0,0,0);
//        mCanvas.drawColor(Color.GRAY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        mPath.reset();
//                        mPath.moveTo(event.getX(),event.getY());
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        mPath.lineTo(event.getX(),event.getY());
//                        break;
//                }
//
//        mCanvas.drawPath(mPath, mPaint);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Matrix matrix = new Matrix();
//        matrix.setRotate(-20,0,0);
//        canvas.drawBitmap(mFgBitmap, matrix, null);


//        canvas.save();
//        canvas.rotate(-5, 0, 0);//逆时针
//        canvas.translate(+100, 0);
        Paint paint = new Paint();
        canvas.drawBitmap(mBgBitmap, 0, 0, paint);
//        canvas.restore();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCanvas.drawColor(0xffffff00, PorterDuff.Mode.SRC_OVER);
        canvas.drawBitmap(mFgBitmap, 0, 0, paint);
//        canvas.drawColor(0xccffFF00);


//        canvas.drawBitmap(mFgBitmap, 50, 50, paint);

//        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
//        canvas.drawBitmap(rectBitmap, 0, 0, mPaint); // 画方
//        mPaint.setXfermode(xfermode); // 设置 Xfermode
//        canvas.drawBitmap(circleBitmap, 0, 0, mPaint); // 画圆
//        mPaint.setXfermode(null); // 用完及时清除 Xfermode
//            canvas.drawBitmap(mBgBitmap,0,0,null);
//            //前景色
//            canvas.drawBitmap(mFgBitmap,0,0,null);

    }
}