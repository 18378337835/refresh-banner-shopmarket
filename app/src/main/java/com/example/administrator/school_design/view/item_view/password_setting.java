package com.example.administrator.school_design.view.item_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.activity.back_activity;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.login.box_login;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2017/5/31.
 * 密码设置的界面
 */

public class password_setting extends back_activity {
    private Button change_ok;
    private ImageView setting_back;
    private EditText new_pswd;
    private  EditText old_pswd;
    public  String old_p;
    public String new_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_setting);
        init();
 }

    private void init() {
        old_pswd= (EditText) findViewById(R.id.old_pswd);
        new_pswd= (EditText) findViewById(R.id.new_pswd);
        change_ok= (Button) findViewById(R.id.change_ok);
        change_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             old_p=old_pswd.getText().toString();
             new_p=new_pswd.getText().toString();
                if (old_p.equals("")){
                    Toast.makeText(password_setting.this,"请输入原始密码",Toast.LENGTH_SHORT).show();
                }
            else     if (new_p.equals("")){
                    Toast.makeText(password_setting.this,"请输入修改后的密码",Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                    String thing_idd=sharedPreferences.getString("thing_id",null);
                    BmobQuery<user> query = new BmobQuery<user>();
                    query.getObject(thing_idd, new QueryListener<user>() {

                        @Override
                        public void done(user object, BmobException e) {
                            if(e==null){
                                if (old_p.equals(object.getPwd()))
                                {
                                    SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                                    String thing_idd=sharedPreferences.getString("thing_id",null);
                                    user p2 = new user();
                                    p2.setValue("pwd",new_p);
                                    p2.update(thing_idd, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            finish();
                                            overridePendingTransition(R.anim.push_right_in,
                                                    R.anim.push_right_out);
                                            Toast.makeText(password_setting.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(password_setting.this,"原始密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }

                    });
                }


            }
        });
        setting_back= (ImageView) findViewById(R.id.setting_back);
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);

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
