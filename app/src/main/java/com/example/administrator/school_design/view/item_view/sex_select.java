package com.example.administrator.school_design.view.item_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.login.box_login;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2017/6/26.
 * 选择性别
 */

public class sex_select extends Activity {
    private TextView sex_women;
    private TextView sex_men;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sex_select);
        init();


    }

    private void init() {
        sex_men= (TextView) findViewById(R.id.sex_men);
        sex_women= (TextView) findViewById(R.id.sex_women);
        sex_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                String thing_idd=sharedPreferences.getString("thing_id",null);
                user p2 = new user();
                p2.setValue("sex","男");
                p2.update(thing_idd, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        Intent intent =getIntent();
                        Bundle bundle = intent.getExtras();
                        bundle.putString("sex_ok","男" );
                        intent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, intent);//返回页面1
                        finish();
                        Toast.makeText(sex_select.this,"更改成功",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

sex_women.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
        String thing_idd=sharedPreferences.getString("thing_id",null);
        user p2 = new user();
        p2.setValue("sex","女");
        p2.update(thing_idd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Intent intent =getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("sex_ok","女" );
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);//返回页面1
                finish();
                Toast.makeText(sex_select.this,"更改成功",Toast.LENGTH_SHORT).show();
            }
        });

    }
});
sex_select.this.setFinishOnTouchOutside(true);
    }


}
