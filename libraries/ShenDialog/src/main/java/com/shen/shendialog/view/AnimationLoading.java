package com.shen.shendialog.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 *
 */
public class AnimationLoading extends View {

  /** 大圆的半径 */
  private float mBigCircleRaduis = 50;
  /** 小圆的半径 */
  private float mSubCircleRadius = 10;

  /** 大圆的圆心坐标 */
  private PointF mBigCenterPoint;
  /** 绘制背景的画笔 */
  private Paint mBgPaint;
  /** 绘制前景色的画笔 */
  private Paint mFgPaint;
  /** 动画模板 */
  private AnimatorTemplet mTemplet;
  /** 大圆旋转的角度 */
  float mBigCircleRotateAngle;
  /** 屏幕对角线一半的距离 */
  float mDiagonalDist;
  /** 用于作为绘制背景空心圆的半径 */
  float mBgStrokeCircleRadius;


  /** 6个小圆的颜色 */
  private int[] colors = new int[]{Color.RED, Color.DKGRAY, Color.YELLOW, Color.BLUE, Color.LTGRAY, Color.GREEN};


  public AnimationLoading(Context context) {
    this(context, null);
  }

  public AnimationLoading(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    mBigCenterPoint.x = w / 2f;                             // 确定大圆的圆心坐标
    mBigCenterPoint.y = h / 2f;

    mDiagonalDist = (float) (Math.sqrt(w * w + h * h) / 2); // 屏幕对角线的一半
  }


  private void init() {

    mBigCenterPoint = new PointF();

    mFgPaint = new Paint();
    mFgPaint.setAntiAlias(true);

    mBgPaint = new Paint(mFgPaint);
    mBgPaint.setColor(Color.WHITE);
    mBgPaint.setStyle(Paint.Style.STROKE);                  // 设置圆弧为空心

  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (null == mTemplet) {
      // 开启旋转动画
      mTemplet = new RotateState();   // 开启后，就会一直修改 -- mBigCircleRotateAngle
    }
    // 传递Canvas对象
    mTemplet.drawState(canvas);
  }

  /**
   * 绘制圆
   *
   * @param canvas
   */
  private void drawCircle(Canvas canvas) {
    // 获取每个小圆间隔的角度 -- 2*π == 圆周
    float rotateAngle = (float) (2 * Math.PI / colors.length);

    for (int i = 0; i < colors.length; i++) {
      // 每个小圆的实际角度
      double angle = rotateAngle * i + mBigCircleRotateAngle; // 这里加上"大圆旋转的角度"是为了带动"小圆"一起旋转
      // 计算每个小圆的圆心坐标
      float cx = (float) (mBigCircleRaduis * Math.cos(angle)) + mBigCenterPoint.x;
      float cy = (float) (mBigCircleRaduis * Math.sin(angle)) + mBigCenterPoint.y;
      // 绘制6个小圆
      mFgPaint.setColor(colors[i]);
      canvas.drawCircle(cx, cy, mSubCircleRadius, mFgPaint);
    }
  }

  /**
   * 绘制背景
   *
   * @param canvas
   */
  private void drawBackground(Canvas canvas) {
    if (mBgStrokeCircleRadius > 0f) {
      // 不断扩散的空心圆
      // 空心圆的半径 为屏幕对角线的一半
      // 空心圆的线宽 则从线宽一半到0
      float strokeWidth = mDiagonalDist - mBgStrokeCircleRadius; // 线宽从对角线的1/2 ~ 0
      mBgPaint.setStrokeWidth(strokeWidth);

      float radius = mBgStrokeCircleRadius + strokeWidth / 2;    // 半径从对角线的1/4 ~ 1/2
      canvas.drawCircle(mBigCenterPoint.x, mBigCenterPoint.y, radius , mBgPaint);
    } else {
      //绘制白色背景
      canvas.drawColor(Color.WHITE);
    }
  }

  private abstract class AnimatorTemplet {
    abstract void drawState(Canvas canvas);

  }

  /**
   * 绘制旋转动画
   */
  private class RotateState extends AnimatorTemplet {
    ValueAnimator mValueAnimator;

    public RotateState() {
      // 旋转的过程
      // 就是不断的获取大圆的角度
      // 从0-2π
      mValueAnimator = ValueAnimator.ofFloat(0, (float) Math.PI * 2); // 变化范围 -- 设置开始值和结束值
      mValueAnimator.setInterpolator(new LinearInterpolator());       // 匀速插值器
      mValueAnimator.setDuration(2000);                               // 设置变化时长

      mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // ValueAnimator设置监听器
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          // 获取大圆旋转的角度
          mBigCircleRotateAngle = (float) animation.getAnimatedValue(); // 值改变后，回调（我们就获取这个角度）
          // 重绘
          invalidate();
        }
      });

      // valueAnimatorEnd.setRepeatCount(ValueAnimator.INFINITE);            // 无限循环 -- 设置重复次数
      mValueAnimator.setRepeatCount(65536);
      // valueAnimatorEnd.setRepeatMode(ValueAnimator.RESTART);              // 重复方式
      mValueAnimator.start();
    }

    /**
     * 停止旋转动画,在数据加载完毕后供外部调用
     */
    public void stopRotate() {
      mValueAnimator.cancel();
    }

    @Override
    void drawState(Canvas canvas) {
      drawBackground(canvas);
      drawCircle(canvas);
    }
  }


  /**
   * 绘制聚合动画
   */
  private class MergingState extends AnimatorTemplet {

    public MergingState() {
      // 聚合的过程
      // 就是不断的改变大圆的半径
      // 从mBigCircleRaduis~0
      ValueAnimator valueAnimator = ValueAnimator.ofFloat(mBigCircleRaduis, 0);
      valueAnimator.setInterpolator(new OvershootInterpolator(10f)); // 弹性插值器
      valueAnimator.setDuration(600);
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          // 获取大圆变化的半径
          mBigCircleRaduis = (float) animation.getAnimatedValue();
          // 重绘
          invalidate();
        }
      });
      valueAnimator.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          // 聚合执行完后进入下一个扩散动画
          mTemplet = new SpreadState();

        }
      });
      valueAnimator.start();
    }

    @Override
    void drawState(Canvas canvas) {
      drawBackground(canvas);
      drawCircle(canvas);
    }
  }

  /**
   * 绘制扩散动画
   */
  private class SpreadState extends AnimatorTemplet {

    public SpreadState() {
      // 扩散的过程
      // 就是不断的改变背景画绘制空心圆的半径
      // 从0~mDiagonalDist
      ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mDiagonalDist);
      valueAnimator.setDuration(600);
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          // 获取大圆变化的半径
          mBgStrokeCircleRadius = (float) animation.getAnimatedValue();
          // 重绘
          invalidate();
        }
      });

      valueAnimator.start();
    }

    @Override
    void drawState(Canvas canvas) {
      drawBackground(canvas);
    }
  }


  /**
   * 停止加载动画
   */
  public void stopLoading() {
    if (null != mTemplet && mTemplet instanceof RotateState) {
      ((RotateState) mTemplet).stopRotate();
      //开启下一个聚合动画
      post(new Runnable() {
        @Override
        public void run() {
          mTemplet = new MergingState();
        }
      });
    }
  }
}