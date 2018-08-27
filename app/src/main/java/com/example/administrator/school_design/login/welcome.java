package com.example.administrator.school_design.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.school_design.MainActivity;
import com.example.administrator.school_design.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/5.
 * 进入程序的欢迎界面，利用定时器自动实现跳转
 */

public class welcome extends Activity {
    private SharedPreferences sharedPreferences;
    private ImageView fab;
    private LinearLayout weltext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        fab =(ImageView) findViewById(R.id.fabe);
        weltext=(LinearLayout) findViewById(R.id.weltext);
        mHandler.sendEmptyMessage(0);

        sharedPreferences=this.getSharedPreferences("userinfo", MODE_PRIVATE);
        box_login.name=sharedPreferences.getString("urname",null);
        box_login.pswd=sharedPreferences.getString("urpasswprd",null);


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //判断是否第一次登陆
                if(TextUtils.isEmpty(box_login.name) &&TextUtils.isEmpty(box_login.pswd)){
                    Intent it = new Intent(welcome.this, box_login.class); //你要转向的Activity
                    startActivity(it); //执行
//                    overridePendingTransition(R.anim.alphto1,
//                            R.anim.alphto0);
                    overridePendingTransition(R.anim.push_left_in,
                            R.anim.push_left_out);
                    finish();

                }
                else {
                    Intent it = new Intent(welcome.this, MainActivity.class); //你要转向的Activity
                    startActivity(it); //执行
//                    overridePendingTransition(R.anim.alphto1,
//                            R.anim.alphto0);
                    overridePendingTransition(R.anim.push_left_in,
                            R.anim.push_left_out);
                    finish();

                }

            }
        };
        timer.schedule(task, 2800); //2秒后
    }

    private void init() {
        Animation operatingAnim = AnimationUtils.loadAnimation(getApplication(), R.anim.myanim);
        Animation operatingAnim1 = AnimationUtils.loadAnimation(getApplication(), R.anim.myanim2);
        Interpolator lin = new DecelerateInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim1.setInterpolator(lin);
        fab.startAnimation(operatingAnim);
        weltext.startAnimation(operatingAnim1);

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0 )
            {
                init();
            }

        }
    };
}
