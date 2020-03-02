package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ScribbleView extends View {
    //View state
    private List<TimedPoint> mPoints;
    private boolean mIsEmpty;
    //    private Boolean mHasEditState;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastVelocity;
    private float mLastWidth;
    //    private RectF mDirtyRect;
//    private Bitmap mBitmapSavedState;
    private Path mPath = new Path();

//    private final SvgBuilder mSvgBuilder = new SvgBuilder();

    // Cache
    //private List<TimedPoint> mPointsCache = new ArrayList<>();
    private ControlTimedPoints mControlTimedPointsCached = new ControlTimedPoints();
    private Bezier mBezierCached = new Bezier();

    //Configurable parameters
    private int mMinWidth;
    private int mMaxWidth;
    private float mVelocityFilterWeight;
    //    private OnSignedListener mOnSignedListener;
    private boolean mClearOnDoubleClick;

    //Click values
//    private long mFirstClick;
//    private int mCountClick;
//    private static final int DOUBLE_CLICK_DELAY_MS = 200;

    //Default attribute values
    private final int DEFAULT_ATTR_PEN_MIN_WIDTH_PX = 3;
    private final int DEFAULT_ATTR_PEN_MAX_WIDTH_PX = 7;
    private final int DEFAULT_ATTR_PEN_COLOR = Color.BLACK;
    private final float DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT = 0.9f;
    private final boolean DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK = false;

    private Paint mPaint = new Paint();
    private Bitmap mSignatureBitmap = null;

    public ScribbleView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.ScribbleView,
//                0, 0);

        //Configurable parameters
        try {
            mMinWidth = convertDpToPx(DEFAULT_ATTR_PEN_MIN_WIDTH_PX);
            mMaxWidth =   convertDpToPx(DEFAULT_ATTR_PEN_MAX_WIDTH_PX);
            mPaint.setColor( DEFAULT_ATTR_PEN_COLOR);
            mVelocityFilterWeight =DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT;
            mClearOnDoubleClick =DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK;
        } finally {
        }

        //Fixed parameters
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        //Dirty rectangle to update only the changed portion of the view
//        mDirtyRect = new RectF();

//        clearView();
        mPoints = new ArrayList<>();
        mLastVelocity = 0;
        mLastWidth = (mMinWidth + mMaxWidth) / 2;

        if (mSignatureBitmap != null) {
            mSignatureBitmap = null;
//            ensureSignatureBitmap();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return false;
        float eventX = event.getX();
        float eventY = event.getY();
        System.out.println("===========x="+event.getX()+",y="+event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPoints.clear();
                mPath.moveTo(eventX, eventY);
                mLastTouchX = eventX;
                mLastTouchY = eventY;
                addPoint(getNewPoint(eventX, eventY));
            case MotionEvent.ACTION_MOVE:
                addPoint(getNewPoint(eventX, eventY));
                break;
            case MotionEvent.ACTION_UP:
                addPoint(getNewPoint(eventX, eventY));
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
            canvas.drawPath(mPath, mPaint);
    }

    private TimedPoint getNewPoint(float x, float y) {
        TimedPoint timedPoint;
        timedPoint = new TimedPoint();
        return timedPoint.set(x, y);
    }

    private void addPoint(TimedPoint newPoint) {
        mPoints.add(newPoint);
        int pointsCount = mPoints.size();
        if (pointsCount > 3) {
            ControlTimedPoints tmp = calculateCurveControlPoints(mPoints.get(0), mPoints.get(1), mPoints.get(2));
            TimedPoint c2 = tmp.c2;
            tmp = calculateCurveControlPoints(mPoints.get(1), mPoints.get(2), mPoints.get(3));
            TimedPoint c3 = tmp.c1;
            Bezier curve = mBezierCached.set(mPoints.get(1), c2, c3, mPoints.get(2));
            TimedPoint startPoint = curve.startPoint;
            TimedPoint endPoint = curve.endPoint;
            float velocity = endPoint.velocityFrom(startPoint);
            velocity = Float.isNaN(velocity) ? 0.0f : velocity;
            velocity = mVelocityFilterWeight * velocity
                    + (1 - mVelocityFilterWeight) * mLastVelocity;
            float newWidth = strokeWidth(velocity);
            addBezier(curve, mLastWidth, newWidth);
            mLastVelocity = velocity;
            mLastWidth = newWidth;
            mPoints.remove(0);
        }
//        else if (pointsCount == 1) {
//            TimedPoint firstPoint = mPoints.get(0);
//            mPoints.add(getNewPoint(firstPoint.x, firstPoint.y));
//        }
    }

    private void addBezier(Bezier curve, float startWidth, float endWidth) {
//        ensureSignatureBitmap();
        float originalWidth = mPaint.getStrokeWidth();//
        float widthDelta = endWidth - startWidth;
        float drawSteps = (float) Math.ceil(curve.length());

        for (int i = 0; i < drawSteps; i++) {
            float t = ((float) i) / drawSteps;
            float tt = t * t;
            float ttt = tt * t;
            float u = 1 - t;
            float uu = u * u;
            float uuu = uu * u;
            float x = uuu * curve.startPoint.x;
            x += 3 * uu * t * curve.control1.x;
            x += 3 * u * tt * curve.control2.x;
            x += ttt * curve.endPoint.x;
            float y = uuu * curve.startPoint.y;
            y += 3 * uu * t * curve.control1.y;
            y += 3 * u * tt * curve.control2.y;
            y += ttt * curve.endPoint.y;
            mPaint.setStrokeWidth(startWidth + ttt * widthDelta);
            mPath.addCircle(x,y,(startWidth + ttt * widthDelta)/2, Path.Direction.CCW);
            //todo 下面这段代码为什么会导致卡顿？？？
//            mSignatureBitmapCanvas.drawPath(mPath, mPaint);
            //todo
//            mSignatureBitmapCanvas.drawPoint(x, y, mPaint);
        }
        mPaint.setStrokeWidth(originalWidth);
    }

    private ControlTimedPoints calculateCurveControlPoints(TimedPoint s1, TimedPoint s2, TimedPoint s3) {
        float dx1 = s1.x - s2.x;
        float dy1 = s1.y - s2.y;
        float dx2 = s2.x - s3.x;
        float dy2 = s2.y - s3.y;

        float m1X = (s1.x + s2.x) / 2.0f;
        float m1Y = (s1.y + s2.y) / 2.0f;
        float m2X = (s2.x + s3.x) / 2.0f;
        float m2Y = (s2.y + s3.y) / 2.0f;

        float l1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
        float l2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

//        float dxm = (m1X - m2X);
//        float dym = (m1Y - m2Y);
        float k = l2 / (l1 + l2);
        if (Float.isNaN(k)) k = 0.0f;
//        float cmX = m2X + dxm * k;
//        float cmY = m2Y + dym * k;

        float tx = s2.x - (m2X + (m1X - m2X) * (l2 / (l1 + l2)));
        float ty = s2.y - (m2Y + (m1Y - m2Y) * (l2 / (l1 + l2)));

        return mControlTimedPointsCached.set(getNewPoint(m1X + tx, m1Y + ty), getNewPoint(m2X + tx, m2Y + ty));
    }

    private float strokeWidth(float velocity) {
        return Math.max(mMaxWidth / (velocity + 1), mMinWidth);
    }


//    private void ensureSignatureBitmap() {
//        if (mSignatureBitmap == null) {
//            mSignatureBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
//                    Bitmap.Config.ARGB_8888);
//        }
//    }

    private int convertDpToPx(float dp) {
        return Math.round(getContext().getResources().getDisplayMetrics().density * dp);
    }
}
