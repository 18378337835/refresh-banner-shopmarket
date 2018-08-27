package com.example.administrator.school_design.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.school_design.MainActivity;
import com.example.administrator.school_design.R;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.diy_item.Exitactivity;
import com.example.administrator.school_design.diy_item.internet_connenct;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/myself_change/12.
 * 登陆界面
 */
public class box_login extends Activity{
    private CheckBox remember;
    private Button login;
    private Button register;
    private TextView urpassword;
    private TextView urname;
    private SharedPreferences sharedPreferences =null;
    private  boolean exit =false;
    private  boolean isrememner =true;
    public int indexn=0;
    public static  String name;
    public static  String pswd;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Exitactivity.getInstance().addActivity(this);
requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        //登陆记忆
        sharedPreferences=getSharedPreferences("userinfo", MODE_PRIVATE);
       //初始化
        init();
        String sID=getIntent().getStringExtra("index");
        if (sID!=null){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("urname", null);
            editor.putString("urpasswprd", null);
            editor.putBoolean("ischoose", false);
            editor.commit();
            name=null;
            pswd=null;
         }


    }



    private void init() {

        remember= (CheckBox) findViewById(R.id.remember);
        login= (Button) findViewById(R.id.login);
        urpassword= (TextView) findViewById(R.id.edit_password);
        urname= (TextView) findViewById(R.id.edit_name);
        register= (Button) findViewById(R.id.register);
        login.setOnClickListener(login_listenner);
        register.setOnClickListener(register_listenner);

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isrememner=true;
                if (isChecked==false)
                    isrememner=false;
            }
        });
        if(sharedPreferences.getBoolean("ischoose",false)){
            urname.setText(sharedPreferences.getString("urname",null));
            urpassword.setText(sharedPreferences.getString("urpasswprd",null));
               }

        if (isrememner) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("urname", name);
            editor.putString("urpasswprd", pswd);
            editor.putBoolean("ischoose", true);
            editor.commit();
        }
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("urname", null);
            editor.putString("urpasswprd", null);
            editor.putBoolean("ischoose", false);
            editor.commit();
        }

    }


    private SharedPreferences sharedPreferences1;
    private View.OnClickListener login_listenner= new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           name = urname.getText().toString();
            pswd = urpassword.getText().toString();
           if (name.trim().equals("")) {       //判断用户名是否为空
               Toast toast = Toast.makeText(box_login.this,
                       "请您输入用户名！", Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.CENTER, 0, 0);
               toast.show();
               return;
           }
        else  if (pswd.trim().equals("")) {        //判断密码是否为空
               Toast toast = Toast.makeText(box_login.this,
                       "请您输入密码！", Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.CENTER, 0, 0);
               toast.show();
               return;
           }
           //判断网络情况，ok则登陆服务器返回数据，否则报无网络
       else   if ( internet_connenct.isNetworkAvailable(getApplication()) ){
              pd = ProgressDialog.show(box_login.this, null, "正在登陆，请稍候...");

            //--and条件1
               BmobQuery<user> eq1 = new BmobQuery<user>();
               eq1.addWhereEqualTo("name",name);
       //--and条件2
               BmobQuery<user> eq2 = new BmobQuery<user>();
               eq2.addWhereEqualTo("pwd",pswd);
               //最后组装完整的and条件
               List<BmobQuery<user>> andQuerys = new ArrayList<BmobQuery<user>>();
               andQuerys.add(eq1);
               andQuerys.add(eq2);
               //查询符合整个and条件的人
               BmobQuery<user> query = new BmobQuery<user>();
               query.and(andQuerys);
               query.findObjects(new FindListener<user>() {
                   @Override
                   public void done(List<user> object, BmobException e) {
                       if(e==null&&object.size()!=0){
                           sharedPreferences1= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor1 = sharedPreferences1.edit();//获取编辑器
                           editor1.putString("thing_id",object.get(0).getObjectId() );
                           editor1.commit();//提交修改

                           Log.i("bmob","tag："+object.size());
                           pd.dismiss();
                           if (isrememner) {
                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               editor.putString("urname", name);
                               editor.putString("urpasswprd", pswd);
                               editor.putBoolean("ischoose", true);
                               editor.putInt("index",indexn);

                               editor.commit();
                           }
                           Toast toast = Toast.makeText(box_login.this,
                                   "登陆成功", Toast.LENGTH_SHORT);
                           toast.setGravity(Gravity.CENTER, 0, 0);
                           toast.show();

                          Intent intent =new Intent(box_login.this,MainActivity.class);
                         startActivity(intent);
                          finish();

                       }else {
                           pd.dismiss();
                           Toast toast = Toast.makeText(box_login.this,
                                   "账户或者密码错误", Toast.LENGTH_SHORT);
                           toast.setGravity(Gravity.CENTER, 0, 0);
                           toast.show();
                           Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                       }
                   }
               });



           }
           else {
               Toast toast = Toast.makeText(box_login.this,
                       "请连接网络！", Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.CENTER, 0, 0);
               toast.show();
           }



            }
   };


    View.OnClickListener register_listenner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(box_login.this,rigister.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }
    };

    //页面退出函数
    public void exit(){
        if(exit){
           this.finish();
        }
        else
            Toast.makeText(this,"再按一次，退出程序", Toast.LENGTH_SHORT).show();
        exit = true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

}
