package com.example.penview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.ImageReader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ScreenshotLayout extends RelativeLayout {
    // 以下表示截屏边框拖动的方位
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 3;
    public static final int BOTTOM = 4;
    public static final int LEFT_TOP = 5;
    public static final int RIGHT_TOP = 6;
    public static final int LEFT_BOTTOM = 7;
    public static final int RIGHT_BOTTOM = 8;
    // 表示截屏框平移
    public static final int TRANSLATION = 0;
    /**
     * 截图框可识别拖动的范围
     */
    private static final int AVAILABLE_AREA = 30;  //30，具体有效区域待定

    /**
     * 带透明款的背景图
     */
    private ScreenshotDrawable background;
    private Paint mPaint = new Paint();

    private int mWindowWidth;
    private int mWindowHeight;
    private WindowManager mWindowManager;
    /**
     * 截图菜单的布局
     */
    private ViewGroup mMenuLayout;
    private Button mBtnScreenSize;
    /**
     * 截屏是否截全屏
     */
    public boolean mIsFullScreen = false;
    /**
     * 是否隐藏辅助圆点
     */
    private boolean isHideDot = false;
    /**
     * 截屏菜单布局参数
     */
    private WindowManager.LayoutParams mMenuLayoutParams;
    /**
     * 用于获取图片信息
     */
    private ImageReader mImageReader;
    /**
     * ScreenshotService，用于直接在此关闭Service
     */
    // 以下为透明边框的坐标
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;
    // 以下为触控点的坐标
    private int mLastX;
    private int mLastY;
    /**
     * 拖动的方向
     */
    private int mDirection = TRANSLATION;

    public ScreenshotLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ScreenshotLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScreenshotLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化界面
     * @param context 上下文
     */
    private void initView(@NonNull Context context) {
        background = new ScreenshotDrawable(getBackground());
        setBackground(background);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (mWindowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
            mWindowWidth = displayMetrics.widthPixels;
            mWindowHeight = displayMetrics.heightPixels;
            mLeft = mWindowWidth / 4;
            mTop = mWindowHeight / 4;
            mRight = mWindowWidth * 3 / 4;
            mBottom = mWindowHeight * 3 / 4;
        }
    }

    /**
     * 隐藏辅助圆点
     */
    public void hideDot() {
        isHideDot = true;
    }

    /**
     * 设置是否全屏
     * @param isFullScreen 是否全屏
     * @param refresh 设置后是否刷新
     */
    public void setFullScreen(boolean isFullScreen, boolean refresh) {
        mIsFullScreen = isFullScreen;
        if (isFullScreen) {
            Path path = new Path();
            // 矩形透明区域
            path.addRect(0, 0, mWindowWidth, mWindowHeight, Path.Direction.CW);
            background.setSrcPath(path);
        } else {
            resetBackgroundHoleArea();
        }
        if (refresh) {
            invalidate();
        }
    }

    /**
     * 设置截屏框区域
     * @param rect 区域
     */
    public void setRect(Rect rect) {
        mLeft = rect.left;
        mTop = rect.top;
        mRight = rect.right;
        mBottom = rect.bottom;
        resetBackgroundHoleArea();
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        resetBackgroundHoleArea();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isHideDot) {
            return;
        }
        if (mIsFullScreen) {
            return;
        }
        int radius = 8;
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mLeft, mTop, radius, mPaint);
        canvas.drawCircle(mLeft, (mBottom + mTop) / 2, radius, mPaint);
        canvas.drawCircle(mLeft, mBottom, radius, mPaint);
        canvas.drawCircle((mLeft + mRight) / 2, mTop, radius, mPaint);
        canvas.drawCircle((mLeft + mRight) / 2, mBottom, radius, mPaint);
        canvas.drawCircle(mRight, mTop, radius, mPaint);
        canvas.drawCircle(mRight, (mBottom + mTop) / 2, radius, mPaint);
        canvas.drawCircle(mRight, mBottom, radius, mPaint);
    }

    /**
     * 重置矩形透明区域
     */
    private void resetBackgroundHoleArea() {
        Path path = new Path();
        path.addRect(mLeft, mTop, mRight, mBottom, Path.Direction.CW);
        background.setSrcPath(path);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsFullScreen) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = (int) event.getRawX();
            mLastY = (int) event.getRawY();
            mDirection = getDirection(mLastX, mLastY);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mLastX = (int) event.getRawX();
            mLastY = (int) event.getRawY();
            mDirection = TRANSLATION;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (event.getRawX() - mLastX);
            int dy = (int) (event.getRawY() - mLastY);
            if (mLeft + dx < 0 || mTop + dy < 0 || mRight + dx > mWindowWidth || mBottom + dy > mWindowHeight || mRight - mLeft < 200 || mBottom - mTop < 200) {
                return false;
            }
            if (mDirection == TRANSLATION) {
                mLeft += dx;
                mTop += dy;
                mRight += dx;
                mBottom += dy;
            } else {
                switch (mDirection) {
                    case LEFT:
                        mLeft += dx;
                        break;
                    case TOP:
                        mTop += dy;
                        break;
                    case RIGHT:
                        mRight += dx;
                        break;
                    case BOTTOM:
                        mBottom += dy;
                        break;
                    case LEFT_TOP:
                        mLeft += dx;
                        mTop += dy;
                        break;
                    case LEFT_BOTTOM:
                        mLeft += dx;
                        mBottom += dy;
                        break;
                    case RIGHT_TOP:
                        mRight += dx;
                        mTop += dy;
                        break;
                    case RIGHT_BOTTOM:
                        mRight += dx;
                        mBottom += dy;
                        break;
                }
            }
            mLastX = (int) event.getRawX();
            mLastY = (int) event.getRawY();

            resetBackgroundHoleArea();

            if (mDirection == TRANSLATION || mDirection == LEFT_BOTTOM || mDirection == RIGHT_BOTTOM) {
                if (mMenuLayoutParams.x + dx <= (mRight - mMenuLayout.getMeasuredWidth())) {
                    mMenuLayoutParams.x += dx;
                }
                if (mMenuLayoutParams.x < mLeft || mRight - mLeft > mMenuLayout.getMeasuredWidth()) {
                    mMenuLayoutParams.x = mRight - mMenuLayout.getMeasuredWidth();
                }
                mMenuLayoutParams.y += dy;
                mWindowManager.updateViewLayout(mMenuLayout, mMenuLayoutParams);
            } else if (mDirection == RIGHT || mDirection == RIGHT_TOP) {
                mMenuLayoutParams.x += dx;
                mWindowManager.updateViewLayout(mMenuLayout, mMenuLayoutParams);
            } else if (mDirection == BOTTOM) {
                mMenuLayoutParams.y += dy;
                mWindowManager.updateViewLayout(mMenuLayout, mMenuLayoutParams);
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取截屏区域拖动的方向
     *
     * @param x 触控点的x坐标
     * @param y 触控点的y坐标
     * @return 方向
     */
    private int getDirection(int x, int y) {
        int left = Math.abs(mLeft - x);
        int right = Math.abs(mRight - x);
        int top = Math.abs(mTop - y);
        int bottom = Math.abs(mBottom - y);
        if (left < AVAILABLE_AREA && top < AVAILABLE_AREA) {
            return LEFT_TOP;
        }
        if (right < AVAILABLE_AREA && top < AVAILABLE_AREA) {
            return RIGHT_TOP;
        }
        if (left < AVAILABLE_AREA && bottom < AVAILABLE_AREA) {
            return LEFT_BOTTOM;
        }
        if (right < AVAILABLE_AREA && bottom < AVAILABLE_AREA) {
            return RIGHT_BOTTOM;
        }
        if (left < AVAILABLE_AREA) {
            return LEFT;
        }
        if (top < AVAILABLE_AREA) {
            return TOP;
        }
        if (right < AVAILABLE_AREA) {
            return RIGHT;
        }
        if (bottom < AVAILABLE_AREA) {
            return BOTTOM;
        }
        return TRANSLATION;
    }

}
