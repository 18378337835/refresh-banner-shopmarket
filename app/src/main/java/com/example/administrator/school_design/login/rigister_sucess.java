package com.example.administrator.school_design.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.diy_item.Exitactivity;


/**
 * Created by Administrator on 2017/6/5.
 * 注册成功界面
 */

public class rigister_sucess extends Activity implements View.OnClickListener {
    private TextView login_2;
    private  TextView rigister_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rigister_sucess);

        login_2= (TextView)findViewById(R.id.login_2);
        rigister_exit= (TextView) findViewById(R.id.rigister_exit);
        login_2.setOnClickListener(this);
        rigister_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_2:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                Intent intent =getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("infor", "yes");
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);//返回页面1



                break;
            case R.id.rigister_exit:

                finish();

                Exitactivity.getInstance().exit();
                break;
        }
    }
}
