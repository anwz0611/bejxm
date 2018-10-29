package com.zyinfo.brj.ui.more.activity;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.PhoneItemInfo;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.utils.SharedUtil;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description:
 */


public class PhoneListActivity extends AppCompatActivity implements OnClickListener {
    private ViewPager mPager;
    private ImageView image;
    private int currIndex;// 当前页卡编号
    private int bmpW;// 横线图片宽度
    private int offset;// 图片移动的偏移量
    @Bind(R.id.tv_guid1)
    TextView view1;

    @Bind(R.id.text_three)
    TextView text_three;// 群发短信的按钮
    //
    @Bind(R.id.fanhui)
    ImageView goBack;// 返回


    @Bind(R.id.tv_guid2)
    TextView view2;
    /**
     * 头部处理
     */
    @Bind(R.id.textView1)
    TextView tv_title;

    private Context mContext;

    private View view;
    private static List<PhoneItemInfo> phoneList = new ArrayList<PhoneItemInfo>();

    private List<View> Views; // Tab页面列表

    private LocalActivityManager manager = null;
    private Boolean isSend = false;
    private String huquDate = "";


    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, PhoneListActivity.class);

        mContext.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_list);
        ButterKnife.bind(this);
        mContext = this;
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
//		CrashHandler.getInstance().init(PhoneListActivity.this);
        tv_title.setText("通讯录");
        view1.setOnClickListener(new txListener(0));
        view2.setOnClickListener(new txListener(1));
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("updata");// 返回更新
        mFilter.addAction("Num");//
        mFilter.addAction("showOld");//
        PhoneListActivity.this.registerReceiver(mBroadcast, mFilter);


        InitImage();
        InitViewPager();
        goBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private int num = 0;

    public static List<Map<String, String>> mList;

    // 开启广播监听是否发送成功！
    private BroadcastReceiver mBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {

                case "showOld":
                    if (list1.size() == 0) {
                        //发送成功的返回
                        text_three.setText("群发短信");
                        isSend = false;
                    } else {
                        //没有发送的返回
                        isSend = true;
                    }
                    break;
                //短信编辑页面返回取值
                case "updata":
                    list1.clear();
                    text_three.setText("群发短信");
                    isSend = false;
                    break;
            /*
			 * 改变发送的人总个数
			 */
                case "Num":
                    num = SharedUtil.getIntValue(context, "ChooseNum", 0)
                            + SharedUtil.getIntValue(context, "ChooseNum1", 0);
                    Log.e("获取改变num数", num + "");
                    text_three.setText("发送(" + num + ")");
                    break;
                default:
                    break;
            }
        }
    };

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		context = getActivity();
//		if (view == null) {
//			view = x.view().inject(this, inflater, container);
//		}
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		manager = new LocalActivityManager(getActivity(), true);
//		manager.dispatchCreate(savedInstanceState);
//		CrashHandler.getInstance().init(getActivity());
//		tv_title.setText("通讯录");
//		view1.setOnClickListener(new txListener(0));
//		view2.setOnClickListener(new txListener(1));
//		IntentFilter mFilter = new IntentFilter();
//		mFilter.addAction("updata");// 返回更新
//		mFilter.addAction("Num");//
//		mFilter.addAction("showOld");//
//		context.registerReceiver(mBroadcast, mFilter);
//		InitImage();
//		InitViewPager();
//		return view;
//	}

    @SuppressLint("ResourceAsColor")
    public class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPager.setCurrentItem(index);
        }
    }

    /*
     * 初始化图片的位移像素
     */
    public void InitImage() {
        text_three.setVisibility(View.VISIBLE);
        text_three.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        PhoneListActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = screenW / 2;
        // imgageview设置平移，使下划线平移到初始位置（平移一个offset）
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        image.setImageMatrix(matrix);
    }

    public void getnum(int num) {

        text_three.setText("" + num);
    }

    /*
     * * 初始化ViewPager
     */
    public void InitViewPager() {

        mPager = (ViewPager) findViewById(R.id.viewpager);
        Views = new ArrayList<View>();
        Intent intent = new Intent(this, CompanyCityFragment.class);
        Views.add(getView("A", intent));
        Intent intent2 = new Intent(this, CompanyStationFragment.class);
        Views.add(getView("B", intent2));
        mPager.setAdapter(new MyPagerAdapter(Views));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {
        private int one = offset + bmpW / 2;// 两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);// 平移动画
            currIndex = arg0;
            animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);// 动画持续时间0.2秒
            image.startAnimation(animation);// 是用ImageView来显示动画的
            int i = currIndex + 1;
            // Toast.makeText(ReservoirViewpagerActivity.this, "您选择了第"+i+"个页卡",
            // Toast.LENGTH_SHORT).show();
            switch (arg0) {
                case 0:
                    view1.setTextColor(getResources().getColor(R.color.bluetext));
                    view2.setTextColor(getResources().getColor(R.color.greytext));
                    break;

                case 1:
                    view2.setTextColor(getResources().getColor(R.color.bluetext));
                    view1.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> Views;

        public MyPagerAdapter(List<View> Views) {
            this.Views = Views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(Views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return Views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(Views.get(arg1), 0);
            return Views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.text_three:
                if (isSend) {
                    // 显示群发短信
                    Log.e("phoneList页面——allSend", "进入-显示群发短信");
                    if (list1.size() != 0) {
                        for (int i = 0; i < list1.size(); i++) {
                            Log.e("" + i, list1.get(i).getDwlxr());
                        }
                        isSend = false;
                        //带值跳转到
                        Intent intent = new Intent(mContext, EditoeSmsActivity.class);
                        Bundle bd = new Bundle();
                        bd.putString("type", "phoneList");
                        bd.putSerializable("phoneList", (Serializable) list1);
                        intent.putExtras(bd);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "您还没有选择发送人员！", 0).show();
                    }
                } else {
                    // 显示发送的条数
                    Log.e("phoneList页面——allSend", "进入-显示发送的条数");
                    num = SharedUtil.getIntValue(this, "ChooseNum", 0)
                            + SharedUtil.getIntValue(this, "ChooseNum1", 0);
                    text_three.setText("发送(" + num + ")");
                    isSend = true;
                    Intent intent = new Intent();
                    intent.setAction("allSend");
                    PhoneListActivity.this.sendBroadcast(intent);
                }
                break;
            default:
                break;
        }
    }

    /*
     * 获取adapter里面选中的list长度
     */
    public static List<addressBookInfoBeans.WaterBookDetailListBean> list1 = new ArrayList<addressBookInfoBeans.WaterBookDetailListBean>();

    public static void getmList(int num, addressBookInfoBeans.WaterBookDetailListBean WaterBookDetailListBean) {
        if (num == 0) {

            //添加选中的
            if (!list1.contains(WaterBookDetailListBean)) {
                list1.add(WaterBookDetailListBean);
            }
        } else {
            //删除取消的
            list1.remove(WaterBookDetailListBean);
            Log.e("计入删除的代码", list1.size() + "");
        }
        Log.e("@@改变后的list的个数", list1.size() + "");
    }
}
