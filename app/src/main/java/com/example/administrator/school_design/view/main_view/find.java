package com.example.administrator.school_design.view.main_view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.school_design.R;
import com.example.administrator.school_design.adapt.write_adapt;
import com.example.administrator.school_design.bean.my_shop;
import com.example.administrator.school_design.bean.my_shop_1;
import com.example.administrator.school_design.loopview.why168.LoopViewPagerLayout;
import com.example.administrator.school_design.loopview.why168.listener.OnBannerItemClickListener;
import com.example.administrator.school_design.loopview.why168.loader.OnDefaultImageViewLoader;
import com.example.administrator.school_design.loopview.why168.modle.BannerInfo;
import com.example.administrator.school_design.loopview.why168.modle.IndicatorLocation;
import com.example.administrator.school_design.loopview.why168.modle.LoopStyle;
import com.example.administrator.school_design.loopview.why168.utils.L;
import com.example.administrator.school_design.util.DynamicTimeFormat;
import com.example.administrator.school_design.view.item_view.shop_list;
import com.example.administrator.school_design.view.item_view.thing_content;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/4.
 * 写入界面
 */

public class find extends Fragment implements OnBannerItemClickListener {
    private View view;
    private LoopViewPagerLayout mLoopViewPagerLayout;
    private RefreshLayout mRefreshLayout;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private List<my_shop_1> mData1;
    private LayoutInflater inflater;
    private write_adapt adapter;

    private ArrayList<my_shop_1> mData;
    private write_adapt adapter1;

    private ListView mListViewArray;
    //小标题
    private LinearLayout first_title;
    private LinearLayout second_title;
    private LinearLayout third_title;
    private LinearLayout four_title;

    private TextView first_title_1;
    private TextView second_title_1;
    private TextView third_title_1;
    private TextView  four_title_1;

    ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
    private int i;
    private EditText edit_name;
     private Button search;
    private PopupWindow mPopupWindow;
    private ImageView search_back;
    private ListView search_list;
    private String tex;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view= inflater.inflate(R.layout.find,container,false);
            init();
            adpate();

            initloop();
            init_refresh();
          refresh();
      }
        else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        return  view;
    }


    private void refresh() {
        RefreshLayout refreshLayout = (RefreshLayout)view .findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
           //     addshoptitle();
                addshop();

                refreshlayout.finishRefresh();//传入false表示刷新失败
            }
        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//
//                refreshlayout.finishLoadmore();//传入false表示加载失败
//
//            }
//        });
    }

    private void adpate() {
        mData1 = new ArrayList<>();
        inflater = getActivity().getLayoutInflater();
        adapter = new write_adapt(inflater,mData1);

        mListViewArray.setAdapter(adapter);
        addshop();
        mListViewArray.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getActivity(),shop_list.class);
        intent.putExtra("shop_list", position+"");
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
});

    }

    private void addshop() {
        mData1.clear();
        //将数据一一添加到自定义的布局中。
        BmobQuery<my_shop_1> query = new BmobQuery<my_shop_1>();
        query.findObjects(new FindListener<my_shop_1>() {
            @Override
            public void done(List<my_shop_1> object, BmobException e) {
                if(e==null){
                    for (final my_shop_1 gameScore : object) {
                        BmobFile pic=gameScore.getPic();
                        pic.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                Log.i("我的list","out"+s);

                                if (s!=null){
                                    my_shop_1 data=new my_shop_1(gameScore.getTitle(),gameScore.getTip(),gameScore.getPrice(),gameScore.getAddress(),s);
                                    mData1.add(data);

                                    adapter.notifyDataSetChanged();
                                    setListViewHeight(mListViewArray);
                                }

                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });

                    }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }


    private void init_refresh() {
        int deta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader = (ClassicsHeader)mRefreshLayout.getRefreshHeader();
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()-deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        mDrawableProgress = mClassicsHeader.getProgressView().getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }
        setThemeColor(R.color.transparent2, R.color.list_pressed);
        mClassicsHeader.setSpinnerStyle(SpinnerStyle.Translate);

    }

    private void initloop() {
        //TODO 设置LoopViewPager参数
        mLoopViewPagerLayout.setLoop_ms(3000);//轮播的速度(毫秒)
        mLoopViewPagerLayout.setLoop_duration(1500);//滑动的速率(毫秒)
        mLoopViewPagerLayout.setLoop_style(LoopStyle.Zoom);//轮播的样式-深度depth
        mLoopViewPagerLayout.setIndicatorLocation(IndicatorLocation.Center);//指示器位置-中Center
        mLoopViewPagerLayout.setNormalBackground(R.drawable.indicator_normal_background);//默认指示器颜色
        mLoopViewPagerLayout.setSelectedBackground(R.drawable.indicator_selected_background);//选中指示器颜色
        L.e("LoopViewPager Empty 参数设置完毕");

        //TODO 初始化
        mLoopViewPagerLayout.initializeData(getActivity());

        //TODO 准备数据
       final Context context=view.getContext();
        mLoopViewPagerLayout.setOnLoadImageViewListener(new OnDefaultImageViewLoader() {
            @Override
            public void onLoadImageView(ImageView view, Object object) {
                if (context != null)
                    Glide
                            .with(context)
                            .load(object)
                            .centerCrop()
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load_fail)
                            .crossFade()
                            .into(view);


            }
        });
        mLoopViewPagerLayout.setOnBannerItemClickListener(this);
        addshoptitle();


    }

    private void addshoptitle() {
        bannerInfos.clear();
        BmobQuery<my_shop> query = new BmobQuery<my_shop>();
//执行查询方法
        query.findObjects(new FindListener<my_shop>() {
            @Override
            public void done(List<my_shop> object, BmobException e) {
                if(e==null){
                    first_title_1.setText(object.get(3).getTitle());
                    second_title_1.setText(object.get(4).getTitle());
                    third_title_1.setText(object.get(5).getTitle());
                    four_title_1.setText(object.get(6).getTitle());

                    Log.i("bmob","tag："+object.size());
                    for (i=0;i<3;i++) {
                        BmobFile img= object.get(i).getPic();
                        final String title=object.get(i).getTitle();
              //          Log.i("显现","tag："+title);
                        Log.i("显现","tag："+img);
                        img.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (s!=null){
                                    bannerInfos.add(new BannerInfo<String>(s, title));
                                    //TODO 设置数据
                                    mLoopViewPagerLayout.setLoopData(bannerInfos);
                                }

                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });

                    }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    private void showpopwindow() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.searchh,null);
        mPopupWindow=new PopupWindow(contentview,
                ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow.setContentView(contentview);

        search_back= (ImageView) contentview.findViewById(R.id.search_back);
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setAnimationStyle(R.style.history_more1);
        mPopupWindow.showAtLocation(contentview, Gravity.BOTTOM, 0,0);
        search_list= (ListView) contentview.findViewById(R.id.search_list);
        mData = new ArrayList<>();
        adapter1 = new write_adapt(inflater,mData);
        search_list.setAdapter(adapter1);
        addsearch();
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Toast.makeText(getActivity(),"该功能没做，想要看这个功能的就给个优秀",Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getActivity(),shop_list.class);
//                intent.putExtra("shop_list", position+"");
//                startActivity(intent);
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void addsearch() {
        //将数据一一添加到自定义的布局中。
        BmobQuery<my_shop_1> query = new BmobQuery<my_shop_1>();
        query.addWhereEqualTo("title", tex);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        query.findObjects(new FindListener<my_shop_1>() {
            @Override
            public void done(List<my_shop_1> object, BmobException e) {
                if(e==null){
                    for (final my_shop_1 gameScore : object) {
                        BmobFile pic=gameScore.getPic();
                        pic.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                Log.i("我的list","out"+s);

                                if (s!=null){
                                    my_shop_1 data=new my_shop_1(gameScore.getTitle(),gameScore.getTip(),gameScore.getPrice(),gameScore.getAddress(),s);
                                    mData.add(data);

                                    adapter1.notifyDataSetChanged();
                                    setListViewHeight(search_list);
                                }

                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });

                    }

                }else{
                    Toast.makeText(getActivity(),"没有搜到你想要的哟",Toast.LENGTH_SHORT).show();
              //      Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp =getActivity(). getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    //初始化
    private void init() {
        edit_name= (EditText) view.findViewById(R.id.edit_name);
        search= (Button) view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tex=edit_name.getText().toString();
                if ( tex.equals("")){
                    Toast.makeText(getActivity(),"输入为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    showpopwindow();
                }
            }
        });


        first_title_1= (TextView) view.findViewById(R.id.first_title_1);
        second_title_1= (TextView) view.findViewById(R.id. second_title_1);
        third_title_1= (TextView) view.findViewById(R.id. third_title_1);
        four_title_1= (TextView) view.findViewById(R.id. four_title_1);

        mListViewArray = (ListView) view.findViewById(R.id.write_list);
        mLoopViewPagerLayout = (LoopViewPagerLayout) view.findViewById(R.id.mLoopViewPagerLayout);
        mRefreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能

        first_title= (LinearLayout) view.findViewById(R.id.first_title);
        second_title= (LinearLayout) view.findViewById(R.id.second_title);
        third_title= (LinearLayout) view.findViewById(R.id. third_title);
        four_title= (LinearLayout) view.findViewById(R.id.four_title);
        first_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),thing_content.class);
                intent.putExtra("shop_title", "3");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        second_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),thing_content.class);
                intent.putExtra("shop_title", "4");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        third_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),thing_content.class);
                intent.putExtra("shop_title", "5");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        four_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),thing_content.class);
                intent.putExtra("shop_title", "6");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
    }
    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {

        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), colorPrimaryDark));
        }
    }
    @Override
    public void onBannerClick(int index, ArrayList<BannerInfo> banner) {
//        Toast.makeText(getActivity(), "index = " + index + " title = " + banner.get(index).title, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(),thing_content.class);
        intent.putExtra("shop_title", index+"");
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @Override
    public void onStart() {
        //TODO 开始循环
        mLoopViewPagerLayout.startLoop();
        super.onStart();
    }

    @Override
    public void onStop() {
        //TODO 停止循环
        mLoopViewPagerLayout.stopLoop();
        super.onStop();
    }




    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * @param listView
     */
    private    void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }
        if (listAdapter.getCount() <3){
            ViewGroup.LayoutParams params1 = listView.getLayoutParams();
            params1.height =600;
            listView.setLayoutParams(params1);
        }else {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight +90+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

      //  Glide.with(getActivity()).pauseRequests();

    }
}
