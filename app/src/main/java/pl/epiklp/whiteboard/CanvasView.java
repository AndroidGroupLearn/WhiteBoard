package pl.epiklp.whiteboard;

import android.accessibilityservice.FingerprintGestureController;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by epiklp on 11.05.18.
 */

public class CanvasView extends View {

    //public int width;
    //public int height;
    public static int BRUSH_SIZE = 10;
    public static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_BACKGROUND = Color.WHITE;
    private static final int TOUCH_TOLERANCE = 4;
    private static final float DEFAULT_ZOOM = 1;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor;
    private int strokeWidth;
    private float backgroundZoom;
    private boolean emboss = false;
    private boolean blur = false;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private PointF start = new PointF();

    private GestureDetector gestureDetector;




    public CanvasView(Context context){
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1,1,1,1}, .4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    public void zoom(){
        Matrix mMatrix = new Matrix();
        backgroundZoom *= 2;
        mMatrix.setScale(backgroundZoom, backgroundZoom);
        mCanvas.setMatrix(mMatrix);
    //    refreshDrawableState();
    }


    public void init(DisplayMetrics metrics){
        Matrix mMatrix = new Matrix();
        mMatrix.setScale(1,1);
        mBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.setMatrix(mMatrix);
        currentColor = DEFAULT_COLOR;
        backgroundColor = DEFAULT_BACKGROUND;
        strokeWidth = BRUSH_SIZE;
        backgroundZoom = DEFAULT_ZOOM;
    }

    public void normal(){
        emboss = false;
        blur = false;
    }

    public void emboss(){
        emboss = true;
        blur = false;
    }

    public void blur(){
        emboss = false;
        blur = true;
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.save();
        mCanvas.drawColor(backgroundColor);
        for(FingerPath fp: paths){
            mPaint.setColor(currentColor);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            if(fp.emboss){
                mPaint.setMaskFilter(mEmboss);
            } else if(fp.blur){
                mPaint.setMaskFilter(mBlur);
            }
            mCanvas.drawPath(fp.mPath, mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    public void touchStart(float x, float y){
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, blur, emboss, strokeWidth, mPath);
        paths.add(fp);
        mPath.reset();
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
    }

    public void onMove(float x, float y){
        float dX = Math.abs(x - mX);
        float dY = Math.abs(y - mY);
        if(dX >= TOUCH_TOLERANCE || dY >= TOUCH_TOLERANCE){
            mPath.quadTo(mX, mY, (mX + x)/2, (mY + y)/2);
            mX = x;
            mY = y;
        }
    }

    public void touchUp(){
        mPath.moveTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        if(event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(x / backgroundZoom, y / backgroundZoom);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    onMove(x / backgroundZoom, y / backgroundZoom);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;
            }
        }else if(event.getPointerCount() == 2){

        }

        return true;
    }

    public void back(){
        if(paths.size() > 0)
            paths.remove(paths.size()-1);
        invalidate();
    }

    public void clear(){
        if(paths.size() > 0)
            paths.clear();
        invalidate();
    }

    private float spacing(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void midle(){

    }
}
