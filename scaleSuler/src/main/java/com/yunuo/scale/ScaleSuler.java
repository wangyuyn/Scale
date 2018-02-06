package com.yunuo.scale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangyu on 2017/12/21.
 */

public class ScaleSuler extends View {

    private int viewHeight;
    private int viewWidth;
    private Paint mPaint;
    private Paint centerLinePaint;
    private int scaleWidth;
    private int screenWidth;
    private int bottomColor = Color.GRAY;
    private int centerLineColor = Color.RED;
    private int maxScale = 200;
    private int scaleValue = 1000;

    //当前刻度
    private int scale = 0;

    private int distance = 0;

//    private GestureDetector gestureDetector;
    public ScaleSuler(Context context) {
        super(context);
        init(context,null,0);
    }

    public ScaleSuler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }


    public ScaleSuler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        loadAttributes(attrs,defStyleAttr);

        screenWidth = UIUtils.getScreenWidth(context);
        scaleWidth = (screenWidth / (5 * 10));
        viewWidth = screenWidth+maxScale*scaleWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(bottomColor);
        mPaint.setStrokeWidth(UIUtils.dipToPx(context,1));
        mPaint.setTextSize(UIUtils.dipToPx(context,14));

        centerLinePaint = new Paint(mPaint);
        centerLinePaint.setColor(centerLineColor);

        //手势解析器
//        gestureDetector = new GestureDetector(context, gestureListener);
//        gestureDetector.setIsLongpressEnabled(false);
//
//        Scroller scroller = new Scroller(context);
    }

    private void loadAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.ScaleSuler,defStyleAttr,0);
        bottomColor = a.getColor(R.styleable.ScaleSuler_bottomColor,bottomColor);
        centerLineColor = a.getColor(R.styleable.ScaleSuler_centerlineColor,centerLineColor);
        maxScale = a.getInteger(R.styleable.ScaleSuler_maxScale,maxScale);
        scaleValue = a.getInteger(R.styleable.ScaleSuler_scaleValue,scaleValue);


        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画底部线
        canvas.drawLine(0,viewHeight,viewWidth,viewHeight,mPaint);

        //开始绘制的位置
//        int startLocation = screenWidth/2-scale*scaleWidth;
        int startLocation = screenWidth/2-distance;
        //画中间刻度
        for (int i = 0;i<=maxScale;i++){

            //画长刻度
            if (i%10==0){
                Rect bounds = new Rect();
                String drawStr = i*scaleValue+"";
                mPaint.getTextBounds(drawStr, 0, drawStr.length(), bounds);
                canvas.drawText(drawStr,startLocation+i*scaleWidth-bounds.width()/2,viewHeight-UIUtils.dipToPx(getContext(),30),mPaint);
                canvas.drawLine(startLocation+i*scaleWidth,viewHeight-UIUtils.dipToPx(getContext(),20),startLocation+i*scaleWidth,viewHeight,mPaint);
            }else{
                canvas.drawLine(startLocation+i*scaleWidth,viewHeight-UIUtils.dipToPx(getContext(),10),startLocation+i*scaleWidth,viewHeight,mPaint);
            }
        }

        //画中间红线
        canvas.drawLine(screenWidth/2,UIUtils.dipToPx(getContext(),10),screenWidth/2,viewHeight, centerLinePaint);
    }

    private int actionDownX;
    private SulerCallBack sulerCallBack;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                actionDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                distance+=-currentX+actionDownX;
                actionDownX = currentX;

                if (distance<0)distance=0;
                if (distance>scaleWidth*maxScale)distance = scaleWidth*maxScale;

                if (sulerCallBack!=null){
                    sulerCallBack.sulerValue(scaleValue*((distance+scaleWidth/2)/scaleWidth));
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (distance%scaleWidth-scaleWidth/2>0){
                    distance+=scaleWidth-distance%scaleWidth;
                }else{
                    distance-=distance%scaleWidth;
                }
                if (sulerCallBack!=null){
                    sulerCallBack.sulerValue(scaleValue*distance/scaleWidth);
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setCallBack(SulerCallBack sulerCallBack) {
        this.sulerCallBack = sulerCallBack;
    }

    public interface SulerCallBack{
        void sulerValue(int value);
    }

//    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.e("1111", "onScroll: "+e1.getAction()+":"+e2.getAction() );
//            distance+=distanceX;
//            if (distance<0)distance=0;
//            if (distance>scaleWidth*maxScale)distance = scaleWidth*maxScale;
//
//            invalidate();
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
////            Log.e("1111", "onScroll: "+velocityX );
//            return true;
//        }
//    };
}
