package com.zyinfo.brj.ui.zone.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.PatrolDataBeans;
import com.zyinfo.brj.bean.PatrolUploadingBeans;
import com.zyinfo.brj.entity.ShapeLoadingDialog;
import com.zyinfo.brj.ui.more.activity.FristTextDetails;
import com.zyinfo.brj.ui.more.activity.SecondTextDetails;
import com.zyinfo.brj.ui.news.adapter.GirdDropDownAdapter;
import com.zyinfo.brj.ui.zone.adapter.NinePicturesAdapter;
import com.zyinfo.brj.ui.zone.contract.PatrolDataContract;
import com.zyinfo.brj.ui.zone.model.PatrolDataModel;
import com.zyinfo.brj.ui.zone.presenter.PatrolDataPresenter;
import com.zyinfo.brj.utils.BitmapUtil;
import com.zyinfo.brj.widget.DateChooseWheelViewDialog;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonutils.ImageLoaderUtils;
import com.zyinfo.common.commonwidget.LoadingTip;
import com.zyinfo.common.commonwidget.NoScrollGridView;
import com.zyinfo.common.commonwidget.NormalTitleBar;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 巡查上传
 */
public class CirclePublishActivity extends BaseActivity<PatrolDataPresenter, PatrolDataModel> implements PatrolDataContract.View, View.OnClickListener {
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.gridview)
    NoScrollGridView gridview;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.TextView_dmt_text1)
    TextView TextView_dmt_text1;
    @Bind(R.id.TextView_dmt_text2)
    TextView TextView_dmt_text2;
    @Bind(R.id.TextView_dmt_text3)
    TextView TextView_dmt_text3;
    @Bind(R.id.TextView_dmt_text4)
    TextView TextView_dmt_text4;
    @Bind(R.id.TextView_dmt_text5)
    TextView TextView_dmt_text5;
    @Bind(R.id.TextView_dmt_text6)
    TextView TextView_dmt_text6;
    @Bind(R.id.TextView_dmt_text7)
    TextView TextView_dmt_text7;
    @Bind(R.id.TextView_dmt_text8)
    TextView TextView_dmt_text8;
    @Bind(R.id.TextView_dmt_text9)
    TextView TextView_dmt_text9;
    @Bind(R.id.TextView_dmt_text10)
    TextView TextView_dmt_text10;


    @Bind(R.id.TextView_dmt1)
    TextView TextView_dmt1;
    @Bind(R.id.TextView_dmt2)
    TextView TextView_dmt2;
    @Bind(R.id.TextView_dmt3)
    TextView TextView_dmt3;
    @Bind(R.id.TextView_dmt4)
    TextView TextView_dmt4;
    @Bind(R.id.TextView_dmt5)
    TextView TextView_dmt5;
    @Bind(R.id.TextView_dmt6)
    TextView TextView_dmt6;
    @Bind(R.id.TextView_dmt7)
    TextView TextView_dmt7;
    @Bind(R.id.TextView_dmt8)
    TextView TextView_dmt8;
    @Bind(R.id.TextView_dmt9)
    TextView TextView_dmt9;
    @Bind(R.id.TextView_dmt10)
    TextView TextView_dmt10;
    private ShapeLoadingDialog dialog;
    private NinePicturesAdapter ninePicturesAdapter;
    private int REQUEST_CODE = 120;
    private PatrolDataBeans list = new PatrolDataBeans();
    private GirdDropDownAdapter cityAdapter;
    String[] mStringItems;
    String[] mStringItems1;
    String[] mStringItems2;
    int one = -1;
    int two = -1;
    int thre = -1;
    String Lttd="";
    String Lgtd="";

    /**
     * 启动入口
     *
     * @param context
     */
    public static void startAction(Context context,String Lttd,String Lgtd) {
        Intent intent = new Intent(context, CirclePublishActivity.class);
        intent.putExtra(AppConstant.LTTD, Lttd);
        intent.putExtra(AppConstant.LGTD, Lgtd);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_publish_zone;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        ntb.setTitleText(getString(R.string.xunchashangchuan));
        Lttd = getIntent().getStringExtra(AppConstant.LTTD);
        Lgtd = getIntent().getStringExtra(AppConstant.LGTD);


        dialog = new ShapeLoadingDialog(mContext);
        ninePicturesAdapter = new NinePicturesAdapter(this, 9, new NinePicturesAdapter.OnClickAddListener() {
            @Override
            public void onClickAdd(int positin) {
                choosePhoto();
            }
        });
        gridview.setAdapter(ninePicturesAdapter);

        mPresenter.getPatrolDataRequest();

    }

    @OnClick({R.id.tv_back, R.id.tv_save, R.id.jstongxun1, R.id.jstongxun2, R.id.jstongxun3, R.id.jstongxun4, R.id.jstongxun5, R.id.jstongxun6, R.id.jstongxun7,
            R.id.jstongxun8, R.id.jstongxun9, R.id.jstongxun10, R.id.TextView_dmt_text6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_save:
//                if (TextUtils.isEmpty(etContent.getText().toString())) {
//                    CircleItem circleItem = new CircleItem();
//                    circleItem.setContent(etContent.getText().toString());
//                    circleItem.setPictures(getPictureString());
//                    circleItem.setIcon(AppCache.getInstance().getIcon());
//                    circleItem.setUserId(AppCache.getInstance().getUserId());
//                    circleItem.setNickName(AppCache.getInstance().getUserName());
//                    circleItem.setCreateTime(Long.parseLong("1471942968000"));
//                    mRxManager.post(AppConstant.ZONE_PUBLISH_ADD, circleItem);
//                    finish();
//                } else {
//                    ToastUitl.showToastWithImg(getString(R.string.circle_publish_empty), R.drawable.ic_warm);
//                }

                if (ninePicturesAdapter.getData().size()==1){
                    Toast.makeText(mContext, "至少选择一张图片！", Toast.LENGTH_LONG).show();
                    break;
                }
                if (TextView_dmt_text1.getText().equals("") || TextView_dmt_text3.getText().equals("请选择") || TextView_dmt_text2.getText().
                        equals("请选择") || TextView_dmt_text5.getText().equals("请选择") || TextView_dmt_text6.equals("请选择时间")) {
                    Toast.makeText(mContext, "请填写名称，类型，单位，隐患程度，时间！", Toast.LENGTH_LONG).show();

                    break;
                }
                upload();
                break;
            case R.id.jstongxun1:
                Intent jstongxun = new Intent(mContext, SecondTextDetails.class);
                jstongxun.putExtra("biaoti", TextView_dmt1.getText().toString());
                jstongxun.putExtra("neirong233", TextView_dmt_text1.getText().toString());
                startActivityForResult(jstongxun, 5);

                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);

                break;

            case R.id.jstongxun2:


                final NormalListDialog dialog = new NormalListDialog(this, mStringItems);
                dialog.title("工程类型")//
                        .titleTextSize_SP(16)//
                        .titleBgColor(Color.parseColor("#409ED7"))//
                        .itemPressColor(Color.parseColor("#85D3EF"))//
                        .itemTextColor(Color.parseColor("#303030"))//
                        .itemTextSize(14)//
                        .cornerRadius(2)//
                        .widthScale(0.8f)//
                        .show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        TextView_dmt_text2.setText(mStringItems[i]);
                        one = i;

                        dialog.dismiss();
                    }
                });

                break;
            case R.id.jstongxun3:


                final NormalListDialog dialog1 = new NormalListDialog(this, mStringItems1);
                dialog1.title("申报单位")//
                        .titleTextSize_SP(16)//
                        .titleBgColor(Color.parseColor("#409ED7"))//
                        .itemPressColor(Color.parseColor("#85D3EF"))//
                        .itemTextColor(Color.parseColor("#303030"))//
                        .itemTextSize(14)//
                        .cornerRadius(2)//
                        .widthScale(0.8f)//
                        .show();
                dialog1.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        TextView_dmt_text3.setText(mStringItems1[i]);
                        two = i;
                        dialog1.dismiss();
                    }
                });
                break;
            case R.id.jstongxun4:
                Intent jstongxun4 = new Intent(mContext, SecondTextDetails.class);
                jstongxun4.putExtra("biaoti", TextView_dmt4.getText().toString());
                jstongxun4.putExtra("neirong233", TextView_dmt_text4.getText().toString());
                startActivityForResult(jstongxun4, 8);

                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);

                break;
            case R.id.jstongxun5:

                final NormalListDialog dialog2 = new NormalListDialog(this, mStringItems2);
                dialog2.title("隐患程度")//
                        .titleTextSize_SP(16)//
                        .titleBgColor(Color.parseColor("#409ED7"))//
                        .itemPressColor(Color.parseColor("#85D3EF"))//
                        .itemTextColor(Color.parseColor("#303030"))//
                        .itemTextSize(14)//
                        .cornerRadius(2)//
                        .widthScale(0.8f)//
                        .show();
                dialog2.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        TextView_dmt_text5.setText(mStringItems2[i]);
                        thre = i;
                        dialog2.dismiss();

                    }
                });


                break;
            case R.id.jstongxun7:
                Intent jstongxun7 = new Intent(getApplicationContext(), FristTextDetails.class);
                jstongxun7.putExtra("biaoti", TextView_dmt7.getText().toString());
                jstongxun7.putExtra("neirong233", TextView_dmt_text7.getText().toString());
                startActivityForResult(jstongxun7, 10);

                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);

                break;
            case R.id.jstongxun8:
                Intent jstongxun8 = new Intent(getApplicationContext(), FristTextDetails.class);
                jstongxun8.putExtra("biaoti", TextView_dmt8.getText().toString());
                jstongxun8.putExtra("neirong233", TextView_dmt_text8.getText().toString());
                startActivityForResult(jstongxun8, 11);

                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);

                break;
            case R.id.jstongxun9:
                Intent jstongxun9 = new Intent(getApplicationContext(), SecondTextDetails.class);
                jstongxun9.putExtra("biaoti", TextView_dmt9.getText().toString());
                jstongxun9.putExtra("neirong233", TextView_dmt_text9.getText().toString());
                startActivityForResult(jstongxun9, 12);

                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);

                break;
            case R.id.jstongxun10:
                Intent jstongxun10 = new Intent(getApplicationContext(), SecondTextDetails.class);
                jstongxun10.putExtra("biaoti", TextView_dmt10.getText().toString());
                jstongxun10.putExtra("neirong233", TextView_dmt_text10.getText().toString());
                startActivityForResult(jstongxun10, 13);

                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);

                break;
            case R.id.TextView_dmt_text6:
                DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(mContext, new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        TextView_dmt_text6.setText(time + ":00");

                    }
                });

                startDateChooseDialog.setDateDialogTitle("选择日期");

                startDateChooseDialog.showDateChooseDialog();
                break;

            default:
                break;


        }
    }


    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(true)
                // 确定按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                .titleBgColor(ContextCompat.getColor(this, R.color.main_color))
                // 使用沉浸式状态栏
                .statusBarColor(ContextCompat.getColor(this, R.color.main_color))
                // 返回图标ResId
                .backResId(R.drawable.ic_arrow_back)
                .title("图片")
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9 - ninePicturesAdapter.getPhotoCount())
                .build();
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoaderUtils.display(context, imageView, path);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                if (ninePicturesAdapter != null) {
                    ninePicturesAdapter.addAll(pathList);
                }
            } else if (requestCode == 5) {
                String neirong = data.getStringExtra("neirong");

                TextView_dmt_text1.setText(neirong);
            } else if (requestCode == 6) {
                String neirong1 = data.getStringExtra("neirong");

                TextView_dmt_text2.setText(neirong1);
            } else if (requestCode == 7) {
                String neirong2 = data.getStringExtra("neirong");

                TextView_dmt_text3.setText(neirong2);
            } else if (requestCode == 8) {
                String neirong3 = data.getStringExtra("neirong");

                TextView_dmt_text4.setText(neirong3);
            } else if (requestCode == 9) {
                String neirong4 = data.getStringExtra("neirong");

                TextView_dmt_text5.setText(neirong4);
            } else if (requestCode == 10) {
                String neirong5 = data.getStringExtra("neirong");

                TextView_dmt_text7.setText(neirong5);
            } else if (requestCode == 11) {
                String neirong6 = data.getStringExtra("neirong");

                TextView_dmt_text8.setText(neirong6);

            } else if (requestCode == 12) {
                String neirong7 = data.getStringExtra("neirong");

                TextView_dmt_text9.setText(neirong7);


            } else if (requestCode == 13) {
                String neirong8 = data.getStringExtra("neirong");

                TextView_dmt_text10.setText(neirong8);


            }

        }
    }

    /**
     * 上传图片
     *
     * @return
     */
    private void upload() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型

        List<String> strings = ninePicturesAdapter.getData();
        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < strings.size() - 1; i++) {
            File file = new File(BitmapUtil.compressImage(strings.get(i)));
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            builder.addFormDataPart("file", file.getName(), photoRequestBody);


//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            map.put("files\"; filename=\"" + file.getName(), requestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();


        RequestBody can = toRequestBody(TextView_dmt_text1.getText().toString());
        RequestBody cantype = toRequestBody(list.getCantype().get(one).getValue().toString());
        RequestBody id = toRequestBody(list.getManageUnit().get(two).getId().toString());
        RequestBody classification = toRequestBody(TextView_dmt_text4.getText().toString());
        RequestBody grade = toRequestBody(list.getYhdj().get(thre).getValue().toString());
        RequestBody tm = toRequestBody(TextView_dmt_text6.getText().toString());
        RequestBody part = toRequestBody(TextView_dmt_text7.getText().toString());
        RequestBody description = toRequestBody(TextView_dmt_text8.getText().toString());
        RequestBody mgr = toRequestBody(TextView_dmt_text9.getText().toString());
        RequestBody tel = toRequestBody(TextView_dmt_text10.getText().toString());
        RequestBody annex = toRequestBody(TextView_dmt_text10.getText().toString());

        RequestBody lttd = toRequestBody(Lgtd);
        RequestBody lgtd = toRequestBody(Lttd);

        mPresenter.getPatrolUploadingRequest(can, cantype, id, classification, grade,
                tm, part, description, mgr, tel, annex, lgtd, lttd, parts);


    }

    /**
     * 获取到拼接好的图片
     *
     * @return
     */
    private String getPictureString() {
        //拼接图片链接
        List<String> strings = ninePicturesAdapter.getData();
        if (strings != null && strings.size() > 0) {
            StringBuilder allUrl = new StringBuilder();
            for (int i = 0; i < strings.size(); i++) {
                if (!TextUtils.isEmpty(strings.get(i))) {
                    allUrl.append(strings.get(i) + ";");
                }
            }
            if (!TextUtils.isEmpty(allUrl)) {
                String url = allUrl.toString();
                url = url.substring(0, url.lastIndexOf(";"));
                return url;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public void showLoading(String title) {

        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);

        dialog.loading("上传中....");
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        dialog.dismiss();

    }

    @Override
    public void showErrorTip(String msg) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
    }


    @Override
    public void returnPatrolData(PatrolDataBeans patrolDataBeans) {
        if (patrolDataBeans != null) {
            list = patrolDataBeans;
            mStringItems = new String[list.getCantype().size()];
            for (int i = 0; i < list.getCantype().size(); i++) {
                mStringItems[i] = list.getCantype().get(i).getLabel();
            }

            mStringItems1 = new String[list.getManageUnit().size()];
            for (int i = 0; i < list.getManageUnit().size(); i++) {
                mStringItems1[i] = list.getManageUnit().get(i).getName();
            }
            mStringItems2 = new String[list.getYhdj().size()];
            for (int i = 0; i < list.getYhdj().size(); i++) {
                mStringItems2[i] = list.getYhdj().get(i).getLabel();
            }
        }
    }

    @Override
    public void returnPatrolUploading(PatrolUploadingBeans Result) {

        if (Result.isUploadingstate()) {
            Toast.makeText(mContext, "上传 成功！", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mContext, "上传 失败！", Toast.LENGTH_LONG).show();
        }
    }
    public RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }


}
