package com.example.administrator.school_design.view.item_view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.school_design.R;
import com.example.administrator.school_design.bean.Commodity_order;
import com.example.administrator.school_design.bean.my_shop;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.login.box_login;
import com.example.administrator.school_design.loopview.why168.LoopViewPagerLayout;
import com.example.administrator.school_design.loopview.why168.listener.OnBannerItemClickListener;
import com.example.administrator.school_design.loopview.why168.loader.OnDefaultImageViewLoader;
import com.example.administrator.school_design.loopview.why168.modle.BannerInfo;
import com.example.administrator.school_design.loopview.why168.modle.IndicatorLocation;
import com.example.administrator.school_design.loopview.why168.modle.LoopStyle;
import com.example.administrator.school_design.loopview.why168.utils.L;
import com.example.administrator.school_design.scrol_title.GradationScrollView;
import com.example.administrator.school_design.scrol_title.ScrollViewContainer;
import com.example.administrator.school_design.scrol_title.StatusBarUtil;
import com.example.administrator.school_design.util.DynamicTimeFormat;
import com.mcxtzhang.lib.AnimShopButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2018/3/17.
 */

public class thing_content extends Activity implements OnBannerItemClickListener, GradationScrollView.ScrollViewListener {
    //标题栏
    private RelativeLayout llTitle;
  private   TextView tvGoodTitle;
    private int height;
    private ScrollViewContainer container;

    private LoopViewPagerLayout mLoopViewPagerLayout1;
    private ImageView shop_back;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private RefreshLayout mRefreshLayout;
    ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
    private TextView shop_name;
    private TextView shop_price;
    private  ImageView imag1;
    private  ImageView imag2;
    private  ImageView imag3;
    private  ImageView imag4;
    private  ImageView imag5;

    private Button shop_car;
    private int tp;
    private String id;
    private PopupWindow mPopupWindow;
    private ImageView address_add_back;
    private EditText edit_named;
    private EditText  edit_phone;
    private EditText   edit_address;
    private Button save_address;
    private ProgressDialog pd;
    private String named;
    private String phone;
    private String address;
    private TextView thing_address;
    private TextView thing_tip;
private Button shop_gou;
    private LinearLayout llOffset;
    private  GradationScrollView scrollView;
    private Activity activity=thing_content.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.thing_content);
        init();
        init_title();
      initListeners();
        initloop();
        init_refresh();
        refresh();
       calculate();

    }
    private void init_title() {
 //       StatusBarUtil.setTranslucentForImageView(this,llOffset);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) llOffset.getLayoutParams();
        params1.setMargins(0,-StatusBarUtil.getStatusBarHeight(this)/4,0,0);
        llOffset.setLayoutParams(params1);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLoopViewPagerLayout1.getLayoutParams();
        params.height = getScreenHeight(this)*2/3;
        mLoopViewPagerLayout1.setLayoutParams(params);

        container = new ScrollViewContainer(getApplicationContext());
    }
    private void initListeners() {

        ViewTreeObserver vto = mLoopViewPagerLayout1.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = mLoopViewPagerLayout1.getHeight();

                scrollView.setScrollViewListener(thing_content.this);
            }
        });
    }

    private void calculate() {

        shop_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( AnimShopButton.mCount==0){
                    Toast.makeText(thing_content.this,"请选择购买数量",Toast.LENGTH_SHORT).show();
                }else {
                    showpopwindow();
                }


            }
        });


    }
    public  int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private void showpopwindow() {
        View contentview= LayoutInflater.from(this).inflate(R.layout.address_add,null);
        mPopupWindow=new PopupWindow(contentview,
                ViewGroup.LayoutParams.MATCH_PARENT,   ViewGroup.LayoutParams.WRAP_CONTENT, true);

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
                    Toast.makeText(thing_content.this,"请输入收件姓名",Toast.LENGTH_SHORT).show();
                }else if (phone.equals("")){
                    Toast.makeText(thing_content.this,"请输入收件电话",Toast.LENGTH_SHORT).show();
                }else if (address.equals("")){
                    Toast.makeText(thing_content.this,"请输入收件地址",Toast.LENGTH_SHORT).show();
                }else {
                    pd = ProgressDialog.show(thing_content.this, null, "正在下单，请稍候...");
                    final String title=shop_name.getText().toString();
                    final String price= shop_price.getText().toString();
                    final String count = String.valueOf(AnimShopButton.mCount);


                    final SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                    String thing_idd=sharedPreferences.getString("thing_id",null);
                    BmobQuery<user> query1 = new BmobQuery<user>();
                    query1.getObject(thing_idd, new QueryListener<user>() {

                        @Override
                        public void done(user object, BmobException e) {
                            if(e==null){
                                //先上传文本
                                Commodity_order Score = new Commodity_order();
                                Score.setCount(count);
                                Score.setTitle(title);
                                Score.setPrice(price);
                                Score.setName(object.getName());
                                Score.setState("等待确认订单");

                                Score.setNamed(named);
                                Score.setPhone(phone);
                                Score.setAddress(address);
                                Score.save(new SaveListener<String>() {

                                    @Override
                                    public void done(final String objectId, BmobException e) {
                                        if(e==null){
                                            id=objectId;
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



                    BmobQuery<my_shop> query = new BmobQuery<my_shop>();
                    query.findObjects(new FindListener<my_shop>() {
                        @Override
                        public void done(List<my_shop> list, BmobException e) {
                            BmobFile pic=list.get(tp).getPic();
                            pic.download(new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {

                                    final BmobFile file = new BmobFile(new File(s));

                                    file.upload(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            Commodity_order Score1 = new Commodity_order();
                                            Score1.setValue("pic",file);
                                            Score1.update(id, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        pd.dismiss();
                                                        Toast.makeText(thing_content.this,"购买成功" ,Toast.LENGTH_SHORT).show();
                                                        Log.i("上传","成功");
                                                        mPopupWindow.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        }
                    });

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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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
        setThemeColor(R.color.need, R.color.need);
        mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);

    }
    private void refresh() {
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout_1);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                addshop();
                refreshlayout.finishRefresh();//传入false表示刷新失败
            }
        });

    }

    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {

        mRefreshLayout.setPrimaryColorsId(colorPrimary, R.color.bgcolor);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimaryDark));
        }
    }
    private void initloop() {

        mLoopViewPagerLayout1.setLoop_duration(2500);//滑动的速率(毫秒)
        mLoopViewPagerLayout1.setLoop_ms(4000);//轮播的速度(毫秒)
        mLoopViewPagerLayout1.setIndicatorLocation(IndicatorLocation.Center);//指示器位置-中Center
        mLoopViewPagerLayout1.setLoop_style(LoopStyle.Zoom);//轮播的样式-深度depth
        mLoopViewPagerLayout1.setNormalBackground(R.drawable.indicator_normal_background);//默认指示器颜色
        mLoopViewPagerLayout1.setSelectedBackground(R.drawable.indicator_selected_background);//选中指示器颜色
        L.e("LoopViewPager Empty 参数设置完毕");
        Context context;
        //TODO 初始化
        mLoopViewPagerLayout1.initializeData(getApplicationContext());

        mLoopViewPagerLayout1.setOnLoadImageViewListener(new OnDefaultImageViewLoader() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onLoadImageView(ImageView view, Object object) {
                if (!activity.isDestroyed())
                    Glide
                            .with(activity)
                            .load(object)
                            .centerCrop()
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load_fail)
                            .crossFade()
                            .into(view);

            }
        });
        mLoopViewPagerLayout1.setOnBannerItemClickListener(this);

       addshop();
    }

    private void addshop() {
        bannerInfos.clear();
        String tap=getIntent().getStringExtra("shop_title");
        tp=Integer.parseInt(tap);      //String转int
        BmobQuery<my_shop> query = new BmobQuery<my_shop>();
        query.findObjects(new FindListener<my_shop>() {
            @Override
            public void done(List<my_shop> object, BmobException e) {
                if(e==null){
                    BmobFile top_pic=object.get(tp).getPic();
                    BmobFile top_pic2=object.get(tp).getPic2();
                    BmobFile top_pic3=object.get(tp).getPic3();
                    top_pic.download(new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {

                            bannerInfos.add(new BannerInfo<String>(s, ""));
                            //TODO 设置数据
                            mLoopViewPagerLayout1.setLoopData(bannerInfos);
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                    top_pic2.download(new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            bannerInfos.add(new BannerInfo<String>(s, ""));
                            //TODO 设置数据
                            mLoopViewPagerLayout1.setLoopData(bannerInfos);
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                    top_pic3.download(new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            bannerInfos.add(new BannerInfo<String>(s, ""));
                            //TODO 设置数据
                            mLoopViewPagerLayout1.setLoopData(bannerInfos);
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });


                    shop_name.setText(object.get(tp).getTitle());
                    shop_price.setText(object.get(tp).getPrice());

                    thing_address.setText(object.get(tp).getAddress());
                    thing_tip.setText(object.get(tp).getTip());
                    final BmobFile img=object.get(tp).getMy_pic1();
                    BmobFile img2=object.get(tp).getMy_pic2();
                    BmobFile img3=object.get(tp).getMy_pic3();
                    BmobFile img4=object.get(tp).getMy_pic4();
                    BmobFile img5=object.get(tp).getMy_pic5();
                    img.download(new DownloadFileListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void done(String s, BmobException e) {
                            //                     Log.i("鲜艳",s);
                            //imag1.setImageBitmap(BitmapFactory.decodeFile(s));   //根据地址解码并显示图片
                            if (!activity.isDestroyed())
                                Glide
                                    .with(activity)
                                    .load(s)
                                    .into(imag1);
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                    img2.download(new DownloadFileListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void done(String s, BmobException e) {
                            //      imag2.setImageBitmap(BitmapFactory.decodeFile(s));   //根据地址解码并显示图片
                            if (!activity.isDestroyed())
                                Glide
                                    .with(activity)
                                    .load(s)
                                    .into(imag2);
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                    img3.download(new DownloadFileListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void done(String s, BmobException e) {
                            if (!activity.isDestroyed())
                                Glide
                                    .with(activity)
                                    .load(s)
                                    .into(imag3);
                            //     imag3.setImageBitmap(BitmapFactory.decodeFile(s));   //根据地址解码并显示图片
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                    img4.download(new DownloadFileListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void done(String s, BmobException e) {
                            if (!activity.isDestroyed())
                                Glide
                                    .with(activity)
                                    .load(s)
                                    .into(imag4);
                            //      imag4.setImageBitmap(BitmapFactory.decodeFile(s));   //根据地址解码并显示图片
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                    img5.download(new DownloadFileListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void done(String s, BmobException e) {
                            if (!activity.isDestroyed())
                                Glide
                                    .with(activity)
                                    .load(s)
                                    .into(imag5);
                            //     imag5.setImageBitmap(BitmapFactory.decodeFile(s));   //根据地址解码并显示图片
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }

    private void init() {
        shop_car= (Button) findViewById(R.id.shop_car);
        scrollView= (GradationScrollView) findViewById(R.id.scrollview);
        container= (ScrollViewContainer) findViewById(R.id.sv_container);
        llOffset= (LinearLayout) findViewById(R.id.ll_offset);
        tvGoodTitle= (TextView) findViewById(R.id.tv_shop_1);
        llTitle= (RelativeLayout) findViewById(R.id.shop_title);
        shop_gou= (Button) findViewById(R.id.shop_gou);

        shop_car.setBackground(this.getResources().getDrawable(R.drawable.diy_button));
        shop_gou.setBackground(this.getResources().getDrawable(R.drawable.diy_button));
        shop_gou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( AnimShopButton.mCount==0){
                    Toast.makeText(getApplicationContext(),"请选择购买数量",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"加入购物车成功",Toast.LENGTH_SHORT).show();
                    AnimShopButton.mCount=0;
                }
            }
        });
       thing_address= (TextView) findViewById(R.id.thing_address);
        thing_tip= (TextView) findViewById(R.id.thing_tip);
        mLoopViewPagerLayout1= (LoopViewPagerLayout) findViewById(R.id.mLoopViewPagerLayout1);
        mRefreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout_1);
        shop_back= (ImageView) findViewById(R.id.shop_back);
        shop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimShopButton.mCount = 0;
                finish();
            }
        });

        shop_name= (TextView) findViewById(R.id.shop_name);
        shop_price= (TextView) findViewById(R.id.shop_price);
         imag1= (ImageView) findViewById(R.id.imag1);
        imag2= (ImageView) findViewById(R.id.imag2);
       imag3= (ImageView) findViewById(R.id.imag3);
        imag4= (ImageView) findViewById(R.id.imag4);
         imag5= (ImageView) findViewById(R.id.imag5);



    }

    @Override
    public void onBannerClick(int index, ArrayList<BannerInfo> banner) {

    }

    @Override
    protected void onStart() {
        mLoopViewPagerLayout1.stopLoop();
        super.onStart();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimShopButton.mCount = 0;
        finish();
    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
        // TODO Auto-generated method stub

        if (y <= 0) {   //设置标题的背景颜色
            llTitle.setBackgroundColor(Color.argb((int) 0, 177,183,237));
            tvGoodTitle.setTextColor(Color.argb(0, 255,255,255));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            tvGoodTitle.setTextColor(Color.argb((int) alpha, 255,255,255));
           llTitle.setBackgroundColor(Color.argb((int) alpha, 177,183,237));

        } else {    //滑动到banner下面设置普通颜色
            llTitle.setBackgroundColor(Color.argb((int) 255, 177,183,237));

        }
    }
}
