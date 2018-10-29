package com.zyinfo.brj.ui.zone.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zyinfo.brj.R;
import com.zyinfo.brj.ui.zone.bean.CommentItem;
import com.zyinfo.brj.ui.zone.presenter.CircleZonePresenter;
import com.zyinfo.common.baseapp.AppCache;
import com.zyinfo.common.commonutils.FormatUtil;


/**
 * des:评论长按对话框，保护复制和删除
 * Created by xsf
 * on 2016.07.11:11
 */
public class CommentDialog extends Dialog implements
        View.OnClickListener {

    private Context mContext;
    private CircleZonePresenter mPresenter;
    private CommentItem mCommentItem;
    private int mCirclePosition,commentPosition;

    public CommentDialog(Context context,CircleZonePresenter presenter,
                         CommentItem commentItem, int circlePosition,int commentPosition) {
        super(context, R.style.CustomProgressDialog);
        mContext = context;
        this.mPresenter = presenter;
        this.mCommentItem = commentItem;
        this.mCirclePosition = circlePosition;
        this.commentPosition=commentPosition;
        setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_comment, null);
        setContentView(view);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = (TextView) findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
        if (mCommentItem != null
                && AppCache.getInstance().getUserId().equals(
                mCommentItem.getUserId())) {
            deleteTv.setVisibility(View.VISIBLE);
        } else {
            deleteTv.setVisibility(View.GONE);
        }
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTv:
                if (mCommentItem != null) {
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(mCommentItem.getContent());
                }
                dismiss();
                break;
            case R.id.deleteTv:
                if (mPresenter != null && mCommentItem != null) {
                    mPresenter.deleteComment(mCirclePosition, FormatUtil.checkValue(mCommentItem.getId()),commentPosition);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

}
