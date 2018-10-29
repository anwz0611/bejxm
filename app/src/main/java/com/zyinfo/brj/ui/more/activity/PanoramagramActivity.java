package com.zyinfo.brj.ui.more.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.zyinfo.brj.R;
import com.zyinfo.brj.ui.news.activity.NewsBrowserActivity;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Zwei  on 2018/6/7.
 * E-Mail Address：592296083@qq.com
 * 全景图
 */

public class PanoramagramActivity extends BaseActivity implements  OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    private CommonRecycleViewAdapter<String> ListAdapter;
    private List<String> list =new ArrayList<>();
    private  String[] mStringItems={"布尔津县水利局","2817分中心","窝依莫克分中心"};
    private String[] mStringItems1={"2817灌区"};
    private String[] mStringItems2={"西岸灌区通克干渠","西岸灌区南干渠","窝依莫克","阿克吐别克","西岸干渠 ","2817龙口","致富干渠 ","喀克龙口"};
    @Override
    public int getLayoutId() {
        return R.layout.panoramagram_activity;
    }

    @Override
    public void initView() {
        ntb.setTitleText(getString(R.string.quanjingtu));
        list.add("管理中心");
        list.add("灌区一览");
        list.add("干渠龙口");
        irc.setLayoutManager(new LinearLayoutManager(mContext));
        ListAdapter = new CommonRecycleViewAdapter<String>(mContext, R.layout.policylawinfo_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, final String list) {
                helper.setText(R.id.textView9, list);
                CardView cv = helper.getView(R.id.wsf_cd);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            switch (list){
                                case "管理中心":

                                    final NormalListDialog dialog = new NormalListDialog(mContext, mStringItems);
                                    dialog.title("管理中心")//
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
                                                switch (i){
                                                    case 0:
                                                        NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/a857a16d-5950-49d5-a1da-e4865e36e0d2?buttons=on"
                                                                ,"布尔津县水利局 ");
                                                        break;

                                                    case 1:
                                                        NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/f09de431-5604-43d7-8037-e6bf1feebf2c?buttons=on"
                                                                ,"2817分中心 ");
                                                        break;
                                                    case 2:
                                                        NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/b9dc9192-1567-4635-bc53-a41c04d4bee2?buttons=on"
                                                                ,"窝依莫克分中心 ");
                                                        break;

                                                }

                                            dialog.dismiss();
                                        }
                                    });
                                    break;
                                case "灌区一览":
                                    final NormalListDialog dialog1 = new NormalListDialog(mContext, mStringItems1);
                                    dialog1.title("灌区一览")//
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
                                            NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/d0a90d59-4a32-4802-8416-c6f0d2501207?buttons=on"
                                                    ,"2817灌区");

                                            dialog1.dismiss();
                                        }
                                    });
                                    break;
                                case "干渠龙口":
                                    final NormalListDialog dialog2 = new NormalListDialog(mContext, mStringItems2);
                                    dialog2.title("干渠龙口")//
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
                                            switch (i){
                                                case 0:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/d91eb6e7-bb42-4385-987a-ce953db6da4a?buttons=on"
                                                            ,"西岸灌区通克干渠");
                                                    break;

                                                case 1:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/f09de431-5604-43d7-8037-e6bf1feebf2c?buttons=on"
                                                            ,"西岸灌区南干渠");
                                                    break;
                                                case 2:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/fb2a5211-0375-4fa4-950e-24bc32f8aef9?buttons=on"
                                                            ,"窝依莫克");
                                                    break;
                                                case 3:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/fd5efc72-c83b-4c88-8b51-f846bcbb0ec5?buttons=on"
                                                            ,"阿克吐别克");
                                                    break;
                                                case 4:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/21939882-3fce-42ea-b4f9-7ef5ebaf766f?buttons=on"
                                                            ,"西岸干渠");
                                                    break;
                                                case 5:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/094864c5-06cb-4862-ac37-64207427c694?buttons=on"
                                                            ,"2817龙口");
                                                    break;
                                                case 6:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/093db685-d3a8-41ca-a07e-77e00eb77ba4?buttons=on"
                                                            ,"致富干渠");
                                                    break;
                                                case 7:
                                                    NewsBrowserActivity.startAction(mContext,"https://www.skypixel.com/photos/play/c7ed7a9c-94b2-48b2-8ad1-e1873ca46390?buttons=on"
                                                            ,"喀克龙口");
                                                    break;

                                            }

                                            dialog2.dismiss();

                                        }
                                    });
                                    break;

                            }
                    }
                });
            }


        };

        ListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(ListAdapter);
        ListAdapter.addAll(list);

    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, PanoramagramActivity.class);

        mContext.startActivity(intent);

    }

    @Override
    public void initPresenter() {

    }


    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore(View loadMoreView) {

    }
}
