package com.example.administrator.school_design.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.diy_item.Exitactivity;


/**
 * Created by Administrator on 2017/6/7.
 * 注册失败界面
 */

public class rigister_fail extends Activity implements View.OnClickListener {
    private TextView fail;
    private  TextView rigister_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rigister_fail);


        fail= (TextView) findViewById(R.id.fail);
        rigister_exit = (TextView) findViewById(R.id.rigister_exit);
        fail.setOnClickListener(this);
        rigister_exit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fail:
                finish();
//                overridePendingTransition(R.anim.push_right_in,
//                        R.anim.push_right_out);
                 break;
            case R.id.rigister_exit:

                finish();

                Exitactivity.getInstance().exit();
                break;
        }
    }
    }
