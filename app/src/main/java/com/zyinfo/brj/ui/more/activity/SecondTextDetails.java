package com.zyinfo.brj.ui.more.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyinfo.brj.R;
import com.zyinfo.brj.ui.zone.activity.CirclePublishActivity;


/**
 * Created by Zwei  on 2018/5/24.
 * E-Mail Address：592296083@qq.com
 */

public class SecondTextDetails extends Activity implements View.OnClickListener{
    private ImageView duihao,chahao;
    private TextView biaoti;
    private EditText neirong;
    private String biaotia,neirong233;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_text_details);
        biaotia = getIntent().getStringExtra("biaoti");
        neirong233 = getIntent().getStringExtra("neirong233");
        initDate();
    }

    private void initDate() {
        duihao=(ImageView)findViewById(R.id.duihao);
        duihao.setOnClickListener(this);
        chahao=(ImageView)findViewById(R.id.chahao);
        chahao.setOnClickListener(this);
        biaoti=(TextView) findViewById(R.id.textView1);
        biaoti.setText(biaotia);
        neirong=(EditText) findViewById(R.id.ftd_neirong);
        neirong.setText(neirong233);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.duihao:

                if (neirong.getText().toString().equals("")||neirong.getText()==null){

                    Toast.makeText(this, "请填写信息", 0).show();

                    break;
                }else {

                    Intent intent = new Intent(SecondTextDetails.this,CirclePublishActivity.class);
                    intent.putExtra("neirong",neirong.getText().toString());
                    setResult(RESULT_OK,intent);
                    overridePendingTransition(R.anim.slide_bottom_out,R.anim.slide_bottom_in);
                    finish();}
                break;
            case R.id.chahao:
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.setFocusable(false);
                    v.setFocusableInTouchMode(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
