package com.shen.shendialog.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shen.shendialog.R;
import com.shen.shendialog.dialog.config.ProgressDialogConfig;
import com.shen.shendialog.utils.StringUtils;

/**
 * 继承 AlertDialog 的"自定义进度条窗口"
 */
public class ShenProgressDialog extends AlertDialog {

    /** 进度条 -- 最大值 */
    private int mMax = 100;
    /** 进度条 -- 当前进度 */
    private int mCurrentVal = 0;

    /** 标题 */
    private LinearLayout mLLayoutTitle;
    private TextView mTvTitle;

    /** 内容 */
    private LinearLayout mLLayoutContent;
    private TextView mTvContent;

    /** 进度条 -- pb */
    private ProgressBar mProgressBar;
    /** 进度百分比 -- tv_progressPercent */
    private TextView mTvProgressPercent;
    /** 进度具体的数值 -- tv_progressNumber */
    private TextView mTvProgressNumber;

    /** 取消 */
    private LinearLayout mLLayoutCancel;
    private Button mBtnCancel;

    /** 确认 */
    private LinearLayout mLLayoutConfirm;
    private Button mBtnConfirm;

    /** 消息内容 */
    private String mContent;


    private String mContentText = "";
    private String mTitleText = "";

//    protected ShenProgressDialog(Context context) {
//        super(context);
//    }
//    protected ShenProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }
//
//    protected ShenProgressDialog(Context context, int themeResId) {
//        super(context, themeResId);
//    }

    /** 窗口的配置 */
    private ProgressDialogConfig mConfig = new ProgressDialogConfig.Builder()
            .setTitle("标题")
            .setContent("内容")
            .setCancelText("取消")
            .setConfirmText("确认")
            .setCancelOnClickListener(new ProgressDialogConfig.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shenDialog.dismiss();
                }
            }).setConfirmOnClickListener(new ProgressDialogConfig.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shenDialog.dismiss();
                }
            }).create();


    // ShenDialog shenDialog = new ShenDialog(mContext);
    // shenDialog.init(config);
    // shenDialog.show();

    /**
     * 获得窗口参数
     * @param config
     */
    public ShenProgressDialog init(ProgressDialogConfig config){
        mConfig = config;
        return this;
    }

    public ShenProgressDialog(Context context) {
        super(context, R.style.alert_dialog);
    }

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 0:
//                    int value = (int) msg.obj;
//                    mProgressBar.setProgress(value);
//                    Log.i("shen", "mTvProgressPercent.setText:" + value);
//                    mTvProgressPercent.setText(value/(float)getMax() * 100 + "%");
//                    Log.i("shen", "mTvProgressPercent.setText:" + value);
//                    Log.i("shen", "mTvProgressPercent.getText:" + mTvProgressPercent.getText());
//                    break;
//
//                case 1:
//                    break;
//            }
//        }
//    };


    /*-----------  要注意到一个问题，dialog.show() 之后才调用 onCreate() --------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shen_progress);

        // 安卓弹出ProgressDialog进度框之后触摸屏幕就消失了的解决方法
        setCanceledOnTouchOutside(false);

        // 设置此窗口的设置
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        window.setAttributes(params);

        initView();
        setView();
    }

    private void initView() {

        mLLayoutTitle = findViewById(R.id.lLayout_title);
        mTvTitle = findViewById(R.id.tv_title);

        mLLayoutContent = findViewById(R.id.lLayout_content);
        mTvContent = findViewById(R.id.tv_content);

        mProgressBar = findViewById(R.id.pb);
        mTvProgressPercent = findViewById(R.id.tv_progressPercent);
        mTvProgressNumber = findViewById(R.id.tv_progressNumber);

        mLLayoutCancel = findViewById(R.id.layout_cancel);
        mBtnCancel = findViewById(R.id.btn_cancel);

        mLLayoutConfirm = findViewById(R.id.layout_confirm);
        mBtnConfirm = findViewById(R.id.btn_confirm);

    }

    private void setView(){

        if(mConfig.isCancelable()){
            setCancelable(true);
        }else {
            setCancelable(false);
        }

        if(StringUtils.isEmpty(mConfig.getTitle())){
            mTvTitle.setText("标题");
        }else {
            mTvTitle.setText(mConfig.getTitle());
        }

        if(StringUtils.isEmpty(mConfig.getContent())){
            showContentText(false);
        }else {
            showContentText(true);
            mTvContent.setText(mConfig.getContent());
        }

        if(StringUtils.isNotEmpty(mConfig.getCancelText())){
            mBtnCancel.setText(mConfig.getCancelText());
        }
        if(mConfig.getCancelOnClickListener() == null){
            showCancelButton(false);
        }else {
            if(StringUtils.isEmpty(mConfig.getCancelText())){
                mBtnCancel.setText("取消");
            }
            showCancelButton(true);
            mBtnCancel.setOnClickListener(mConfig.getCancelOnClickListener());
        }

        if(StringUtils.isNotEmpty(mConfig.getConfirmText())){
            mBtnConfirm.setText(mConfig.getConfirmText());
        }
        if(mConfig.getConfirmOnClickListener() == null){
            showConfirmButton(false);
        }else {
            if(StringUtils.isEmpty(mConfig.getConfirmText())){
                mBtnConfirm.setText("确认");
            }
            showConfirmButton(true);
            mBtnConfirm.setOnClickListener(mConfig.getConfirmOnClickListener());
        }


        mProgressBar.setMax(mConfig.getMax());


//        if (mMax > 0) {
//            setMax(mMax);
//        }
//        if (mCurrentVal > 0) {
//            setProgress(mCurrentVal);
//        }
    }

        /*---------------------------- 设置控件是否显示 -------------------------------*/

 /*---------------------------- 设置控件是否显示 -------------------------------*/

    /**
     * 设置--标题文本--是否显示
     * @param b
     */
    private void showTitleText(boolean b){
        if(mLLayoutTitle != null) {
            if (b)
                mLLayoutTitle.setVisibility(View.VISIBLE);
            else
                mLLayoutTitle.setVisibility(View.GONE);
        }
    }


    /**
     * 设置--内容文本--是否显示
     * @param b
     */
    private void showContentText(boolean b){
        if(mLLayoutContent != null) {
            if (b)
                mLLayoutContent.setVisibility(View.VISIBLE);
            else
                mLLayoutContent.setVisibility(View.GONE);
        }
    }

    /**
     * 设置--取消按钮--是否显示
     * @param b
     */
    private void showCancelButton(boolean b){
        if(mLLayoutCancel != null) {
            if (b)
                mLLayoutCancel.setVisibility(View.VISIBLE);
            else
                mLLayoutCancel.setVisibility(View.GONE);
        }
    }


    /**
     * 设置--确认按钮--是否显示
     * @param b
     */
    private void showConfirmButton(boolean b){
        if(mLLayoutConfirm != null) {
            if (b)
                mLLayoutConfirm.setVisibility(View.VISIBLE);
            else
                mLLayoutConfirm.setVisibility(View.GONE);
        }
    }

    /*---------------------------- 更改控件内容 -------------------------------*/
    public ShenProgressDialog setContentText (String text) {
        mContentText = text;
        if (mTvContent != null && StringUtils.isNotEmpty(text)) {
            showContentText(true);
            mTvContent.setText(mContentText);
        }
        return this;
    }

    public ShenProgressDialog setTitleText (String text) {
        mTitleText = text;
        if (mTvTitle != null ) {
            showContentText(true);
            mTvTitle.setText(mTitleText);
        }
        return this;
    }



    /*---------------------------- 更改控件内容2 -------------------------------*/

    /**
     * 获得"进度条"的最大值
     * @return
     */
    public int getMax() {
        if (mProgressBar != null) {
            return mProgressBar.getMax();
        }
        return mMax;
    }



    /**
     * 设置"进度条"的"当前值" -- 要再主线程执行，不要就挂了（但是如果只是设置--ProgressBar--就不会）
     * @param value
     */
    public void setProgress(int value) {
        if (mProgressBar != null) {
            mProgressBar.setProgress(value);
            mTvProgressPercent.setText(value/(float)getMax() * 100 + "%");
            mCurrentVal = value;
        } else {
            mCurrentVal = value;
        }
    }

    /**
     * 设置"进度条"进度 -- 要再主线程执行，不要就挂了（但是如果只是设置--ProgressBar--就不会）
     * @param strProgress           "进度条"进度(0~100 -- 是字符串)
     * @param strProgressNumber     实际的进度(具体的内容) -- 字符串
     */
    public void setProgress(String strProgress, String strProgressNumber){
        if (mProgressBar != null && mTvProgressNumber != null && mTvProgressPercent != null) {
            mCurrentVal = StringUtils.isEmpty(strProgress) ? 0 : Integer.valueOf(strProgress);
            mProgressBar.setProgress(mCurrentVal);
            mTvProgressPercent.setText(strProgress + "%");
            mTvProgressNumber.setText(strProgressNumber);
        } else {
            mCurrentVal = StringUtils.isEmpty(strProgress) ? 0 : Integer.valueOf(strProgress);
        }
    }


    /**
     * 设置内容
     * @param content   内容
     */
    public void setContent(String content){
        if(StringUtils.isEmpty(content)){
            showContentText(false);
        }else {
            showContentText(true);
            if(mTvContent != null) {
                mTvContent.setText(content);
                mContent = content;
            }else {
                mContent = content;
            }
        }
    }

}