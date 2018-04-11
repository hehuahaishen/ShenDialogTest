package com.shen.shendialog.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shen.shendialog.R;
import com.shen.shendialog.dialog.config.EditDialogConfig;
import com.shen.shendialog.dialog.config.ProgressDialogConfig;
import com.shen.shendialog.utils.StringUtils;
import com.shen.shendialog.view.ClearEditText;


/**
 * Created by shen on 10/30 0030.
 */
public abstract class ShenEditDialog extends AlertDialog implements View.OnClickListener{


    Context mContext;

    /** 标题 */
    private LinearLayout mLLayoutTitle;
    private TextView mTvTitle;

    /** 内容 */
    private LinearLayout mLLayoutContent;
    private TextView mTvContent;

    /** 编辑框 -- cet */
    private ClearEditText mCEt;

    /** 取消 */
    private LinearLayout mLLayoutCancel;
    private Button mBtnCancel;

    /** 确认 */
    private LinearLayout mLLayoutConfirm;
    private Button mBtnConfirm;

    /** 消息内容 */
    private String mContent;

    /** 编辑框 */
    private String mContentCEt;

    private String mContentText = "";
    private String mTitleText = "";

    /** 窗口的配置 */
    private EditDialogConfig mConfig = new EditDialogConfig.Builder()
            .setTitle("标题")
            .setContent("内容")
            .setCancelText("取消")
            .setConfirmText("确认")
            .setCancelOnClickListener(new EditDialogConfig.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shenDialog.dismiss();
                }
            }).setConfirmOnClickListener(new EditDialogConfig.OnClickListener() {
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
    public ShenEditDialog init(EditDialogConfig config){
        mConfig = config;
        return this;
    }

    protected ShenEditDialog(Context context) {
        super(context, R.style.alert_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shen_edit);

        // 安卓弹出ProgressDialog进度框之后触摸屏幕就消失了的解决方法
        setCanceledOnTouchOutside(false);

        // 设置此窗口的设置
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.flags = //params.flags |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH |
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED |
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND;             // 设置了这个就有灰色背景了

        params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        window.setAttributes(params);

        initView();
        setView();
    }

    private void initView(){

        mLLayoutTitle = findViewById(R.id.lLayout_title);
        mTvTitle = findViewById(R.id.tv_title);

        mLLayoutContent = findViewById(R.id.lLayout_content);
        mTvContent = findViewById(R.id.tv_content);

        mCEt = findViewById(R.id.cet);

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
    public ShenEditDialog setContentText (String text) {
        mContentText = text;
        if (mTvContent != null && StringUtils.isNotEmpty(text)) {
            showContentText(true);
            mTvContent.setText(mContentText);
        }
        return this;
    }

    public ShenEditDialog setTitleText (String text) {
        mTitleText = text;
        if (mTvTitle != null) {
            mTvTitle.setText(mTitleText);
        }
        return this;
    }


     /*---------------------------- 更改控件内容2 -------------------------------*/
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


    public String getContentCEt() {
        return mCEt.getText().toString().trim();
    }

    public void setContentCEt(String contentCEt) {
        mContentCEt = contentCEt;
        mCEt.setText(contentCEt);
    }
}
