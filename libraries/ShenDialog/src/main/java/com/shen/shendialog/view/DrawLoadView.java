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
import android.view.animation.AccelerateDecelerateInterpolator;

import com.shen.shendialog.R;

/**
 *
 */
public class DrawLoadView extends View {

    /** 圆心坐标 */
    private PointF mCenterPoint;
    /** 画笔 */
    private Paint mPaint;
    /** 画笔 -- 笔size */
    private final static int mPaintSize = 10;

    /** 圆圈的开始 */
    private float mAnimatedValueStart = 0;
    /** 圆圈的结束 */
    private float mAnimatedValueEnd = 0;

    /** 动画模板 */
    private AnimatorTemplet mTemplet;


    //最终阶段
    private static final int FINAL_STATE = 2;
    //当前动画阶段
    private int mCurrAnimatorState = 0;
    private static final int OUTER_CIRCLE_ANGLE = 320;

    /**
     * 接口 -- 动画模板
     */
    private abstract class AnimatorTemplet {
        /**
         * 画
         * @param canvas
         */
        abstract void drawState(Canvas canvas);

    }

    public DrawLoadView(Context context) {
        super(context);
        init();
    }

    public DrawLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawLoadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mCenterPoint = new PointF();

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.green));  // 设置画笔颜色
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
    }


    //绘制
    @Override
    protected void onDraw(Canvas canvas) {

        if (null == mTemplet) {
            // 开启旋转动画
            mTemplet = new RotateState();   // 开启后，就会一直修改
        }

        mTemplet.drawState(canvas);         // 传递Canvas对象
    }

    /**
     * 绘制旋转动画
     */
    private class RotateState extends AnimatorTemplet {
        ValueAnimator mValueAnimatorStart;

        public RotateState() {

            // 旋转的过程
            // 就是不断的获取大圆的角度
            // 从0-2π
            mValueAnimatorStart = ValueAnimator.ofFloat(0, 1);            // 变化范围 -- 设置开始值和结束值
            //valueAnimatorStart.setInterpolator(new LinearInterpolator());   // 匀速插值器
            //valueAnimatorStart.setInterpolator(new AccelerateInterpolator()); // 在动画开始的地方速率改变比较慢，然后开始加速
            //valueAnimatorStart.setInterpolator(new AnticipateInterpolator()); // 开始的时候向后然后向前甩
            //valueAnimatorStart.setInterpolator(new DecelerateInterpolator()); // 在动画开始的地方快然后慢
            mValueAnimatorStart.setInterpolator(new AccelerateDecelerateInterpolator()); // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
            mValueAnimatorStart.setDuration(1000);                           // 设置变化时长

            mValueAnimatorStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // ValueAnimator设置监听器
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {            // 监听，改变的值
                    //mAnimatedValueStart = (float) animation.getAnimatedValue();
                    float animatedValue = (float) animation.getAnimatedValue();

                    mAnimatedValueStart = (int) (360 * animatedValue);


                    switch (mCurrAnimatorState){
                        case 0:
                            mAnimatedValueEnd = (int) (OUTER_CIRCLE_ANGLE * animatedValue);
                            break;
                        case 1:
                            mAnimatedValueEnd = OUTER_CIRCLE_ANGLE - (int) (OUTER_CIRCLE_ANGLE * animatedValue);
                            break;
                        default:
                            break;
                    }

                    invalidate();       // 重绘
                }
            });

            mValueAnimatorStart.addListener(new Animator.AnimatorListener() {
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
                    if (++mCurrAnimatorState > FINAL_STATE){
                        // 还原到第一阶段
                        mCurrAnimatorState = 0;
                    }
                }
            });



            mValueAnimatorStart.setRepeatCount(ValueAnimator.INFINITE);       // 无限循环 -- 设置重复次数
            mValueAnimatorStart.setRepeatMode(ValueAnimator.RESTART);         // 重复方式
            mValueAnimatorStart.start();
        }

        /**
         * 停止旋转动画,在数据加载完毕后供外部调用
         */
        public void stopRotate() {
            mValueAnimatorStart.cancel();
        }

        @Override
        void drawState(Canvas canvas) {
            drawCircle(canvas);
        }


    }

    /**
     * 绘制圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {

        RectF rectF = new RectF(mPaintSize, mPaintSize, getWidth()-mPaintSize, getWidth()-mPaintSize);

        // 参数一： 定义的圆弧的形状和大小的范围
        // 参数二： 这个参数的作用是设置圆弧是从哪个角度来顺时针绘画的
        // 参数三： 这个参数的作用是设置圆弧扫过的角度
        // 参数四： 这个参数的作用是设置我们的圆弧在绘画的时候，是否经过圆形
        // 参数五： 这个参数的作用是设置我们的画笔对象的属性

        canvas.drawArc(rectF, mAnimatedValueStart% 360, mAnimatedValueEnd% 360, false, mPaint);
    }
}
