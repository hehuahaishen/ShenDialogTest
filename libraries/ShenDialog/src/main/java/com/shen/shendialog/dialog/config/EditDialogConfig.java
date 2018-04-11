package com.shen.shendialog.dialog.config;

import android.view.View;

/**
 * @author:  shen
 * @date:    2018/1/30
 *
 */
public class EditDialogConfig {

    /** 标题 */
    private String mTitle = "";
    /** 内容 */
    private String mContent = "";
    /** 取消按钮监听 */
    private OnClickListener mCancelOnClickListener = null;
    /** 取消按钮文本 */
    private String mCancelText = "";
    /** 确认按钮监听 */
    private OnClickListener mConfirmOnClickListener = null;
    /** 确认按钮文本 */
    private String mConfirmText = "";

    /** 点击"返回键" -- false:不关闭窗口 true:关闭窗口 */
    private boolean mCancelable = true;


    /**
     * 构造函数
     */
    private EditDialogConfig(){

    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public OnClickListener getCancelOnClickListener() {
        return mCancelOnClickListener;
    }

    public String getCancelText() {
        return mCancelText;
    }

    public OnClickListener getConfirmOnClickListener() {
        return mConfirmOnClickListener;
    }

    public String getConfirmText() {
        return mConfirmText;
    }

    public boolean isCancelable() {
        return mCancelable;
    }

    /**
     * 内部了 -- Builder
     */
    public static class Builder{
        /** 标题 */
        private String title = "";
        /** 内容 */
        private String content = "";
        /** 取消按钮监听 */
        private OnClickListener cancelOnClickListener = null;
        /** 取消按钮文本 */
        private String cancelText = "";
        /** 确认按钮监听 */
        private OnClickListener confirmOnClickListener = null;
        /** 确认按钮文本 */
        private String confirmText = "";
        /** 点击"返回键" -- false:不关闭窗口 true:关闭窗口 */
        private boolean cancelable = true;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }


        public Builder setCancelOnClickListener(OnClickListener cancelOnClickListener) {
            this.cancelOnClickListener = cancelOnClickListener;
            return this;
        }

        public Builder setCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder setConfirmOnClickListener(OnClickListener confirmOnClickListener) {
            this.confirmOnClickListener = confirmOnClickListener;
            return this;
        }

        public Builder setConfirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }



        // 设置配置
        private void applyConfig(EditDialogConfig config){
            config.mTitle = this.title;
            config.mContent = this.content;

            config.mCancelOnClickListener = this.cancelOnClickListener;
            config.mCancelText = this.cancelText;
            config.mConfirmOnClickListener = this.confirmOnClickListener;
            config.mConfirmText = this.confirmText;
            config.mCancelable = this.cancelable;
        }

        public EditDialogConfig create(){
            EditDialogConfig dialogConfig = new EditDialogConfig();
            applyConfig(dialogConfig);
            return dialogConfig;
        }

    }

    public abstract static class OnClickListener implements View.OnClickListener {
    }

}
