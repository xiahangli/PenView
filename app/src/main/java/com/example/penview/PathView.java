package com.example.penview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry
 * @Date 2020-01-18  18:00
 * @Email 2427417167@qq.com
 */
public class PathView extends View {
    private static final String TAG = "PathView";
    private  Path mPath;
    private List<Path> mPathList = new ArrayList<>();
    private Paint mPaint = new Paint();
    //临时路径
    private Path tempPath = new Path();

    private Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    private boolean flag = false;
    private int mHalfWidth;
    private int mHalfHeight;
    private int bitmapWidth;
    private int bitmapHeight;
    private int mViewWidth;
    private int mViewHeight;
    private int num;
    private Bitmap mDrawBitmap;
    private Canvas mDrawCanvas;
    private int i;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mPaint.setColor(0xff00ff00);
        mPaint.setStrokeWidth(20);//会影响宽度，哪怕是path.addOval或mPath.addCircle
        mPaint.setStyle(Paint.Style.STROKE);//不要空心，填充路径
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPath = new Path();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //创建bitmap
                mDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                mDrawCanvas = new Canvas(mDrawBitmap);
            }
        });
//        bitmapWidth = bitmap.getWidth();
//        bitmapHeight = bitmap.getHeight();


//        mPath.quadTo(100,100,(200+100)/2f,(200+100)/2f);
//        mPath.lineTo(100, 100);
        measure(0, 0);
        mViewHeight = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
//        mPath.addCircle(40, 40, 45, Path.Direction.CCW);
//        mPath.addCircle(80, 80, 45, Path.Direction.CCW);
//        mPath.addCircle(120, 120, 45, Path.Direction.CCW);
    }

    /**
     * @param canvas
     * @param x
     * @param y
     * @param ft     path填充模式
     * @param paint
     */
    private void showPath(Canvas canvas, int x, int y, Path.FillType ft,
                          Paint paint) {
        canvas.save();
        canvas.translate(x, y);
        canvas.clipRect(0, 0, 160, 160);
        canvas.drawColor(Color.WHITE);
        mPath.setFillType(ft);
        canvas.drawPath(mPath, paint);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHalfWidth = w / 2;
        mHalfHeight = h / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                mTouchStartY = event.getY();
                onTouchDown(event);
                return true;
            case MotionEvent.ACTION_MOVE:
//                float y = event.getY();
//                if (y >= mTouchStartY) {
//                    float moveSize = (y - mTouchStartY) * SLIPPAGE_FACTOR;
//                    float progress = moveSize >= TOUCH_MOVE_MAX_Y ? 1 : (moveSize / TOUCH_MOVE_MAX_Y);
//                    pullView.setProgress(progress);
//                }
                onTouchMove(event);
                return true;
            case MotionEvent.ACTION_UP:
//                pullView.release();
                mPathList.add(tempPath);
                return true;
            default:
                break;

        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //通过measureSpec.getSize得到具体的size尺寸信息
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (mViewWidth!=0){
            flag = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint(mPaint);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(8);
        //(0,50),(50,0),(100,50),(150,100),(200,50)
        mPaint.setAlpha(10);
//0
        if (flag){
//            flag/ = false;
            System.out.println("===============");
            canvas.drawPoint(0f,500f,paint);
            mPaint.setColor(Color.CYAN);
            mPath.moveTo(0, 500);

//1
            canvas.drawPoint(100,0,paint);
            mPath.quadTo(0,500,(0+100)/2f,(500+0)/2);
            canvas.drawPath(mPath,mPaint);

//            mPa th.reset();
//            mPaint.setColor(Color.RED);
//            Path mPath2 = new Path();
//            mPath2.moveTo(0,500);
//            mPath2.lineTo(100,0);
//            canvas.drawPath(mPath2,mPaint);

//            mPaint.setColor(Color.CYAN);
//            canvas.drawPath(mPath,mPaint);
//2
            canvas.drawPoint(200,500,paint);
            mPath.quadTo(100,0,(100+200)/2f,(0+500)/2);
//            canvas.drawPath(mPath,mPaint);
//3
//            canvas.drawPoint(150,100,paint);
//            mPath.quadTo(100,50,(100+150)/2,(50+100)/2);
//4
//            canvas.drawPoint(200,50,paint);
//            mPath.quadTo(150,100,(150+200)/2,(100+50)/2);

//5
//            canvas.drawPoint(250,0,paint);
//            mPath.quadTo(200,50,(200+250)/2,(50+0)/2);

//            canvas.drawPoint(300,50,paint);
//            mPath.quadTo(250,0,(250+300)/2,(0+50)/2);

//            canvas.drawPoint(350,100,paint);
//            mPath.quadTo(300,50,(300+350)/2,(50+100)/2);
//        canvas.drawPath(mPath,mPaint);
        }

//        paint.setColor(Color.RED);
//        canvas.drawColor(0xFF00CCCC);//画背景画布的canvas
////        canvas.translate(20, 20);
//        paint.setAntiAlias(true);
////        showPath(canvas, 0, 0, Path.FillType.WINDING, paint);
////        showPath(canvas, 160 * 2, 0, Path.FillType.EVEN_ODD, paint);
////        showPath(canvas, 0, 160 * 2, Path.FillType.INVERSE_WINDING, paint);
////        showPath(canvas, 160 * 2, 160 * 2, Path.FillType.INVERSE_EVEN_ODD, paint);
//        //画自己的画笔
//        mPaint.setStyle(Paint.Style.STROKE);
////        for (int i = 0; i < mPathList.size(); i++) {
////            canvas.drawPath(mPathList.get(i),paint);
////        }
//        if (tempPath != null && !tempPath.isEmpty()) {
//            canvas.drawPath(tempPath, paint);
//        }
//        for (Path path : mPathList) {//遍历历史path
//            canvas.drawPath(path, paint);
//        }
//
//        //画bitmap
//        canvas.drawBitmap(bitmap, null, new Rect(mHalfWidth-bitmapWidth/2, 100, 800, 200), null);
//        canvas.drawPath(mPath,paint);
        //平移到view的中间
//        canvas.translate(mViewWidth/2,mViewHeight/2);
//
//        Path path1 = new Path();
//        //添加圆弧，从坐标系右边轴开始
//        path1.addArc(new RectF(-100,-100,100,100),0,180);
//        //添加大矩形
//        path1.addRect(-200,-200,200,200, Path.Direction.CW);//getlength会首先得到这个path
//        //添加小矩形
//        path1.addRect(-100,-100,100,100,Path.Direction.CW);
//        paint.setColor(Color.RED);
//        canvas.drawPath(path1,paint);
//        Path dst = new Path();
//
//        //将pathMeasure于path关联
//        PathMeasure measure1 = new PathMeasure(path1,false);
//        PathMeasure measure2 = new PathMeasure(path1,true);
//
//        float len1 = measure1.getLength();
//        System.out.println("========="+len1);
//        boolean b = measure1.nextContour();
//        float length = measure1.getLength();
//        System.out.println("========="+length);
//        boolean b1 = measure1.nextContour();
//        float length1 = measure1.getLength();
//        System.out.println("========="+length1);
//
//        //当dst本身有内容的时候,可以得出结论当getSegment被截取的会被保存到dst,而不是替换
//        dst.lineTo(-300, -300);
//        Log.e("TAG", "forceClosed=false---->"+measure1.getLength());//600
//        Log.e("TAG", "forceClosed=true----->"+measure2.getLength());//800
//        // 截取一部分存入dst中，startWithMoveTo=true使用 moveTo 保持截取得到的 Path 第一个点的位置不变
//        measure1.getSegment(200,600,dst,false);
//        paint.setColor(Color.CYAN);
//        canvas.drawPath(dst,paint);

        //pathMeasure
//        Path path = new Path();
//        //多路径的效果需要关闭硬件加速！！
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        //190*4=760
////        左上角，右上角
//        path.addRect(0, 0, 200, 200, Path.Direction.CW);
//        //forceClosed 的设置状态可能会影响测量结果，如果 Path 没有闭合但在与 PathMeasure
//        // 关联的时候设置 forceClosed 为 true 时，
//        // 测量结果可能会比 Path 实际长度稍长一点，
//        // 获取得到的是该 Path 闭合时的状态
//        PathMeasure measure = new PathMeasure(path, false);
//        //length为1600，400*4=1600，即为周长
//        float length = measure.getLength();
//        Log.i("damon", "length1:" + length);
//        paint.setColor(Color.GREEN);
////        canvas.drawPath(path, paint);
//        //dst
//        Path dst = new Path();
////        moveTo(0,0)
////        dst.moveTo(0,100);
////        dst.lineTo(300, 200);
//        //下面使用rMoveTo和moveTo差不多
////        dst.moveTo(100,100);
//        dst.lineTo(0,0);
//        ////当 startD 为0时，起始点在矩形的左上角
//        //    //大于0时，按绘制方向移动对应距离
//        //startWithMoveTo:false，代表该起始点是否位上一个的结束点(是否保持连续性)。false不保持连续性
//        //startD：开始截取位置距离 Path 起点的长度 取值范围: 0 <= startD < stopD <= Path总长度；
//        //如果startWithMove=true，那么就会以startD为起始点,否则以0为起始点
//        //startWithMoveTo=false是和dst结合使用，
//        measure.getSegment(190, 405, dst, true);
//        paint.setColor(Color.RED);


//        //下面 将dst transform
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(100,100);
//        dst.transform(matrix);
//        canvas.drawPath(dst, paint);
//        canvas.drawPath(tempPath, mPaint);
//            canvas.drawBitmap(mDrawBitmap,0,0,null);
    }


    private void onTouchMove(MotionEvent event) {
        Log.e(TAG, "onTouchMove: " + (num++));
        tempPath.lineTo(event.getX(), event.getY());
        //这个radius也会影响到宽度，画笔的宽度,是和mPath共同决定的
//        tempPath.addCircle(event.getX(), event.getY(), i++, Path.Direction.CW);
        Paint paint = new Paint(mPaint);
        paint.setStrokeWidth(1.4f*mPaint.getStrokeWidth()+8.0f);
//        int color = Color.parseColor("#00ffffff");
        int color = Color.argb(0, 255, 155, 255);
        paint.setColor(paint.getColor()^color);
        mDrawCanvas.drawPath(tempPath,paint);
//        mDrawCanvas.drawPath(tempPath,aaa );
        invalidate();
    }

    protected void onTouchDown(MotionEvent event) {
//        mPath.reset();
        num = 0;
        tempPath.moveTo(event.getX(), event.getY());
        invalidate();
    }

    public void move() {
        if (mPathList.size() > 0) {
            Matrix matrix = new Matrix();
            matrix.postTranslate(200, 10);
            mPathList.get(0).transform(matrix);
            invalidate();
        }
    }


    /**
     * 使用判断点是否在path中
     *
     * @param path
     * @param point
     * @return
     */
    private boolean pointInPath(Path path, Point point) {
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        //Region类
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region.contains(point.x, point.y);
    }

    public void selectMode() {
        flag = true;
    }
}
