package com.example.administrator.school_design.view.main_view;


import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.adapt.history_adapt;
import com.example.administrator.school_design.bean.shop_car;
import com.example.administrator.school_design.diy_item.Exitactivity;
import com.example.administrator.school_design.diy_item.PopUtils;
import com.mcxtzhang.lib.AnimShopButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2017/5/4.
 * 订单界面
 */

public class history extends Fragment {
    private List<shop_car> mData;
    private ListView mListViewArray;
    private View view;
    private RefreshLayout mRefreshLayout;
    private Button search;
    private LayoutInflater inflater;
    private history_adapt adapter;
private int check_num=0;
private TextView total_price;
    private   int s=0;
    private TextView check_nu;
  private LinearLayout history_kong;
public static  boolean isfirst=true;
public static boolean isfirs_1=false;
private  Button shop_car_pay;
    private PopupWindow mPopupWindow;
    private ImageView address_add_back;
    private EditText edit_named;
    private Button save_address;
    private EditText edit_phone;
    private EditText edit_address;
    private String named;
    private String phone;
    private String address;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       if (view == null) {
            Exitactivity.getInstance().addActivity(getActivity());
            view = inflater.inflate(R.layout.history, container, false);
            init();//初始化
           mData = new ArrayList<>();
           inflater = getActivity().getLayoutInflater();
           adapter = new history_adapt(inflater,mData);
           mListViewArray.setAdapter(adapter);
           mListViewArray.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    new PopUtils(getActivity(), R.layout.history_delete,   ViewGroup.LayoutParams.WRAP_CONTENT,   ViewGroup.LayoutParams.WRAP_CONTENT,view, Gravity.CENTER, 0, 0, new PopUtils.ClickListener() {
                       @Override
                       public void setUplistener(final PopUtils.PopBuilder builder) {
                           builder.getView(R.id.history_cancel).setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   builder.dismiss();
                               }
                           });
                           builder.getView(R.id.history_dele).setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   mData.remove(position);
                                   adapter.notifyDataSetChanged();
                                   setListViewHeight(mListViewArray);
                                   iskong(mListViewArray);
                                   builder.dismiss();
                               }
                           });
                       }
                   }) {
                   };
                   return false;
               }
           });
           adpa();
           adpate();


        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }


      return view;
    }

    private void adpa() {
        adapter.setChecked(new history_adapt.onChecked() {
            @Override
            public void CheckedChanged(int position, boolean isChecked) {
                int a= Integer.parseInt(mData.get(position).getPrice())* Integer.parseInt((mData.get(position).getCount()));
                if (isfirs_1=true){
                    total_price.setText("0");
                    check_nu.setText("0项");

                }
                if (isChecked){
                    s+=a;
                    total_price.setText(String.valueOf(s));
                    check_num+=1;
                    check_nu.setText(check_num+"项");
                    isfirst=false;
                }
                else if (!isChecked&&!isfirst){
                    s-=a;
                    total_price.setText(String.valueOf(s));
                    check_num-=1;
                    check_nu.setText(check_num+"项");

                }

            }
        });
    }

    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {

        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), colorPrimaryDark));
        }
    }


    private void adpate() {
        mData.clear();
        //将数据一一添加到自定义的布局中。
        BmobQuery<shop_car> query = new BmobQuery<shop_car>();
        query.findObjects(new FindListener<shop_car>() {
                              @Override
                              public void done(List<shop_car> list, BmobException e) {
                                  if(e==null){
                                      for (final shop_car gameScore : list) {
                                          BmobFile pic=gameScore.getPic();
                                          pic.download(new DownloadFileListener() {
                                                           @Override
                                                           public void done(String s, BmobException e) {
                                                               shop_car data = new shop_car(gameScore.getTitle(), gameScore.getCount(), gameScore.getPrice(), s);
                                                               mData.add(data);
                                                               adapter.notifyDataSetChanged();
                                                               setListViewHeight(mListViewArray);
                                                           }

                                                           @Override
                                                           public void onProgress(Integer integer, long l) {

                                                           }
                                                       });

                                      }

                                  }

                              }
                          });



    }

    private void setListViewHeight(ListView listView) {
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
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height =700;
            listView.setLayoutParams(params);
            history_kong.setVisibility(View.GONE);
            mListViewArray.setVisibility(View.VISIBLE);
        }else {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight +90+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }

    }

    /* 初始化*/
    private void init() {
        shop_car_pay= (Button) view.findViewById(R.id.shop_car_pay);
        shop_car_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopwindow();
            }
        });
        history_kong= (LinearLayout) view.findViewById(R.id.history_kong);
        total_price= (TextView) view.findViewById(R.id.total_price);
        mListViewArray = (ListView) view.findViewById(R.id.list_history);
        mRefreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout1);
        setThemeColor(android.R.color.holo_orange_light, android.R.color.holo_orange_dark);
       check_nu= (TextView) view.findViewById(R.id.check_nm);

       mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        total_price.setText("0");
        check_nu.setText("0项");
        adpate();
        iskong(mListViewArray);
        mRefreshLayout.finishRefresh();


    }
});

    }

    private void showpopwindow() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.address_add,null);
        mPopupWindow=new PopupWindow(contentview,
                ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow.setContentView(contentview);

        address_add_back= (ImageView) contentview.findViewById(R.id.address_add_back);
        address_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        edit_named= (EditText) contentview.findViewById(R.id.edit_named);
        edit_phone= (EditText) contentview.findViewById(R.id.edit_phone);
        edit_address= (EditText) contentview.findViewById(R.id.edit_address);
        save_address= (Button) contentview.findViewById(R.id.save_address);

        mPopupWindow.setAnimationStyle(R.style.history_more1);
        mPopupWindow.showAtLocation(contentview, Gravity.BOTTOM, 0,0);
        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                named=edit_named.getText().toString();
                phone=edit_phone.getText().toString();
                address=edit_address.getText().toString();
                if (named.equals("")){
                    Toast.makeText(getActivity(),"请输入收件姓名",Toast.LENGTH_SHORT).show();
                }else if (phone.equals("")){
                    Toast.makeText(getActivity(),"请输入收件电话",Toast.LENGTH_SHORT).show();
                }else if (address.equals("")){
                    Toast.makeText(getActivity(),"请输入收件地址",Toast.LENGTH_SHORT).show();
                }else {
                    pd = ProgressDialog.show(getActivity(), null, "正在下单，请稍候...");
            //        final String title=shop_name.getText().toString();
           //         final String price= shop_price.getText().toString();
                    final String count = String.valueOf(AnimShopButton.mCount);

                }

            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
    public void iskong(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        if (listAdapter.getCount() ==0){
            history_kong.setVisibility(View.VISIBLE);
            mListViewArray.setVisibility(View.GONE);
        }
        else{

            history_kong.setVisibility(View.GONE);
            mListViewArray.setVisibility(View.VISIBLE);
        }

    }

}



