package com.example.penview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author Henry
 * @Date 2020-02-03  23:07
 * @Email 2427417167@qq.com
 */
public class BBar extends ProgressBar {


    public BBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(20,1000);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.rotate(-90);
        canvas.translate(getHeight(),+1f);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldh, oldw);

    }
}
