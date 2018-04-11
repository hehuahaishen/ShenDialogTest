package com.shen.shendialog.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shen.shendialog.R;
import com.shen.shendialog.dialog.config.DialogConfig;
import com.shen.shendialog.utils.StringUtils;
import com.shen.shendialog.view.DrawErrorView;
import com.shen.shendialog.view.DrawLoadView;
import com.shen.shendialog.view.DrawSuccessView;
import com.shen.shendialog.view.DrawWarnView;


/**
 * Created by shen on 10/30 0030.
 */
public class ShenDialog extends AlertDialog {

    /** 取消 */
    private LinearLayout mLLayoutDrawView;
    /** 错误图标 */
    private DrawErrorView mDrawErrorView;
    /** 加载图标 */
    private DrawLoadView mDrawLoadView;
    /** 正确图标 */
    private DrawSuccessView mDrawSuccessView;
    /** 警告图标 */
    private DrawWarnView mDrawWarnView;


    /** 标题 */
    private LinearLayout mLLayoutTitle;
    private TextView mTvTitle;

    /** 内容 */
    private LinearLayout mLLayoutContent;
    private TextView mTvContent;

    /** 取消 */
    private LinearLayout mLLayoutCancel;
    private Button mBtnCancel;

    /** 删除 */
    private LinearLayout mLLayoutDel;
    private Button mBtnDel;

    /** 确认 */
    private LinearLayout mLLayoutConfirm;
    private Button mBtnConfirm;


    private String mContentText = "";
    private String mTitleText = "";

    /** 窗口的配置 */
    private DialogConfig mConfig = new DialogConfig.Builder()
            .setDialogType(DialogConfig.DialogType.Normal)
            .setTitle("标题")
            .setContent("内容")
            .setCancelText("否")
            .setConfirmText("是")
            .setCancelOnClickListener(new DialogConfig.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shenDialog.dismiss();
                }
            }).setConfirmOnClickListener(new DialogConfig.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shenDialog.dismiss();
                }
            }).create();


    // ShenDialog shenDialog = new ShenDialog(mContext);
    // shenDialog.init(config);
    // shenDialog.show();



    public ShenDialog(Context context) {
        super(context, R.style.alert_dialog);
    }


    /**
     * 获得窗口参数
     * @param config
     */
    public ShenDialog init(DialogConfig config){
        mConfig = config;
        return this;
    }


    /*-----------  要注意到一个问题，dialog.show() 之后才调用 onCreate() --------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shen);

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


    private void initView(){

        mLLayoutDrawView = findViewById(R.id.lLyout_DrawView);
        mDrawErrorView = findViewById(R.id.view_error);
        mDrawLoadView = findViewById(R.id.view_load);
        mDrawSuccessView = findViewById(R.id.view_success);
        mDrawWarnView = findViewById(R.id.view_warn);


        mLLayoutTitle = findViewById(R.id.lLayout_title);
        mTvTitle = findViewById(R.id.tv_title);

        mLLayoutContent = findViewById(R.id.lLayout_content);
        mTvContent = findViewById(R.id.tv_content);

        mLLayoutCancel = findViewById(R.id.layout_cancel);
        mBtnCancel = findViewById(R.id.btn_cancel);

        mLLayoutDel = findViewById(R.id.layout_del);
        mBtnDel = findViewById(R.id.btn_del);

        mLLayoutConfirm = findViewById(R.id.layout_confirm);
        mBtnConfirm = findViewById(R.id.btn_confirm);
    }


    private void setView(){

        if(mConfig.isCancelable()){
            setCancelable(true);
        }else {
            setCancelable(false);
        }

        switch (mConfig.getDialogType()){
            case Normal:
                mLLayoutDrawView.setVisibility(View.GONE);
                break;

            case Success:
                mLLayoutDrawView.setVisibility(View.VISIBLE);
                mDrawSuccessView.setVisibility(View.VISIBLE);
                break;

            case Error:
                mLLayoutDrawView.setVisibility(View.VISIBLE);
                mDrawErrorView.setVisibility(View.VISIBLE);
                break;

            case Warn:
                mLLayoutDrawView.setVisibility(View.VISIBLE);
                mDrawWarnView.setVisibility(View.VISIBLE);
                break;

            case Load:
                mLLayoutDrawView.setVisibility(View.VISIBLE);
                mDrawLoadView.setVisibility(View.VISIBLE);
                setCancelable(false);               // 点击"返回键"不关闭窗口
                break;

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

        if(StringUtils.isNotEmpty(mConfig.getDelText())){
            mBtnDel.setText(mConfig.getDelText());
        }
        if(mConfig.getDelOnClickListener() == null){
            showDelButton(false);
        }else {
            if(StringUtils.isEmpty(mConfig.getDelText())){
                mBtnDel.setText("删除");
            }
            showDelButton(true);
            mBtnDel.setOnClickListener(mConfig.getDelOnClickListener());
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
    }

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
     * 设置--删除按钮--是否显示
     * @param b
     */
    private void showDelButton(boolean b){
        if(mLLayoutDel != null) {
            if (b)
                mLLayoutDel.setVisibility(View.VISIBLE);
            else
                mLLayoutDel.setVisibility(View.GONE);
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
    public ShenDialog setContentText (String text) {
        mContentText = text;
        if (mTvContent != null && StringUtils.isNotEmpty(text)) {
            showContentText(true);
            mTvContent.setText(mContentText);
        }
        return this;
    }

    public ShenDialog setTitleText (String text) {
        mTitleText = text;
        if (mTvTitle != null) {
            mTvTitle.setText(mTitleText);
        }
        return this;
    }


}
