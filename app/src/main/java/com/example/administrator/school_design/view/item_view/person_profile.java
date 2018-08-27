package com.example.administrator.school_design.view.item_view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
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
import com.example.administrator.school_design.activity.back_activity;
import com.example.administrator.school_design.bean.user;
import com.example.administrator.school_design.diy_item.CircleImg;
import com.example.administrator.school_design.diy_item.FileUtil;
import com.example.administrator.school_design.diy_item.GifView;
import com.example.administrator.school_design.diy_item.pathtoimage;
import com.example.administrator.school_design.login.box_login;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.administrator.school_design.R.drawable.ic_sex_female;
import static com.example.administrator.school_design.R.drawable.ic_sex_male;

/**
 * Created by Administrator on 2017/5/23.
 * 个人信息的界面，包括修改头像，姓名，id
 */
public class person_profile extends back_activity {
    private TextView name;
    public String change_name;
    public String change_work_id;
    public String change_sign;
    private TextView work_id;
    private  int index;
    private TextView sex;
public  SharedPreferences sharedPreferences;
    private String sex_content;
    private CircleImg photo;
    private PopupWindow mPopupWindow;
    private TextView take_photo;
    private TextView select_photo;
private TextView name_2;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private String urlpath;
    private ProgressDialog pd;
  //  private String resultStr = "";	// 服务端返回结果集
    // 上传服务器的路径【一般不硬编码到程序中】
  //  private String imgUrl = "http://123.207.54.219:8080/NfcPlatform/applogin.do";

    private File temp;
    private Drawable drawable;
    private GifView infor;
    private LinearLayout name_set;
    private LinearLayout sex_set;
    private LinearLayout work_id_set;

    private  int  index1=0;
    private ImageView back_2;
    private ImageView sex_1;
private LinearLayout my_sign_1;
    private TextView sign_shi;
    private  TextView my_sign;
    private TextView select_photo_cancel;
    //gif的消息传递，架包为GIFView
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
// 接收消息
            if (msg.what == 0) {
              infor.setMovieResource(R.drawable.infor);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_profile);
        init();

//界面读取上次的记录
        SharedPreferences sharedPreferences= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
        String a=sharedPreferences.getString("name_1",null);
        if (a!=null){
            name.setText(a);
        }else {
            name.setText("(未填写)");
        }
      String b=sharedPreferences.getString("work_1",null);
        if (b!=null){
            work_id.setText(b);
        }else {
            work_id.setText("(未填写)");
        }
    String c=sharedPreferences.getString("sex_1",null);
        if (c!=null){
            sex.setText(c);
            if (c.equals("男")){
                sex_1.setImageResource(ic_sex_male);
            }
            else {
                sex_1.setImageResource(ic_sex_female);
            }
        }else {
            sex.setText("(未选择)");
        }
        String d=sharedPreferences.getString("name_2",null);
        if (d!=null){
            name_2.setText(d);
        }else {
            name_2.setText("(未填写)");
        }
        String paths=sharedPreferences.getString("path",null);
      photo.setImageBitmap(pathtoimage.getDiskBitmap(paths));

        String e=sharedPreferences.getString("sign_1",null);
        if (e!=null){
            sign_shi.setText(e);
            my_sign .setText(e);
        }else {
            sign_shi.setText("(未填写)");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void init() {
        my_sign= (TextView) findViewById(R.id.my_sign);
        sign_shi= (TextView) findViewById(R.id.sign_shi);
        my_sign_1= (LinearLayout) findViewById(R.id.my_sign_1);
        my_sign_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(person_profile.this,change_sign.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 6);// 跳转并要求返回值，0代表请求值(可以随便写)
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        infor= (GifView) findViewById(R.id.infor);
        name_set= (LinearLayout) findViewById(R.id.name_set);
        work_id_set= (LinearLayout) findViewById(R.id.work_id_set);
        sex_set= (LinearLayout) findViewById(R.id.sex_set);
        sex_1= (ImageView) findViewById(R.id.sex_1);
        name_2= (TextView) findViewById(R.id.name_2);
        back_2= (ImageView) findViewById(R.id.back_2);
        back_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        work_id= (TextView) findViewById(R.id.work_id);
        work_id_set.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(person_profile.this,change_workid.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);// 跳转并要求返回值，0代表请求值(可以随便写)
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });



        name= (TextView) findViewById(R.id.name);
        name_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(person_profile.this, com.example.administrator.school_design.view.item_view.change_name.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值(可以随便写)
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        sex= (TextView) findViewById(R.id.sex);
        sex_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(person_profile.this,sex_select.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });
        photo= (CircleImg) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow_photo();
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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
//点击头像弹出的powindows框
    private void showPopupWindow_photo() {
        View contentview= LayoutInflater.from(person_profile.this).inflate(R.layout.photo_select,null);
        mPopupWindow=new PopupWindow(contentview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setContentView(contentview);
        mPopupWindow.showAtLocation(contentview, Gravity.CENTER, 0,0);

        take_photo= (TextView) contentview.findViewById(R.id.take_photo);
        select_photo= (TextView)contentview. findViewById(R.id.select_photo);
        select_photo_cancel= (TextView)contentview. findViewById(R.id.select_photo_cancel);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow.dismiss();
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, 3);
            }

        });
        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 4);
            }
        });
        select_photo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 4:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case 3:// 调用相机拍照
                temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case 5:// 取得裁剪后的图片

                if (data != null) {
                    setPicToView(data);
                }
                break;
        }


//回传数据到个人信息的界面中
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            index=0;
            Bundle bundle = data.getExtras();
            change_name = bundle.getString("name_ok");
           if (change_name!=null)
                name.setText(change_name);
                name_2.setText(change_name);

            sharedPreferences = getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("name_1", change_name);
            editor.putString("name_2", change_name);
            editor.commit();//提交修改
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            index=1;
            Bundle bundle = data.getExtras();
            change_work_id = bundle.getString("work_ok");
            if (change_work_id!=null)
            work_id.setText(change_work_id);

            sharedPreferences = getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("work_1", change_work_id);
            editor.commit();//提交修改
        }

        if (requestCode == 6 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            change_sign = bundle.getString("sign_ok");
            if (change_sign!=null)
                sign_shi.setText(change_sign);
            my_sign .setText(change_sign);
            sharedPreferences = getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("sign_1", change_sign);
            editor.commit();//提交修改
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            sex_content = bundle.getString("sex_ok");
            if (sex_content!=null)
                sex.setText(sex_content);
            if (sex_content.equals("男")){
                sex_1.setImageResource(ic_sex_male);
            }
            else {
                sex_1.setImageResource(ic_sex_female);
            }
            sharedPreferences = getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("sex_1", sex_content );
            editor.commit();//提交修改
        }



        Intent intent =getIntent();
         Bundle bundle = intent.getExtras();
         bundle.putString("name_ok", change_name);
            bundle.putString("work_ok",change_work_id);
        bundle.putString("ss_ok",sex_content);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);//返回页面1





    }

    //图片裁剪
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 650);
        intent.putExtra("outputY", 650);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 5);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo1 = extras.getParcelable("data");
             drawable = new BitmapDrawable(null, photo1);
            urlpath = FileUtil.saveFile(person_profile.this, box_login.name+"temphead.jpg", photo1);

//             新线程后台上传服务端
            pd = ProgressDialog.show(person_profile.this, null, "正在上传图片，请稍候...");
            final BmobFile file = new BmobFile(new File(urlpath));
            file.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    SharedPreferences sharedPreferences1= getSharedPreferences(box_login.name+"pathtoimage", Context.MODE_PRIVATE);
                    String thing_idd=sharedPreferences1.getString("thing_id",null);
                    user p2 = new user();
                    p2.setValue("img",file);
                    p2.update(thing_idd, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                pd.dismiss();
                                Log.i("bmob","更新成功");
                                photo.setImageDrawable(drawable);

                                sharedPreferences = getSharedPreferences(box_login.name+"pathtoimage", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                                editor.putString("path", urlpath);
                                editor.commit();//提交修改
                            }else{
                                Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }

                    });

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }



}
