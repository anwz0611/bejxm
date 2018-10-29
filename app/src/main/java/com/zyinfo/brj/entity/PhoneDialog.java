package com.zyinfo.brj.entity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyinfo.brj.R;


/**
*
*/
public class PhoneDialog implements OnClickListener {
	private Context mContext;
    private Dialog mDialog;
    private RelativeLayout lmobile;
    private RelativeLayout lofficetel;
    private TextView mobile;
    private TextView officetel;
    private TextView officetelText;
    private TextView mobileText;
    private View mDialogContentView;
    private Drawable no_che_yuan;
    private Drawable che_yuan;
    private Button cancel;
    private Button sure;
    private int selected=1;



    public PhoneDialog(Context context) {
        this.mContext=context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        mDialogContentView= LayoutInflater.from(mContext).inflate(R.layout.phone_dialog_view,null);
        mobile= (TextView) mDialogContentView.findViewById(R.id.mobile);
        officetel= (TextView) mDialogContentView.findViewById(R.id.officetel);
        mobileText= (TextView) mDialogContentView.findViewById(R.id.textView2);
        officetelText= (TextView) mDialogContentView.findViewById(R.id.textView5);
        lmobile= (RelativeLayout) mDialogContentView.findViewById(R.id.layout1);
        lofficetel= (RelativeLayout) mDialogContentView.findViewById(R.id.layout2);
        cancel=(Button) mDialogContentView.findViewById(R.id.cancel);
        sure=(Button) mDialogContentView.findViewById(R.id.sure);
        no_che_yuan = mContext.getResources().getDrawable(R.drawable.no_che_yuan);
        che_yuan = mContext.getResources().getDrawable(R.drawable.che_yuan);
        che_yuan.setBounds( 0,0,che_yuan.getMinimumWidth(), che_yuan.getMinimumHeight()); //设置边界
		no_che_yuan.setBounds(0,0,no_che_yuan.getMinimumWidth(), no_che_yuan.getMinimumHeight() ); //设置边界
        lmobile.setOnClickListener(this);
        lofficetel.setOnClickListener(this);
        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(mDialogContentView);
    }

    public void setBackground(int color){
        GradientDrawable gradientDrawable= (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence1, CharSequence charSequence){
    	mobile.setText(charSequence1);
    	officetel.setText(charSequence);
    }

    public void show(){
        mDialog.show();

    }

    public void dismiss(){
        mDialog.dismiss();
    }
    public void loading(CharSequence mobile, CharSequence officetel){
    	setLoadingText(mobile,officetel);
        show();

    }

    public Dialog getDialog(){
        return  mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        mDialog.setCanceledOnTouchOutside(cancel);
    }

   @SuppressLint("MissingPermission")
   @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	   String phoneNumber;
		switch (v.getId()) {
		//选中手机号码
		case R.id.layout1:
			selected=1;
			mobileText.setCompoundDrawables(che_yuan, null,null, null);
			officetelText.setCompoundDrawables(no_che_yuan, null,null, null);
			break;
		//选中办公室号码
		case R.id.layout2:
			selected=2;
			mobileText.setCompoundDrawables(no_che_yuan, null,null, null);
			officetelText.setCompoundDrawables(che_yuan, null,null, null);
			break;
		//取消
		case R.id.cancel:
			mDialog.dismiss();
			break;
		//确定
		case R.id.sure:
			if (selected==1) {
				Toast.makeText(mContext, "手机号码："+mobile.getText(), 0).show();
				phoneNumber=mobile.getText().toString();
			}else {
				Toast.makeText(mContext, "办公室号码："+officetel.getText(), 0).show();
				phoneNumber=officetel.getText().toString();
			}
			//用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
            mContext.startActivity(intent);
            mDialog.dismiss();
			break;

		default:
			break;
		}
		
	}
    

    
}
