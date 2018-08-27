package com.example.administrator.school_design.view.item_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.activity.back_activity;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.login.box_login;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2018/3/11.
 */

public class change_sign extends back_activity {

    private ImageView back_icon;
    private Button sig_ok;
    public String sign_content;
    private EditText edit_sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_sign);

        init();
    }

    private void init() {
        back_icon= (ImageView) findViewById(R.id.back_icon);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);       //过渡动画
            }
        });
        sig_ok= (Button) findViewById(R.id.sig_ok);
        sig_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sign= (EditText) findViewById(R.id.edit_sign_1);
                sign_content=edit_sign.getText().toString();
                if (sign_content.equals("")){
                    Toast.makeText(change_sign.this,"请输入个性签名",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                    String thing_idd=sharedPreferences.getString("thing_id",null);
                    user p2 = new user();
                    p2.setValue("My_signature",sign_content);
                    p2.update(thing_idd, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Intent intent = getIntent();
                                    Bundle bundle = intent.getExtras();
                                    bundle.putString("sign_ok", sign_content);
                                    intent.putExtras(bundle);
                                    setResult(Activity.RESULT_OK, intent);//返回页面1

                                    Toast.makeText(change_sign.this, "更改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.push_right_in,
                                            R.anim.push_right_out);
                                }
                            });


                }


            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }
}
