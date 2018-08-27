package com.example.administrator.school_design.view.item_view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.activity.back_activity;
import com.example.administrator.school_design.adapt.ord_adpate;
import com.example.administrator.school_design.bean.Commodity_order;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.login.box_login;
import com.example.administrator.school_design.util.DynamicTimeFormat;
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
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2018/3/11.
 */

public class my_ord extends back_activity {
    private ImageView ord_back;
    private ListView buying;
    private ListView bought;
    private List<Commodity_order> mData1;
    private LayoutInflater inflater;
    private ord_adpate adapter;
    private ArrayList<Commodity_order> mData2;
    private LayoutInflater inflater1;
    private ord_adpate adapter1;
    private RefreshLayout mRefreshLayout1;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ord);
        init();
        adpate();
        init_refresh();
        refresh();
    }
    private void init_refresh() {
        int deta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader = (ClassicsHeader)mRefreshLayout1.getRefreshHeader();
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
    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {

        mRefreshLayout1.setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimaryDark));
        }
    }
    private void refresh() {
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout_2);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                addthing();
                refreshlayout.finishRefresh();//传入false表示刷新失败
            }
        });
    }
    private void adpate() {
        mData1 = new ArrayList<>();
        inflater = getLayoutInflater();
        adapter = new ord_adpate(inflater,mData1);
        buying.setAdapter(adapter);
        adapter.setOnDelListener(new ord_adpate.onSwipeListener() {
            @Override
            public void onDel(int pos) {
            //    Toast.makeText(my_ord.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

                mData1.remove(pos);
                adapter.notifyDataSetChanged();
                setListViewHeight(buying);


            }

            @Override
            public void onTop(int pos) {

            }
        });


        mData2 = new ArrayList<>();
        inflater1 = getLayoutInflater();
        adapter1 = new ord_adpate(inflater1,mData2);
        bought.setAdapter(adapter1);
        mRefreshLayout1.autoRefresh();
        adapter1.setOnDelListener(new ord_adpate.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                mData2.remove(pos);
                adapter1.notifyDataSetChanged();
                setListViewHeight(bought);
            }

            @Override
            public void onTop(int pos) {

            }
        });
//        pd = ProgressDialog.show(my_ord.this, null, "正在获取数据，请稍候...");
//        addthing();
//        pd.dismiss();
    }

    private void addthing() {
        mData1.clear();
        mData2.clear();
        SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
        String thing_idd=sharedPreferences.getString("thing_id",null);
        BmobQuery<user> query = new BmobQuery<user>();
        query.getObject(thing_idd, new QueryListener<user>() {

            @Override
            public void done(user object, BmobException e) {
                if(e==null){
                    BmobQuery<Commodity_order> query1 = new BmobQuery<Commodity_order>();
                    query1.addWhereEqualTo("name",  object.getName());
                    query1.addWhereEqualTo("state",  "等待确认订单");
                    query1.findObjects(new FindListener<Commodity_order>() {
                        @Override
                        public void done(List<Commodity_order> object, BmobException e) {
                            if(e==null){
                                for (final Commodity_order gameScore : object) {
                                    BmobFile pic=gameScore.getPic();
                                    pic.download(new DownloadFileListener() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            Log.i("我的list122","out "+s);

                                            Commodity_order data=new Commodity_order(gameScore.getTitle(),gameScore.getCount(),gameScore.getPrice(),s,gameScore.getState());
                                            mData1.add(data);
                                            adapter.notifyDataSetChanged();
                                            setListViewHeight(buying);
                                   //         pd.dismiss();
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

                    //第二list
                    BmobQuery<Commodity_order> query2 = new BmobQuery<Commodity_order>();
                    query2.addWhereEqualTo("name",  object.getName());
                    query2.addWhereEqualTo("state",  "已购买");
                    query2.findObjects(new FindListener<Commodity_order>() {
                        @Override
                        public void done(List<Commodity_order> object, BmobException e) {
                            if(e==null){
                                for (final Commodity_order gameScore : object) {
                                    BmobFile pic=gameScore.getPic();
                                    pic.download(new DownloadFileListener() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            Log.i("我的list222","out "+s);
                                            Commodity_order data=new Commodity_order(gameScore.getTitle(),gameScore.getCount(),gameScore.getPrice(),s,gameScore.getState());
                                            mData2.add(data);
                                            adapter1.notifyDataSetChanged();
                                            setListViewHeight(bought);
                               //             pd.dismiss();
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
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }

    private void init() {
        mRefreshLayout1 = (RefreshLayout)findViewById(R.id.refreshLayout_2);
        ord_back= (ImageView) findViewById(R.id.ord_back);
        ord_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buying=(ListView)findViewById(R.id.buying);
        bought= (ListView) findViewById(R.id.bought);
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
    public void onBackPressed() {
        super.onBackPressed();

    }
}
