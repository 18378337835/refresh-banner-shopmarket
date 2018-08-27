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
 * Created by Administrator on 2017/5/27.
 * 更改工作号的ativity
 */

public class change_workid extends back_activity {
    private ImageView back_icon;
    private Button workid_ok;
    public String work_content;
    private EditText edit_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_workid);
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
                        R.anim.push_right_out);
            }
        });

        workid_ok= (Button) findViewById(R.id.work_ok);
        workid_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_id= (EditText) findViewById(R.id.edit_work);
                work_content=edit_id.getText().toString();
                if (work_content.equals("")){
                    Toast.makeText(change_workid.this,"请输入工作号",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                    String thing_idd=sharedPreferences.getString("thing_id",null);
                    user p2 = new user();
                    p2.setValue("work_id",work_content);
                    p2.update(thing_idd, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Intent intent = getIntent();
                                    Bundle bundle = intent.getExtras();
                                    bundle.putString("work_ok", work_content);
                                    intent.putExtras(bundle);
                                    setResult(Activity.RESULT_OK, intent);//返回页面1

                                    finish();
                                    overridePendingTransition(R.anim.push_right_in,
                                            R.anim.push_right_out);
                                    Toast.makeText(getApplicationContext(), "更改成功", Toast.LENGTH_SHORT).show();
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
