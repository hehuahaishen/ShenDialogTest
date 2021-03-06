package com.shen.shendialog.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.shen.shendialog.R;

/**
 *
 */
public class DrawWarnView extends View {

    /** 圆心坐标 */
    private PointF mCenterPoint;
    /** 画笔 */
    private Paint mPaint;
    /** 画笔 -- 笔size */
    private final static int mPaintSize = 10;

    /** 圆圈的开始 */
    private float mCircleValueStart = 0;
    /** 圆圈的结束 */
    private float mCircleValueEnd = 0;

    /** 左边的线 */
    private float mLineValueLift = 0;
    /** 右边的线 */
    private float mLineValueRight = 0;

    /** 动画模板 -- 画圆 */
    private AnimatorTemplet mTempletCircle;
    /** 动画模板 -- 竖 */
    private AnimatorTemplet mTempletLine;
    /** 动画模板 -- 点 */
    private AnimatorTemplet mTempletPoint;

    private float radius = 0;                        // 圆弧半径

    /**
     * 接口 -- 动画模板
     */
    private abstract class AnimatorTemplet {
        /**
         * 画圆
         * @param canvas
         */
        abstract void drawCircle(Canvas canvas);

        /**
         * 画线 -- 竖
         * @param canvas
         */
        abstract void drawLine(Canvas canvas);

        /**
         * 画线 -- 点
         * @param canvas
         */
        abstract void drawPoint(Canvas canvas);

    }

    /**
     * 当前画的类型
     */
    private enum DrawType{
        Circle,
        Line,
        Point
    }
    private DrawType mDrawType = DrawType.Circle;




    public DrawWarnView(Context context) {
        super(context);
        init();
    }

    public DrawWarnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawWarnView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mCenterPoint = new PointF();

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.orange));  // 设置画笔颜色
        mPaint.setStrokeWidth(mPaintSize);                                // 设置圆弧的宽度
        mPaint.setStyle(Paint.Style.STROKE);                     // 设置圆弧为空心
        mPaint.setAntiAlias(true);                               // 消除锯齿
        mPaint.setStrokeCap(Paint.Cap.ROUND);        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，
        // 如圆形样式Cap.ROUND,或方形样式Cap.SQUARE

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterPoint.x = w / 2f;                             // 确定圆心坐标
        mCenterPoint.y = h / 2f;

        if(w > 2 * mPaintSize)
            radius = (w - 2 * mPaintSize)/ 2f;
        else
            radius = w / 2f;
    }


    //绘制
    @Override
    protected void onDraw(Canvas canvas) {

        switch (mDrawType){
            case Circle:
                if (null == mTempletCircle) {
                    mTempletCircle = new DrawCircle();   // 开启后，就会一直修改
                }
                mTempletCircle.drawCircle(canvas);         // 传递Canvas对象
                break;

            case Line:
                if (null == mTempletLine) {
                    mTempletLine = new DrawLine();   // 开启后，就会一直修改
                }
                mTempletCircle.drawCircle(canvas);         // 传递Canvas对象
                mTempletLine.drawLine(canvas);         // 传递Canvas对象
                break;

            case Point:
                if (null == mTempletPoint) {
                    mTempletPoint = new DrawPoint();   // 开启后，就会一直修改
                }
                mTempletCircle.drawCircle(canvas);         // 传递Canvas对象
                mTempletLine.drawLine(canvas);         // 传递Canvas对象
                mTempletPoint.drawPoint(canvas);         // 传递Canvas对象
                break;
        }

    }


    /**
     * 绘制旋转动画
     */
    private class DrawCircle extends AnimatorTemplet {
        ValueAnimator valueAnimatorStart;
        ValueAnimator valueAnimatorEnd;


        public DrawCircle() {

            // 旋转的过程
            // 就是不断的获取大圆的角度
            // 从0-2π
            valueAnimatorStart = ValueAnimator.ofFloat(0, 359);            // 变化范围 -- 设置开始值和结束值
            valueAnimatorStart.setInterpolator(new LinearInterpolator());   // 匀速插值器
            valueAnimatorStart.setDuration(200);                           // 设置变化时长
            //valueAnimatorStart.setRepeatCount(ValueAnimator.INFINITE);       // 无限循环 -- 设置重复次数
            valueAnimatorStart.setRepeatCount(0);                              // 设置重复次数
            valueAnimatorStart.setRepeatMode(ValueAnimator.RESTART);         // 重复方式

            valueAnimatorStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // ValueAnimator设置监听器
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {            // 监听，改变的值
                    mCircleValueEnd = (float) animation.getAnimatedValue();
                    invalidate();       // 重绘
                }
            });

            valueAnimatorStart.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDrawType = DrawType.Line;
                    invalidate();       // 重绘
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            valueAnimatorStart.start();
        }

        /**
         * 停止旋转动画,在数据加载完毕后供外部调用
         */
        public void stop() {
            valueAnimatorStart.cancel();
            valueAnimatorEnd.cancel();
        }

        @Override
        void drawCircle(Canvas canvas) {
            RectF rectF = new RectF(mPaintSize, mPaintSize, getWidth()-mPaintSize, getWidth()-mPaintSize);
            // 参数一： 定义的圆弧的形状和大小的范围
            // 参数二： 这个参数的作用是设置圆弧是从哪个角度来顺时针绘画的
            // 参数三： 这个参数的作用是设置圆弧扫过的角度
            // 参数四： 这个参数的作用是设置我们的圆弧在绘画的时候，是否经过圆形
            // 参数五： 这个参数的作用是设置我们的画笔对象的属性
            canvas.drawArc(rectF, mCircleValueStart, mCircleValueEnd, false, mPaint);
        }

        @Override
        void drawLine(Canvas canvas) {

        }

        @Override
        void drawPoint(Canvas canvas) {

        }
    }


    /**
     * 绘制旋转动画
     */
    private class DrawLine extends AnimatorTemplet {
        ValueAnimator valueAnimatorLeft;

        public DrawLine() {

            // 旋转的过程
            // 就是不断的获取大圆的角度
            // 从0-2π
            valueAnimatorLeft = ValueAnimator.ofFloat(0, radius/4*3);            // 变化范围 -- 设置开始值和结束值
            valueAnimatorLeft.setInterpolator(new LinearInterpolator());   // 匀速插值器
            valueAnimatorLeft.setDuration(100);                           // 设置变化时长
            //valueAnimatorLeft.setRepeatCount(ValueAnimator.INFINITE);       // 无限循环 -- 设置重复次数
            valueAnimatorLeft.setRepeatCount(0);                              // 设置重复次数
            valueAnimatorLeft.setRepeatMode(ValueAnimator.RESTART);         // 重复方式

            valueAnimatorLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // ValueAnimator设置监听器
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {            // 监听，改变的值
                    mLineValueLift = (float) animation.getAnimatedValue();
                    invalidate();       // 重绘
                }
            });

            valueAnimatorLeft.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDrawType = DrawType.Point;
                    invalidate();       // 重绘
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            valueAnimatorLeft.start();

        }

        /**
         * 停止旋转动画,在数据加载完毕后供外部调用
         */
        public void stop() {
            valueAnimatorLeft.cancel();
        }


        @Override
        void drawCircle(Canvas canvas) {

        }

        float start_x = mPaintSize + radius;
        float start_y = mPaintSize + radius/2;
        @Override
        void drawLine(Canvas canvas) {
            canvas.drawLine(start_x, start_y,
                    start_x, start_y + mLineValueLift, mPaint);
        }

        @Override
        void drawPoint(Canvas canvas) {

        }
    }


    /**
     * 绘制旋转动画
     */
    private class DrawPoint extends AnimatorTemplet {
        ValueAnimator valueAnimatorRight;

        public DrawPoint() {

            // 旋转的过程
            // 就是不断的获取大圆的角度
            // 从0-2π
            valueAnimatorRight = ValueAnimator.ofFloat(0, 1);            // 变化范围 -- 设置开始值和结束值
            valueAnimatorRight.setInterpolator(new LinearInterpolator());   // 匀速插值器
            valueAnimatorRight.setDuration(50);                           // 设置变化时长
            //valueAnimatorLeft.setRepeatCount(ValueAnimator.INFINITE);       // 无限循环 -- 设置重复次数
            valueAnimatorRight.setRepeatCount(0);                              // 设置重复次数
            valueAnimatorRight.setRepeatMode(ValueAnimator.RESTART);         // 重复方式

            valueAnimatorRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // ValueAnimator设置监听器
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {            // 监听，改变的值
                    mLineValueRight = (float) animation.getAnimatedValue();
                    invalidate();       // 重绘
                }
            });

            valueAnimatorRight.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            valueAnimatorRight.start();

        }

        /**
         * 停止旋转动画,在数据加载完毕后供外部调用
         */
        public void stop() {
            valueAnimatorRight.cancel();
        }


        @Override
        void drawCircle(Canvas canvas) {

        }

        @Override
        void drawLine(Canvas canvas) {

        }

        float start_x = mPaintSize + radius;
        float start_y = mPaintSize + radius/4*6;
        @Override
        void drawPoint(Canvas canvas) {

            RectF rectF1 = new RectF(start_x-1, start_y, start_x+1, start_y+2);
            // 画圆弧
            canvas.drawArc(rectF1, 0, 360, true, mPaint);
        }
    }
}
