package com.example.administrator.school_design.view.main_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.diy_item.CircleImg;
import com.example.administrator.school_design.diy_item.pathtoimage;
import com.example.administrator.school_design.login.box_login;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.example.administrator.school_design.R.drawable.ic_sex_female;
import static com.example.administrator.school_design.R.drawable.ic_sex_male;


/**
 * Created by Administrator on 2017/5/4.
 * 个人信息界面
 */

public class myself extends Fragment implements View.OnClickListener {
    private TextView tvname;
    private  TextView tv_work;
    private View view;

    private  LinearLayout address;
    private  LinearLayout password_setting;
    private LinearLayout exit_number;
    private SharedPreferences sharedPreferences;
    private ImageView iv_sex;
    private  String log;
    private CircleImg head;
    private LinearLayout sign;
    private PopupWindow mPopupWindow;
    private  TextView sign_ok;
    private  ImageView delete11;
    private String todayTime;
    private  TextView oksign;

    private ImageView sign_pic;
    private View contentview;
    private PopupWindow mPopupWindow1;

    int currentimageid=0;
    private ImageView write_pic;

    int [] image=new int[]{
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
    };

    int [] images=new int[]{

            R.drawable.qvo,
            R.drawable.qvp,R.drawable.qvq,R.drawable.qvk,R.drawable.qvr,R.drawable.qvs,R.drawable.qvt,R.drawable.qvu,
            R.drawable.qvv,R.drawable.qvw,R.drawable.qvx,R.drawable.qvy};
    int [] durations=new int[]{
            10,  10,10,100,100,100,100,100,100,150,150,150
    };
    private Timer timer=new Timer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
      /* 这个结构为了fragment进入其他菜单当前界面数据保留*/
        if (view==null){

            view=inflater.inflate(R.layout.myself,container,false);//显示界面
            tvname= (TextView) view.findViewById(R.id.tvname);
            tv_work= (TextView) view.findViewById(R.id.tv_work);
            init();//初始化
            isToday();//签到功能的记录保存

        }
        else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }

        }

        //读取数据到界面中
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences(box_login .name+"1", MODE_PRIVATE);
        String a=sharedPreferences.getString("name",null);
        if (a!=null){
            tvname.setText(a);
        }else {
            tvname.setText("请输入");
        }
    String b=sharedPreferences.getString("work",null);
        if (b!=null){
            tv_work.setText(b);
        }else {
            tv_work.setText("暂无");
        }

        log=sharedPreferences.getString("sex",null);
        if (log!=null){

            if (log.equals("男")){
                iv_sex.setImageResource(ic_sex_male);
            }
            else {
                iv_sex.setImageResource(ic_sex_female);
            }
        }

        return view;
    }

    //初始化
    private void init() {
        view.findViewById(R.id.view_user).setOnClickListener(this);
        address= (LinearLayout) view.findViewById(R.id.address);
        address.setOnClickListener(this);
        password_setting= (LinearLayout) view.findViewById(R.id.password_setting);
        password_setting.setOnClickListener(this);
        exit_number= (LinearLayout) view.findViewById(R.id.exit_number);
        exit_number.setOnClickListener(this);
        oksign= (TextView) view.findViewById(R.id.oksign);
        iv_sex= (ImageView) view.findViewById(R.id.iv_sex);
        head= (CircleImg) view.findViewById(R.id.head);
        sign= (LinearLayout) view.findViewById(R.id.sign);
        sign.setOnClickListener(this);


    }



    //点击界面的各个点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_user:
                Intent intent = new Intent(getActivity(),com.example.administrator.school_design.view.item_view.person_profile.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);// 跳转并要求返回值，0代表请求值(可以随便写)
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                break;

            case R.id.address:
                Intent intent2=new Intent(getActivity(),com.example.administrator.school_design.view.item_view.my_ord.class);
                startActivity(intent2);
                break;

            case R.id.password_setting:
                Intent intent3=new Intent(getActivity(), com.example.administrator.school_design.view.item_view.password_setting.class);
                startActivity(intent3);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                break;
            case R.id.exit_number:
                Intent intent4=new Intent(getActivity(),box_login.class);
                intent4.putExtra("index", "b");      // id+"" 这样是把int转成String类型, 否则会报错

                    startActivity(intent4);
                getActivity().finish();

                 getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.sign:
                isTodayFirstLogin();

                break;

        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp =getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
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

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what==0){
                write_pic.setImageResource(image[currentimageid++%image.length]);
            }
            super.handleMessage(msg);
        }
    };

  /*  签到功能的弹框*/
    private void showPopupWindow() {
         contentview= LayoutInflater.from(getActivity()).inflate(R.layout.sign,null);
        mPopupWindow=new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
     mPopupWindow.setOutsideTouchable(true);
    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.8f);
        mPopupWindow.setContentView(contentview);
        mPopupWindow.setAnimationStyle(R.style.history_more);
        mPopupWindow.showAtLocation(contentview, Gravity.CENTER, 0,0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }
});
        sign_ok= (TextView) contentview.findViewById(R.id.sign_ok);
        write_pic= (ImageView) contentview.findViewById(R.id.write_pic);
        delete11= (ImageView) contentview.findViewById(R.id.delete11);

        timer.schedule(new TimerTask() {   //实现动画
            @Override
            public void run() {
                handler1.sendEmptyMessage(0);
            }
        },0,200);


        sign_ok.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                showPopupWindow1();
                sign_ok.setText("已签到");
                sign_ok.setTextColor(0xFF838282);
                oksign.setText("已签到");
                saveExitTime(todayTime);
                sign_ok.setEnabled(false);



            }
        });

        delete11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });

    }
    public class myAnimation{


        private final ImageView mImageView;
        private final int[] durations;
        private final int[] mImageRes;

        public myAnimation(ImageView pImageView, int[] pImageRes,
                           int[] durations) {
            this.mImageView = pImageView;
            this.durations = durations;
            this.mImageRes= pImageRes;
            mImageView.setImageResource(mImageRes[1]);
            play(1);
        }

        private void play(final int pImageNo) {
            mImageView.postDelayed(new Runnable() {     //采用延迟启动子线程的方式
                public void run() {
                    mImageView.setImageResource(mImageRes[pImageNo]);
                    if (pImageNo == mImageRes.length-1)
                        return;
                    else
                        play(pImageNo + 1);
                }
            }, durations[pImageNo-1]);
        }
    }
    private void showPopupWindow1() {
        contentview= LayoutInflater.from(getActivity()).inflate(R.layout.sign_anim,null);
         mPopupWindow1 = new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow1.setContentView(contentview);
//        mPopupWindow1.setAnimationStyle(R.style.history_more);
        mPopupWindow1.showAtLocation(contentview, Gravity.CENTER, 0,0);

        sign_pic= (ImageView) contentview.findViewById(R.id.sign_pic);


        int duration=0;
//        sign_pic.setImageResource(R.drawable.sign_anim);
//        animationDrawable = (AnimationDrawable) sign_pic.getDrawable();
         sign_pic.post(new Runnable() {    //在异步线程中执行启动的方法

            @Override
            public void run() {
                // TODO Auto-generated method stub
                new myAnimation(sign_pic, images,durations);
//                animationDrawable.start();   //启动动画
            }
        });
        for(int i=0;i<images.length;i++){

            duration+=durations[i];     //计算动画播放的时间

        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                                mPopupWindow1.dismiss();
            }
        }, duration);
    }


    /* 进行签到功能的判断和保存*/
    private void isTodayFirstLogin() {
        SharedPreferences preferences =getActivity().getSharedPreferences(box_login.name+"LastLoginTime", MODE_PRIVATE);
        String lastTime = preferences.getString("LoginTime", "2017-04-08");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
         todayTime = df.format(new Date());// 获取当前的日期

        if (lastTime.equals(todayTime)) { //如果两个时间段相等

            showPopupWindow();
            sign_ok.setText("已签到");
            sign_ok.setEnabled(false);
            sign_ok.setTextColor(0xFF838282);

        }else {

            showPopupWindow();
            sign_ok.setEnabled(true);
        }
    }
    private void isToday() {
        SharedPreferences preferences =getActivity().getSharedPreferences(box_login.name+"LastLoginTime", MODE_PRIVATE);
        String lastTime = preferences.getString("LoginTime", "2017-04-08");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        todayTime = df.format(new Date());// 获取当前的日期

        if (lastTime.equals(todayTime)) { //如果两个时间段相等
            oksign.setText("已签到");

        }else {
            oksign.setText("未签到");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();

            String named = bundle.getString("name_ok");
            if (named!=null){
                tvname.setText(named);
                if (tvname!=null){
                    tvname.setText(named);

                    sharedPreferences = getActivity().getSharedPreferences(box_login.name+"1", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("name", named);
                    editor.commit();//提交修改
                }

            }

            String work_sid = bundle.getString("work_ok");
            if (work_sid!=null){
                if (tv_work!=null){
                    tv_work.setText(work_sid);

                    sharedPreferences = getActivity().getSharedPreferences(box_login.name+"1", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("work", work_sid);
                    editor.commit();//提交修改
                }
            }

            String sex = bundle.getString("ss_ok");

            if (sex!=null){
                if (sex.equals("男")){
                    iv_sex.setImageResource(ic_sex_male);

                    sharedPreferences = getActivity().getSharedPreferences(box_login.name+"1", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("sex", sex);
                    editor.commit();//提交修改
                }
                else if (sex.equals("女")){
                    iv_sex.setImageResource(ic_sex_female);

                    sharedPreferences = getActivity().getSharedPreferences(box_login.name+"1", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("sex", sex);
                    editor.commit();//提交修改
                }


            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreference= getActivity().getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
        String paths=sharedPreference.getString("path",null);
        if (paths!=null)
        head.setImageBitmap(pathtoimage.getDiskBitmap(paths));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(getActivity());

    }
    private void saveExitTime(String extiLoginTime) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(box_login.name+"LastLoginTime", MODE_PRIVATE).edit();
        editor.putString("LoginTime", extiLoginTime);
        //这里用apply()而没有用commit()是因为apply()是异步处理提交，不需要返回结果，而我也没有后续操作
        //而commit()是同步的，效率相对较低
        //apply()提交的数据会覆盖之前的,这个需求正是我们需要的结果
        editor.apply();
    }
}

