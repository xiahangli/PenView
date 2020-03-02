package com.example.penview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.animation.PathInterpolatorCompat;

public class PullView extends View {

    private Paint mCirclePaint;//圆的画笔
    private int mCircleRadius = 20;//圆的半径
    private float mCirclePointX;//圆的xy坐标
    private float mCirclePointY;
    private float mProgress;//进度
    private int mDragHeight = 300;//可拖拽高度
    private int mTargetWidth = 600;//目标宽度
    private Path mPath = new Path();//贝塞尔曲线
    private Paint mPathPaint;
    private int mTargetGravityHeight = 10;//重心点最终高度，决定控制点的Y坐标
    private int mTangentAngle = 100;//角度变换 0-135
    private Interpolator mProgressInterpolator = new DecelerateInterpolator();//进度插值器
    private Interpolator mTangentAngleInterpolator;//切角路径插值器
    private Drawable mContent = null;//圆圈内部的圈
    private int mContentMargin = 10;//圈的边距
    private ValueAnimator valueAnimator;//释放动画

    public PullView(Context context) {
        this(context, null);
    }

    public PullView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);//抗锯齿
        mCirclePaint.setDither(true);//防抖动
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);//填充方式
        mCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//初始化路径部分画笔
        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        mTangentAngleInterpolator = PathInterpolatorCompat.create((mCircleRadius * 2.0f) / mDragHeight,
                90.0f / mTangentAngle
        );

        mContent = getResources().getDrawable(R.drawable.circle_drawable);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updatePathLayout();
    }

    private void updatePathLayout() {
        final float progress = mProgressInterpolator.getInterpolation(mProgress);
        //获取可绘制区域高度宽度
        final float w = getValueByLine(getWidth(), mTargetWidth, mProgress);
        final float h = getValueByLine(0, mDragHeight, mProgress);
        //X对称轴的参数，圆的圆心坐标，半径等
        final float cPointX = w / 2;
        final float cRadius = mCircleRadius;
        final float cPointY = h - cRadius;
        //控制点结束Y坐标
        final float endControlY = mTargetGravityHeight;
        mCirclePointX = cPointX;
        mCirclePointY = cPointY;

        final Path path = mPath;
        //重置
        path.reset();
        path.moveTo(0, 0);
        //左边部分的结束点和控制点
        float lEndPointX, lEndPointY;
        float lControlPointX, lControlPointY;
        //角度转弧度
        float angle = mTangentAngle * mTangentAngleInterpolator.getInterpolation(progress);
        double radian = Math.toRadians(angle);
        float x = (float) (Math.sin(radian) * cRadius);
        float y = (float) (Math.cos(radian) * cRadius);

        lEndPointX = cPointX - x;
        lEndPointY = cPointY + y;

        //控制点y坐标变化
        lControlPointY = getValueByLine(0, endControlY, progress);
        //控制点与结束定之前的高度
        float tHeight = lEndPointY - lControlPointY;
        //控制点与x坐标的距离
        float tWidth = (float) (tHeight / Math.tan(radian));
        lControlPointX = lEndPointX - tWidth;
        //左边贝塞尔曲线
        path.quadTo(lControlPointX, lControlPointY, lEndPointX, lEndPointY);
        //连接到右边
        path.lineTo(cPointX + (cPointX - lEndPointX), lEndPointY);
        //右边贝塞尔曲线
        ////        w-(lEndPointX-(float)(tHeight/Math.tan(radian)))
        path.quadTo(cPointX + cPointX - lControlPointX, lControlPointY, w, 0);
        //更新内容部分Drawable
        updateContentLayout(cPointX, cPointY, cRadius);
    }

    /**
     * 对内容部分进行测量并设置
     *
     * @param cx     cPointX
     * @param cy     cPointY
     * @param radius cRadius
     */
    private void updateContentLayout(float cx, float cy, float radius) {
        Drawable drawable = mContent;
        if (drawable != null) {
            int margin = mContentMargin;
            int l = (int) (cx - radius + margin);
            int r = (int) (cx + radius - margin);
            int t = (int) (cy - radius + margin);
            int b = (int) (cy + radius - margin);
            drawable.setBounds(l, t, r, b);
        }
    }

    /**
     * 获取当前值
     *
     * @param start    起点
     * @param end      终点
     * @param progress 进度
     * @return 某一个坐标差值的百分百，计算贝塞尔的关键
     */
    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int iHeight = (int) ((mDragHeight * mProgress) + getPaddingTop() + getPaddingBottom());
        int iWidth = 2 * mCircleRadius + getPaddingLeft() + getPaddingRight();
        int measureWidth, measureHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measureWidth = Math.min(iWidth, width);
        } else {
            measureWidth = iWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = height;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measureHeight = Math.min(iHeight, height);
        } else {
            measureHeight = iHeight;
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = canvas.save();
        System.out.println("======"+count);
        float tranX = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress)) / 2;
        canvas.translate(tranX, 0);
        canvas.drawPath(mPath, mPathPaint);
        //画圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mCirclePaint);
        Drawable drawable = mContent;
        if (drawable != null) {
            canvas.save();
            //剪切矩形区域
            canvas.clipRect(drawable.getBounds());
            //绘制,这里是绘制圆形球状区域
            drawable.draw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(float progress) {
        mProgress = progress;
        requestLayout();
    }

    /**
     * 添加释放动作
     */
    public void release() {
        if (valueAnimator == null) {
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, 0f);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(400);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object val = animation.getAnimatedValue();
                    if (val instanceof Float) {
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mProgress, 0f);
        }
        valueAnimator.start();
    }
}
