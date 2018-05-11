package pl.epiklp.whiteboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor;
    private int strokeWidth;
    private boolean emboss = false;
    private boolean blur = false;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);




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

    public void init(int width, int height){

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColor = DEFAULT_COLOR;
        backgroundColor = DEFAULT_BACKGROUND;
        strokeWidth = BRUSH_SIZE;
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

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                onMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }
}
