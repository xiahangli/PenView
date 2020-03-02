package com.example.penview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author Henry
 * @Date 2020-01-18  19:32
 * @Email 2427417167@qq.com
 */
public class PathEffectView extends View {
    private Paint mPaint;
    private Path mPath;
    private PathEffect[] effects = new PathEffect[7];
    public PathEffectView(Context context) {
        this(context, null);

        getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {

            }
        });
    }

    public PathEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);       //绘画风格:空心
        mPaint.setStrokeWidth(5);                  //笔触粗细
        mPaint.setColor(Color.RED);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath = new Path();
        mPath.moveTo(0, 0);
        for (int i = 1; i <= 15; i++) {
            //y随机化
            mPath.lineTo(i*60,(float)Math.random()*300);
        }

        effects[1] = new CornerPathEffect(2);
        effects[2] = new DiscretePathEffect(3,5);
    }

    public PathEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 注意这里平移画布,相当于平移了坐标系
        canvas.translate(50, 50);
        Path path = new Path();
        //添加一个矩形，path的路径反向是逆时针，绘制顺序是左上角，左下，右下，左上
        path.addRect(0,0,8,8,Path.Direction.CCW);
        //设置画笔的patheffect效果
        mPaint.setPathEffect(effects[2]);//设置cornerpath的效果
        canvas.drawPath(mPath,mPaint);
//        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
