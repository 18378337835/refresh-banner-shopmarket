package com.example.administrator.school_design.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.school_design.R;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.diy_item.Exitactivity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Administrator on 2017/5/20.
 * 注册界面
 */

public class rigister extends Activity {
    public Button rigister_1;

    public TextView login_1;
    public ImageView back;
    public TextView login_2;
    public TextView rigister_exit;

    public EditText user1;
    public  EditText edit_password_1;
    public  EditText edit_password_re;
    private ProgressDialog pd;
    private PopupWindow mPopupWinndow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Exitactivity.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rigister);
        init();

    }


    public  void init(){
        rigister_1= (Button) findViewById(R.id.rigister_1);
        login_1= (TextView) findViewById(R.id.login_1);
        back= (ImageView) findViewById(R.id.back);

        user1= (EditText) findViewById(R.id.user1);
        edit_password_1= (EditText) findViewById(R.id.edit_password_1);
        edit_password_re= (EditText) findViewById(R.id.edit_password_re);


        rigister_1.setOnClickListener(listen);
        login_1.setOnClickListener(listen);
        back.setOnClickListener(listen);

    }


    View.OnClickListener listen=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             String name;
            String pswd;
            String re_pswd;
            switch (v.getId()){
                case R.id.rigister_1:
                    name = user1.getText().toString().trim();
                     pswd = edit_password_1.getText().toString().trim();
                    re_pswd = edit_password_re.getText().toString().trim();

                    if (name.equals("")) {
                        Toast.makeText(rigister.this,
                                "请您输入用户名！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                   else if (pswd.equals("")) {
                        Toast.makeText(rigister.this,
                                "请您输入密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                 else  if (re_pswd.equals("")) {
                        Toast.makeText(rigister.this,
                                "请您确认输入密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                 else  if (!re_pswd.trim().equals(pswd)&&!re_pswd.trim().equals("")) {
                        Toast.makeText(rigister.this,
                                "请确认两次输入的密码是否相同！", Toast.LENGTH_SHORT).show();
                        return;
                    }

               else {

                        pd = ProgressDialog.show(rigister.this, null, "正在注册，请稍候...");
                        user p2 = new user();
                        p2.setName(name);
                        p2.setPwd(pswd);

                        p2.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId,BmobException e) {
                                if(e==null){
                                    pd.dismiss();
                                    Intent intent=new Intent(rigister.this,rigister_sucess.class);
                                    startActivityForResult(intent,0);
                                    overridePendingTransition(R.anim.m_up_dialog,
                                            R.anim.m_down_dialog);
                          //         Toast.makeText(rigister.this,"添加数据成功，返回objectId为："+objectId,Toast.LENGTH_SHORT).show();

                                }else{
                                    pd.dismiss();
                                    Intent intent=new Intent(rigister.this,rigister_fail.class);
                                    startActivityForResult(intent,0);

                                    overridePendingTransition(R.anim.m_up_dialog,
                                            R.anim.m_down_dialog);
                           //         Toast.makeText(rigister.this,"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                    break;

                case R.id.login_1:
                   finish();
                  overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                    break;
                case R.id.back:
                   finish();
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0&& resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
              String infor = bundle.getString("infor");
            if (infor!=null){
                back.performClick();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        exit();

    }
    public void exit(){
         this.finish();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
         }



}
