package com.zyinfo.brj.entity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.zyinfo.brj.R;
import com.zyinfo.brj.view.LoadingView;


/**
 *
 */
public class ShapeLoadingDialog {



    private Context mContext;
    private Dialog mDialog;
    private LoadingView mLoadingView;
    private View mDialogContentView;


    public ShapeLoadingDialog(Context context) {
        this.mContext=context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        mDialogContentView= LayoutInflater.from(mContext).inflate(R.layout.layout_dialog,null);
        mLoadingView= (LoadingView) mDialogContentView.findViewById(R.id.loadView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(mDialogContentView);
    }

    public void setBackground(int color){
        GradientDrawable gradientDrawable= (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence){
        mLoadingView.setLoadingText(charSequence);
    }

    public void show(){
        mDialog.show();

    }

    public void dismiss(){
        mDialog.dismiss();
    }
    public void loading(CharSequence charSequence){
    	setLoadingText(charSequence);
        show();
    }

    public Dialog getDialog(){
        return  mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        mDialog.setCanceledOnTouchOutside(cancel);
    }
}
