package com.example.administrator.school_design;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.school_design.activity.ZoomOutPageTransformer;
import com.example.administrator.school_design.activity.back_fragment;
import com.example.administrator.school_design.activity.fragment_new;
import com.example.administrator.school_design.diy_item.Exitactivity;
import com.example.administrator.school_design.view.main_view.find;
import com.example.administrator.school_design.view.main_view.history;
import com.example.administrator.school_design.view.main_view.home_box;
import com.example.administrator.school_design.view.main_view.myself;

import java.util.ArrayList;
import java.util.List;

/*主fragment*/
public  class MainActivity extends back_fragment implements View.OnClickListener  {

    private RelativeLayout re_read;
    private RelativeLayout re_write;
    private RelativeLayout re_history;
    private RelativeLayout re_myself;

    private Fragment f_read;
    private Fragment f_write;
    private Fragment f_history;
    private Fragment f_myself;


    private List<Fragment> mFragments;
    private fragment_new mViewPager;
    private FragmentPagerAdapter mAdapter;


    private ImageView[] imagebuttons;
    private TextView[] textviews;


    private ImageView more;
    private PopupWindow mPopupWindow;

    private TextView title_main;

    private LinearLayout about;
    private  LinearLayout exit;
private View layout_bar;
    private int index1=1;
    private int index2=0;
    private int index3=0;
    private int index4=0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Exitactivity.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);

        init();//初始化

        initable();//初始化下面四个按钮

//        setSelect(0);//默认选择第一个
        mViewPager.setCurrentItem(0);
        textviews[0].setTextColor(0xFEff9900);
  //      more.setVisibility(View.GONE);

    }

    private void initable() {
        //总的底部四个，接下来拆分
        imagebuttons = new ImageView[4];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_read);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_write);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_history);
        imagebuttons[3] = (ImageView) findViewById(R.id.ib_myself);

        imagebuttons[0].setSelected(true);//显示第一部分

        title_main= (TextView) findViewById(R.id.title_main);


        textviews = new TextView[4];
        textviews[0] = (TextView) findViewById(R.id.read);
        textviews[1] = (TextView) findViewById(R.id.write);
        textviews[2] = (TextView) findViewById(R.id.history);
        textviews[3] = (TextView) findViewById(R.id.myself);

        textviews[0].setTextColor(0xFEff9900);//变色


        title_main.setText("家箱");


    }

    private void setSelect(int i) {
        setColor(i);
        setTitle_new(i);
        mViewPager.setCurrentItem(i);

    }

    private void setColor(int currentItem) {
        dark();
        imagebuttons[currentItem].setSelected(true);
        Animation operatingAnim = AnimationUtils.loadAnimation(getApplication(), R.anim.bottom_anim);
        Interpolator lin = new DecelerateInterpolator();
        operatingAnim.setInterpolator(lin);

        if (operatingAnim != null) {
            if (currentItem==0&&index1==0){
                imagebuttons[0].startAnimation(operatingAnim);
                index1=1;index2=0;index3=0;index4=0;
            }
            else if (currentItem==1&&index2==0){
                imagebuttons[1].startAnimation(operatingAnim);
                index1=0;index2=1;index3=0;index4=0;
            }
            else if (currentItem==2&&index3==0){
                imagebuttons[2].startAnimation(operatingAnim);
                index1=0;index2=0;index3=1;index4=0;
            }else if (currentItem==3&&index4==0){
                imagebuttons[3].startAnimation(operatingAnim);
                index1=0;index2=0;index3=0;index4=1;
            }

        }
        textviews[currentItem].setTextColor(0xFEff9900);//变色
    }
    private  void setTitle_new(int currentItem){
        switch (currentItem){
            case 0:
                title_main.setText("家箱");

                break;
            case 1:
                title_main.setText("发现");
                break;
            case 2:
                title_main.setText("订单");

                break;
            case 3:
                title_main.setText("我的");
                break;
        }

}

    private void dark() {
        imagebuttons[0].setSelected(false);
        imagebuttons[1].setSelected(false);
        imagebuttons[2].setSelected(false);
        imagebuttons[3].setSelected(false);

        textviews[0].setTextColor(0xFF5c5c63);
        textviews[1].setTextColor(0xFF5c5c63);
        textviews[2].setTextColor(0xFF5c5c63);
        textviews[3].setTextColor(0xFF5c5c63);


    }


    public void init(){


       re_read= (RelativeLayout) findViewById(R.id.re_read);
        re_write= (RelativeLayout)findViewById(R.id.re_write);
       re_history= (RelativeLayout)findViewById(R.id. re_history);
       re_myself= (RelativeLayout)findViewById(R.id.re_myself);


        more= (ImageView) findViewById(R.id.more);
        layout_bar= findViewById(R.id.layout_bar);

        mViewPager= (fragment_new) findViewById(R.id.fragment_container);


        mFragments=new ArrayList<>();
        //添加主界面
        f_read=new home_box();
        f_write=new find();
        f_history=new history();
        f_myself=new myself();

        //添加fragment
        mFragments.add(f_read);
        mFragments.add(f_write);
        mFragments.add(f_history);
        mFragments.add( f_myself);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());    //动画特效
        mViewPager.setAdapter(mAdapter);
              mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                setSelect(currentItem);
//              if (currentItem==0){
//                  more.setVisibility(View.GONE);
//              }else {
//                  more.setVisibility(View.VISIBLE);
//              }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//设置下方四个按钮的监听事件
        re_read.setOnClickListener(this);
        re_write.setOnClickListener(this);
        re_history.setOnClickListener(this);
        re_myself.setOnClickListener(this);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation operatingAnim = AnimationUtils.loadAnimation(getApplication(), R.anim.more_roation_1);
               Interpolator lin = new DecelerateInterpolator();
                operatingAnim.setInterpolator(lin);
                if (operatingAnim != null) {
                    more.startAnimation(operatingAnim);
                }
                    showPopupWindow();

            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
       lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     * @author cg
     *
     */
     class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }
    //popwindows，界面右上方
    private void showPopupWindow() {

        View contentview= LayoutInflater.from(MainActivity.this).inflate(R.layout.more,null);
        mPopupWindow=new PopupWindow(contentview,
              ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);


        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow.setContentView(contentview);

        about= (LinearLayout) contentview.findViewById(R.id.about);
        exit= (LinearLayout) contentview.findViewById(R.id.exit);
        about.setOnClickListener(listener);
        exit.setOnClickListener(listener);
        mPopupWindow.setAnimationStyle(R.style.contextMenuAnim);
        mPopupWindow.showAsDropDown(more,-330,30);

   mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
       @Override
       public void onDismiss() {
           Animation operatingAnim = AnimationUtils.loadAnimation(getApplication(), R.anim.more_roation_2);
           Interpolator lin = new DecelerateInterpolator();
           operatingAnim.setInterpolator(lin);
           if (operatingAnim != null) {
               more.startAnimation(operatingAnim);
           }
           backgroundAlpha(1f);
       }
   });
    }
//title的点击事件，即右上方的popwindow
View.OnClickListener listener=new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.about:
                Intent intent1=new Intent(MainActivity.this,box_about.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                mPopupWindow.dismiss();
                break;
            case R.id.exit:
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                mPopupWindow.dismiss();
                finish();
                break;

        }

    }
};

    @Override
    public void onClick(View v) {           //点击下方事件
        switch(v.getId())
        {
            case R.id.re_read:
                if (f_read!=null)
//                    layout_bar.setVisibility(View.VISIBLE);
                setSelect(0);
    //            more.setVisibility(View.GONE);
                break;
            case R.id.re_write:

                if (f_write!=null){
                    layout_bar.setVisibility(View.VISIBLE);
                    setSelect(1);
     //               more.setVisibility(View.VISIBLE);
//                    layout_bar.setVisibility(View.GONE);
                }
                break;
            case R.id.re_history:

                if (f_history!=null){
//                    layout_bar.setVisibility(View.VISIBLE);
                    setSelect(2);
          //          more.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.re_myself:

                if (f_myself!=null){
//                    layout_bar.setVisibility(View.VISIBLE);
                    setSelect(3);
           //         more.setVisibility(View.VISIBLE);
                }
                break;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
