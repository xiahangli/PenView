package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * create by henry on 2020/3/11
 */

public class MatrixView extends View {
    private static double OFFSET;
    private int rotateBitmapWidth;
    private int judgeBitmapHeight;
    private int rotateBitmapHeight;
    private int judgeBitmapWidth;
    private Bitmap rotateBitmap;
    private Bitmap rotateButtonBitmap;
    Paint paint;
    private Bitmap bitmap;
    private Bitmap judgeBitmap;
    private Bitmap mBitmap;
    private Matrix mBMatrix = new Matrix();
    private float lTopX;
    private float lTopY;
    private float rTopX;
    private float rTopY;
    private float lBottomX;
    private float lBottomY;
    private float rBottomX;
    private float rBottomY;
    private float mStartx;
    private float mStartY;
    private Path mRectPath = new Path();
    private float centerX;
    private float centerY;
    private Matrix mCMatrix = new Matrix();
    private RectF judgeRightTop = new RectF();
    private RectF judgeRightBottom = new RectF();
    private RectF judgeLeftTop = new RectF();
    private RectF judgeLeftBottom = new RectF();
    private RectF rectRotate = new RectF();
    private Path mSelectPath = new Path();
    private Paint dashPaint = new Paint();
    private int screenWidth = SCREEN_WITH;
    private int screenHeight = SCREEN_HEIGHT;
    public static int SCREEN_HEIGHT = 1092;
    public static int SCREEN_WITH = 1920;
    private int scaledBitmapWidth;
    private int scaledBitmapHeight;
    private float[] mBMatrixata = new float[9];
    private float[] mCMatrixata = new float[9];
    private ArrayList<PointF> pointList;
    private float origRotateIconX;
    private float origRotateIconY;
    private Matrix mMatrix = new Matrix();
    private double lastegrees;
    ;

    public MatrixView(Context context) {
        super(context);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
//        new Handler().postelayed(new Runnable() {
//            @Override
//            public void run() {
////                screenWidth = getWidth();
////                screenHeight = getHeight();
////                invalidate();
//            }
//        }, 300);
//        rotateButtonBitmap = BitmapUtil.decodeSampledBitmapFromRes(getContext(), R.mipmap.rotate, 100, 100);
        if (this.judgeBitmap == null) {
            this.judgeBitmap = drawableToBitmap(ContextCompat.getDrawable(getContext(), R.drawable.icon_delete_press));
            this.rotateBitmap = BitmapUtil.decodeSampledBitmapFromRes(getContext(), R.mipmap.rotate, 100, 100);
            this.judgeBitmapWidth = (int) (this.judgeBitmap.getWidth() * 0.7F);
            this.judgeBitmapHeight = (int) (this.judgeBitmap.getHeight() * 0.7F);
            this.rotateBitmapWidth = (int) (this.rotateBitmap.getWidth() * 0.7F);
            this.rotateBitmapHeight = (int) (this.rotateBitmap.getHeight() * 0.7F);
        }
        mSelectPath = new Path();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap);
        scaleAndTranslate(false);
//        mBMatrix.postRotate(10, screenWidth / 2, screenHeight / 2);
    }

    public static Bitmap drawableToBitmap(Drawable paramrawable) {
        Bitmap.Config config;
        int i = paramrawable.getIntrinsicWidth();
        int j = paramrawable.getIntrinsicHeight();
        if (paramrawable.getOpacity() != PixelFormat.OPAQUE) {
            config = Bitmap.Config.ARGB_8888;
        } else {
            config = Bitmap.Config.RGB_565;
        }
        Bitmap bitmap = Bitmap.createBitmap(i, j, config);
        Canvas canvas = new Canvas(bitmap);
        paramrawable.setBounds(0, 0, i, j);
        paramrawable.draw(canvas);
        return bitmap;
    }


    private static final String TAG = "MatrixView";

    public static class BitmapUtil {
        public static Bitmap decodeSampledBitmapFromRes(Context paramContext, int paramInt1, int paramInt2, int paramInt3) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(paramContext.getResources(), paramInt1);
            options.inSampleSize = calculateInSampleSize(options, paramInt2, paramInt3, true);
            options.inJustDecodeBounds = false;
            return createScaleBitmap(BitmapFactory.decodeResource(paramContext.getResources(), paramInt1), paramInt2, paramInt3, options.inSampleSize);
        }

        public static Bitmap decodeBitmapFromFile(String paramString) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(paramString, options);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(paramString, options);
        }


        private static int calculateInSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2, boolean paramBoolean) {
            int j = paramOptions.outHeight;
            int i = paramOptions.outWidth;
            if (j > paramInt2 || i > paramInt1) {
                paramInt2 = Math.round(j / paramInt2);
                paramInt1 = Math.round(i / paramInt1);
                return (paramBoolean ? (paramInt2 > paramInt1) : (paramInt2 < paramInt1)) ? paramInt2 : paramInt1;
            }
            return 1;
        }

        private static Bitmap createScaleBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3) {
            Bitmap bitmap = Bitmap.createScaledBitmap(paramBitmap, paramInt1, paramInt2, true);
            if (paramBitmap != bitmap)
                paramBitmap.recycle();
            return bitmap;
        }


        public static Bitmap drawableToBitmap(Drawable paramrawable) {
            if (paramrawable instanceof BitmapDrawable)
                return ((BitmapDrawable) paramrawable).getBitmap();
            if (paramrawable != null) {
                Bitmap.Config config;
                int i = paramrawable.getIntrinsicWidth();
                int j = paramrawable.getIntrinsicHeight();
                if (paramrawable.getOpacity() != PixelFormat.OPAQUE) {
                    config = Bitmap.Config.ARGB_8888;
                } else {
                    config = Bitmap.Config.RGB_565;
                }
                Bitmap bitmap = Bitmap.createBitmap(i, j, config);
                Canvas canvas = new Canvas(bitmap);
                paramrawable.setBounds(0, 0, i, j);
                paramrawable.draw(canvas);
                return bitmap;
            }
            return null;
        }

        public static Bitmap getstArea(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap.Config paramConfig) {
            Bitmap bitmap = Bitmap.createBitmap(paramInt1, paramInt2, paramConfig);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(paramInt3, paramInt4);
            canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
            return bitmap;
        }

        public static void releaseResource(Bitmap paramBitmap) {
            if (paramBitmap != null && !paramBitmap.isRecycled())
                paramBitmap.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.translate(300, 300);
//        Path path = new Path();
//        path.addCircle(100, 100, 100, Path.irection.CCW);
//        canvas.drawPath(path, paint);

//        canvas.drawLine(20,0,20,100,paint);
//        canvas.save();
//        canvas.scale(2, 2);//坐标系放大2倍
//        paint.setColor(Color.GREEN);
//        Matrix matrix = new Matrix();
//        RectF rectF = new RectF();
//        path.computeBounds(rectF, true);
//        Log.e(TAG, "onraw: " + rectF.toString());

//        matrix.postScale(2,2);
//        matrix.postRotate(20,bitmap.getWidth()/2,bitmap.getHeight()/2);

//        float[] flo = new float[9];
////        matrix.getValues(flo);
//        canvas.drawLine(20,0,20,100,paint);
//        Log.e(TAG, "onraw: " + flo[0]);
//        canvas.drawBitmap(bitmap,matrix,null);
//        canvas.restore();
//        drawPrivateShape(canvas);
//        mBMatrix.postRotate(10,screenWidth/2,screenHeight/2);
//        mBMatrix.postScale(2,2,screenWidth/2,screenHeight/2);
//        this.mCMatrix.postRotate(paramFloat1, paramFloat2, paramFloat3);
//        this.mBMatrix.postRotate(paramFloat1, paramFloat2, paramFloat3);
//        drawPrivateShape(canvas);
//        mBMatrix.postRotate(10,screenWidth/2,screenHeight/2);
//        drawPrivateShape(canvas);

        drawPrivateShape(canvas);
//        m.postRotate(1, 100, 100);

//        path.transform(m);
//        float[] src = new float[]{0, 0, 200, 0, 200, 200, 0, 200};
//        float[] src1 = new float[]{0, 200, 200, 0, 0, 0, 200, 200};
//        float[] dst = new float[8];
////        m.getValues();
//        for (int i = 0; i < 8; i++) {
//            Log.e(TAG, "onraw: " + dst[i] );
//        }
//        path.computeBounds(rectF, true);
//        Log.e(TAG, "onraw: " + rectF.toString());

    }

    public void generateBitmapFile(String paramString) {
        Log.i(TAG, "generateBitmapFile");
//        this.mCMatrix.getValues(this.mCMatrixata);
//        this.mBMatrix.getValues(this.mBMatrixata);
        paramString = TAG;
    }

    private void scaleSelectBoxMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        Matrix mMatrix = new Matrix();
        mMatrix.setScale(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        mMatrix.mapPoints(this.selectCornersSrc);
        this.centerX = this.selectCornersSrc[8];
        this.centerY = this.selectCornersSrc[9];
        PointF pointF = getNearestPoint(new PointF(this.origRotateIconX, this.origRotateIconY));
        this.origRotateIconX += pointF.x - this.oldNearestPoint.x;
        this.origRotateIconY += pointF.y - this.oldNearestPoint.y;
    }

    private PointF oldNearestPoint;

    private double spacing(PointF paramPointF1, PointF paramPointF2) {
        double f1 = paramPointF1.x - paramPointF2.x;
        double f2 = paramPointF1.y - paramPointF2.y;
        return Math.sqrt((f1 * f1 + f2 * f2));
    }

    private PointF getNearestPoint(PointF paramPointF) {
        ArrayList arrayList = getPointList();
        byte b2 = 0;
        double f = spacing(paramPointF, (PointF) arrayList.get(0));
        byte b1 = 1;
        while (b1 < arrayList.size()) {
            double f2 = spacing(paramPointF, (PointF) arrayList.get(b1));
            double f1 = f;
            if (f > f2) {
                b2 = b1;
                f1 = f2;
            }
            b1++;
            f = f1;
        }
        return (PointF) arrayList.get(b2);
    }

    private float[] selectCornersSrc;

    private ArrayList<PointF> getPointList() {
        this.pointList.clear();
        PointF pointF1 = new PointF(this.selectCornersSrc[0], this.selectCornersSrc[1]);
        PointF pointF2 = new PointF(this.selectCornersSrc[2], this.selectCornersSrc[3]);
        PointF pointF3 = new PointF(this.selectCornersSrc[4], this.selectCornersSrc[5]);
        PointF pointF4 = new PointF(this.selectCornersSrc[6], this.selectCornersSrc[7]);
        this.pointList.add(pointF1);
        this.pointList.add(pointF2);
        this.pointList.add(pointF3);
        this.pointList.add(pointF4);
        return this.pointList;
    }

    private float[] getCornersSrc(RectF paramRectF) {
        return new float[]{paramRectF.left, paramRectF.top, paramRectF.right, paramRectF.top, paramRectF.right, paramRectF.bottom, paramRectF.left, paramRectF.bottom, paramRectF.centerX(), paramRectF.centerY()};
    }

    private RectF selectRectF;

    private void translateToCornersSrc() {
        Log.d(TAG, "translateToCornersSrc");
        RectF rectF = new RectF();
        this.selectRectF.left -= 21.0F;
        this.selectRectF.top -= 21.0F;
        this.selectRectF.bottom += 21.0F;
        this.selectRectF.right += 21.0F;
        this.selectCornersSrc = getCornersSrc(rectF);
        float[] arrayOfFloat = this.selectCornersSrc;
        float f = this.selectRectF.centerX();
        arrayOfFloat[8] = f;
        this.centerX = f;
        arrayOfFloat = this.selectCornersSrc;
        f = this.selectRectF.centerY();
        arrayOfFloat[9] = f;
        this.centerY = f;
        this.origRotateIconX = this.selectCornersSrc[4] + 10.0F + 32.0F;
        this.origRotateIconY = this.selectCornersSrc[5] + 10.0F + 32.0F;
    }

    private void rotateSelectRectFMatrix(float paramFloat) {
        this.mMatrix.reset();
        this.mMatrix.postRotate(paramFloat, this.centerX, this.centerY);
        this.mMatrix.mapPoints(this.selectCornersSrc);
        this.mMatrix.reset();
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = this.origRotateIconX;
        arrayOfFloat[1] = this.origRotateIconY;
        this.mMatrix.postRotate(paramFloat, this.centerX, this.centerY);
        this.mMatrix.mapPoints(arrayOfFloat);
        this.origRotateIconX = arrayOfFloat[0];
        this.origRotateIconY = arrayOfFloat[1];
    }


    private void scaleAndTranslate(boolean paramBoolean) {
        int i = this.mBitmap.getWidth();
        int j = this.mBitmap.getHeight();
        if (!paramBoolean) {
            float f = Math.min((this.screenWidth / 2) * 1.0F / i, (this.screenHeight / 2) * 1.0F / j);
//            this.mBitmap = scaleBitmap(this.mBitmap, f);
        }
        this.scaledBitmapWidth = this.mBitmap.getWidth();
        this.scaledBitmapHeight = this.mBitmap.getHeight();
        Point point1 = new Point((this.screenWidth / 2), (this.screenHeight / 2));
        Point point2 = new Point((this.scaledBitmapWidth / 2), (this.scaledBitmapHeight / 2));


        Matrix matrix = new Matrix();
        matrix.postTranslate(point1.x - point2.x, point1.y - point2.y);


//        matrix.postScale(1.0F, 1.0F, point1.x, point1.y);
        this.mBMatrix.postConcat(matrix);
        this.mBMatrix.getValues(this.mBMatrixata);
        this.mCMatrix.postConcat(matrix);
        this.mCMatrix.getValues(this.mCMatrixata);

        float origincenter = mBitmap.getWidth() / 2;
        float origincentery = mBitmap.getHeight() / 2;
        float[] dst = new float[2];
        mBMatrix.mapPoints(dst, new float[]{
                origincenter, origincentery
        });
        centerX = dst[0];
        centerY = dst[1];
    }


    public void drawPrivateShape(Canvas paramCanvas) {

        OFFSET = rotateBitmapHeight / 2D + 20D;


        this.judgeBitmapWidth = (int) (this.judgeBitmap.getWidth() * 0.7F);
        this.judgeBitmapHeight = (int) (this.judgeBitmap.getHeight() * 0.7F);
        this.rotateBitmapWidth = (int) (this.rotateBitmap.getWidth() * 0.7F);
        this.rotateBitmapHeight = (int) (this.rotateBitmap.getHeight() * 0.7F);

        if (paramCanvas != null && this.mBitmap != null && !this.mBitmap.isRecycled()) {
//            this.mCanvas = paramCanvas;
            float[] arrayOfFloat = new float[9];
            this.mBMatrix.getValues(arrayOfFloat);
            this.lTopX = arrayOfFloat[0] * 0.0F + arrayOfFloat[1] * 0.0F + arrayOfFloat[2];
            this.lTopY = arrayOfFloat[3] * 0.0F + arrayOfFloat[4] * 0.0F + arrayOfFloat[5];
            this.rTopX = this.mBitmap.getWidth() * arrayOfFloat[0] + arrayOfFloat[1] * 0.0F + arrayOfFloat[2];
            this.rTopY = this.mBitmap.getWidth() * arrayOfFloat[3] + arrayOfFloat[4] * 0.0F + arrayOfFloat[5];
            this.lBottomX = arrayOfFloat[0] * 0.0F + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            this.lBottomY = arrayOfFloat[3] * 0.0F + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            this.rBottomX = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            this.rBottomY = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            this.mStartx = this.lTopX;
            this.mStartY = this.lTopY;
            this.mRectPath.reset();
            this.mRectPath.moveTo(this.lTopX, this.lTopY);
            this.mRectPath.lineTo(this.rTopX, this.rTopY);
            this.mRectPath.lineTo(this.rBottomX, this.rBottomY);
            this.mRectPath.lineTo(this.lBottomX, this.lBottomY);
            this.mRectPath.close();
//            this.centerX = this.lTopX + (this.rBottomX - this.lTopX) / 2.0F - (InsertImageToolWindow.instance().getWidth(WhiteBoardApplication.mContext) / 2);
//            this.centerY = this.lTopY + (this.rBottomY - this.lTopY) / 2.0F;
            paramCanvas.save();
            paramCanvas.drawBitmap(this.mBitmap, this.mBMatrix, null);
            this.mCMatrix.reset();
//            this.mCMatrix.postConcat(this.mBMatrix);
            this.judgeRightTop.left = (int) (this.rTopX - (this.judgeBitmapWidth / 2));
            this.judgeRightTop.right = (int) (this.rTopX + (this.judgeBitmapWidth / 2));
            this.judgeRightTop.top = (int) (this.rTopY - (this.judgeBitmapHeight / 2));
            this.judgeRightTop.bottom = (int) (this.rTopY + (this.judgeBitmapHeight / 2));
            this.judgeRightBottom.left = (int) (this.rBottomX - (this.judgeBitmapWidth / 2));
            this.judgeRightBottom.right = (int) (this.rBottomX + (this.judgeBitmapWidth / 2));
            this.judgeRightBottom.top = (int) (this.rBottomY - (this.judgeBitmapHeight / 2));
            this.judgeRightBottom.bottom = (int) (this.rBottomY + (this.judgeBitmapHeight / 2));
            this.judgeLeftTop.left = (int) (this.lTopX - (this.judgeBitmapWidth / 2));
            this.judgeLeftTop.right = (int) (this.lTopX + (this.judgeBitmapWidth / 2));
            this.judgeLeftTop.top = (int) (this.lTopY - (this.judgeBitmapHeight / 2));
            this.judgeLeftTop.bottom = (int) (this.lTopY + (this.judgeBitmapHeight / 2));
            this.judgeLeftBottom.left = (int) (this.lBottomX - (this.judgeBitmapWidth / 2));
            this.judgeLeftBottom.right = (int) (this.lBottomX + (this.judgeBitmapWidth / 2));
            this.judgeLeftBottom.top = (int) (this.lBottomY - (this.judgeBitmapHeight / 2));
            this.judgeLeftBottom.bottom = (int) (this.lBottomY + (this.judgeBitmapHeight / 2));

            double theta = Math.atan2((this.rTopY - this.lTopY), (this.rTopX - this.lTopX)) * 180.0 / Math.PI;

            //
            float f1 = this.lTopX + (this.rTopX - this.lTopX) / 2.0F;
            float f2 = this.lTopY + (this.rTopY - this.lTopY) / 2.0F;
//            Log.e(TAG, "drawPrivateShape: length" + Math.hypot(f1 - lTopX, f2 - lTopY));
            double d1 = 0.0;//x
            double d2 = 0.0;//y
            double i = theta - 0.0;
            if (theta == 0) {
                d1 = f1;
                d2 = (f2 - OFFSET);
                Log.e(TAG, "drawPrivateShape: theta == 0");
            } else if (theta > 0 && theta < 90.0) {
                d2 = f1;
                //theta = 90.0 - theta;
                d1 = f1 + Math.sin(Math.toRadians(theta)) * OFFSET;
                d2 = f2 - Math.cos(Math.toRadians(theta)) * OFFSET;
                Log.e(TAG, "drawPrivateShape: theta > 0 && theta < 90.0");
            } else if (theta == 90.0) {
                d2 = (f1 + OFFSET);
                double d = f2;
                theta = Math.sin(Math.toRadians(90.0 - theta)) * OFFSET + d;
            } else if (theta > 90.0 && theta < 180.0) {
                d2 = f1;
                theta -= 90.0;
                theta = 90 - theta;
                Log.e(TAG, "drawPrivateShape: theta > 90.0 && theta < 180.0");
                d1 = f1 + Math.sin(Math.toRadians(theta)) * OFFSET;
                d2 = f2 + Math.cos(Math.toRadians(theta)) * OFFSET;
                double d = f2;
            } else if (theta == 180.0) {// TODO: 2020-03-22
                d2 = f1;
                theta = 180.0 - theta;
                d2 -= Math.cos(Math.toRadians(theta)) * OFFSET;
                theta = f2 + Math.sin(Math.toRadians(theta)) * OFFSET;
            } else if (theta > -180.0 && theta < -90.0) {//[-pi/2,pi]
                d2 = f1;
                theta = -theta - 90.0;
//                    double d = f2;
                d1 = f1 - Math.cos(Math.toRadians(theta)) * OFFSET;
                d2 = f2 + Math.sin(Math.toRadians(theta)) * OFFSET;
                Log.e(TAG, "drawPrivateShape: theta > -180.0 && theta < -90.0");
            } else if (theta == -90.0) {
                d2 = f1;
                theta = -theta;
                d2 -= Math.cos(Math.toRadians(theta)) * OFFSET;
                double d = f2;
                theta = Math.sin(Math.toRadians(theta)) * OFFSET + d;
            } else if (theta > -90.0 && theta < 0.0) {//
//                d2 = f1;
//                theta += 90.0;
//                Log.e(TAG, "drawPrivateShape: theta > -90 && theta < 0.0");
//                d2 -= Math.cos(Math.toRadians(theta)) * OFFSET;
//                theta = f2 - Math.sin(Math.toRadians(theta)) * OFFSET;

                theta = -theta;
                d1 = f1 - Math.sin(Math.toRadians(theta)) * OFFSET;
                d2 = f2 - Math.cos(Math.toRadians(theta)) * OFFSET;
            } else {
                theta = 0.0;
            }
            this.rectRotate.left = (int) (d1 - (this.rotateBitmapWidth / 2));
            this.rectRotate.right = (int) (d1 + (this.rotateBitmapWidth / 2));
            this.rectRotate.top = (int) (d2 - (this.rotateBitmapHeight * 0.5F));
            this.rectRotate.bottom = (int) (d2 + (this.rotateBitmapHeight * 0.5F));

            if (true) {
                this.mSelectPath.moveTo(this.lTopX, this.lTopY);
                this.mSelectPath.lineTo(this.rTopX, this.rTopY);
                this.mSelectPath.lineTo(this.rBottomX, this.rBottomY);
                this.mSelectPath.lineTo(this.lBottomX, this.lBottomY);
                this.mSelectPath.close();
                initashPaint();
                paramCanvas.drawPath(this.mSelectPath, this.dashPaint);
                this.mSelectPath.reset();
                this.dashPaint = null;
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(": onraw()   :selectedShapesSize=");
                stringBuilder.append(",");
                stringBuilder.append(this);
                Log.d(str, stringBuilder.toString());
                if (true) {
                    paramCanvas.drawBitmap(this.judgeBitmap, null, this.judgeLeftTop, null);
                    paramCanvas.drawBitmap(this.judgeBitmap, null, this.judgeLeftBottom, null);
                    paramCanvas.drawBitmap(this.judgeBitmap, null, this.judgeRightTop, null);
                    paramCanvas.drawBitmap(this.judgeBitmap, null, this.judgeRightBottom, null);
                    paramCanvas.drawBitmap(this.rotateBitmap, null, this.rectRotate, null);
                } else {
                }
            }
            paramCanvas.restore();
        }

    }

    boolean inRotateMode = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                inRotateMode = isInRotateMode(event.getX(), event.getY());
//                mBMatrix.set((float) (degrees - lastegrees), screenWidth / 2, screenHeight / 2);
                break;
            case MotionEvent.ACTION_MOVE:
                if (inRotateMode) {
                    double x = event.getX();
                    double y = event.getY();
                    double dx = x - centerX;
                    double dy = y - centerY;
                    double radian = Math.atan(dy / dx);
                    double degrees = Math.toDegrees(radian);
                    degrees = (radian / (Math.PI * 2) * 360);
                    if (dx > 0 && dy > 0) {
                        degrees += 90;
                    } else if (dx > 0 && dy < 0) {
                        degrees = 90 + degrees;
                    } else if (dx < 0 && dy > 0) {
                        degrees += 270;
                    } else if (dx < 0 && dy < 0) {
                        degrees += 270;//或 degrees = - (90-degrees)
                    }
//                    if (degrees - lastegrees>10){
                    mBMatrix.postRotate((float) (degrees - lastegrees), screenWidth / 2, screenHeight / 2);
                    lastegrees = degrees;
//                    }
                    Log.e(TAG, "onTouchEvent:d= " + degrees + "delta=" + ((degrees - lastegrees)) + ",is =" + inRotateMode);
                }
                break;
        }
        invalidate();

        return true;

    }

    //    public boolean isInRegion(float paramFloat1, float paramFloat2) { return (this.mShapeRegion != null && this.mShapeRegion.contains((int)paramFloat1, (int)paramFloat2)); }

    public boolean isInRotateMode(float paramFloat1, float paramFloat2) {
        return this.rectRotate.contains(paramFloat1, paramFloat2);
    }

    public boolean isInScaleMode(float paramFloat1, float paramFloat2) {
        return (this.judgeLeftTop.contains(paramFloat1, paramFloat2) ||
                this.judgeLeftBottom.contains(paramFloat1, paramFloat2) ||
                this.judgeRightTop.contains(paramFloat1, paramFloat2) ||
                this.judgeRightBottom.contains(paramFloat1, paramFloat2));
    }


    private void initashPaint() {
        this.dashPaint = new Paint();
        this.dashPaint.setStyle(Paint.Style.STROKE);
        this.dashPaint.setStrokeJoin(Paint.Join.ROUND);
        this.dashPaint.setStrokeCap(Paint.Cap.ROUND);
        this.dashPaint.setAntiAlias(true);
        this.dashPaint.setColor(-256);
        this.dashPaint.setStrokeWidth(4.0F);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{0.1F, 8.0F}, 0.0F);
        this.dashPaint.setPathEffect(dashPathEffect);
    }
}
