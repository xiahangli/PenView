package com.example.penview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Henry
 * @Date 2020-01-18  18:42
 * @Email 2427417167@qq.com
 */
public class RegionView extends View {
    private Paint mPaint;
//    够着函数需指定大小，否则面积为0
    Region region =new Region(0,0,199,100);
    private Region region1;
    private Region region2;
    Rect  rect1= new Rect(200,200,500,500);
    Rect rect2 = new Rect(300,300,600,600);

    public RegionView(Context context) {
        this(context,null);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }




    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(3);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rect1,mPaint);
        canvas.drawRect(rect2,mPaint);


        //以矩形的方式构造两个region

         region1 = new Region(rect1);
         region2 = new Region(rect2);
//        注意多种模式不要叠加使用
//        region1.op(region2, Region.Op.INTERSECT);//相交
//        region1.op(region2,Region.Op.REPLACE);//显示region2的区域，region2代替region1显示
//        region1.op(region2,Region.Op.REVERSE_DIFFERENCE);//显示region2的区域，region2代替region1显示
//        region1.op(region2,Region.Op.XOR);//排除重叠区域
        //Op操作会音箱到到region
        region1.op(region2,Region.Op.DIFFERENCE);//所有
//        region1.op(region2, Region.Op.DIFFERENCE);//region2的差异化的部分显示
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
//        region.setEmpty();
//        region.set(new Region(0,0,100,100));
//        region.set(new Rect(0,0,100,100));
//        region.translate(20,20,region);
        drawRegion(canvas,region1,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x  = (int) event.getX();
        int  y  = (int) event.getY();
        if (region1.contains(x,y)){
            Log.e("test", "onTouchEvent: contains" );
        }else{
            Log.e("test", "onTouchEvent: not contains" );
        }
        return true;
    }

//    public boolean isInRegion(float x, float y) {
//        return region != null && region.contains((int)x, (int)y);
//    }


    public void setRegion(Path path) {
        Region re = new Region();
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        //region和path相关联
        re.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        this.region = re;
    }


    //迭代绘制region区域
    private void drawRegion(Canvas canvas, Region rgn, Paint paint) {
        RegionIterator iterator =new RegionIterator(rgn);
        Rect rect =new Rect();
        while (iterator.next(rect)){
            canvas.drawRect(rect,paint);
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        Rect rect1 = new Rect(200,200,500,500);
//        Rect rect2 = new Rect(300,300,600,600);
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(2);
//        //绘制二个矩形
//        canvas.drawRect(rect1, paint);
//        canvas.drawRect(rect2, paint);
//        //构造两个Region
//        Region region1 = new Region(rect1);
//        Region region2= new Region(rect2);
//        region1.op(region2, Region.Op.DIFFERENCE);
//        paint.setColor(Color.GREEN);
//        paint.setStyle(Paint.Style.FILL);
//        drawRegion(canvas, region1, paint);
//    }

//    private void drawRegion(Canvas canvas,Region rgn,Paint paint)
//    {
//        RegionIterator iter = new RegionIterator(rgn);
//        Rect r = new Rect();
//        while (iter.next(r)) {
//            canvas.drawRect(r, paint);
//        }
//    }
}
