package com.example.administrator.school_design.view.main_view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.example.administrator.school_design.R;
import com.example.administrator.school_design.activity.BluetoothChatService;
import com.example.administrator.school_design.activity.DeviceListActivity;
import com.example.administrator.school_design.diy_item.Exitactivity;
import com.example.administrator.school_design.diy_item.PopUtils;
import com.example.administrator.school_design.diy_item.ToastUtil;
import com.example.administrator.school_design.diy_item.internet_connenct;
import com.example.administrator.school_design.login.box_login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.example.administrator.school_design.activity.lanya.isBluetoothEnabled;
import static com.example.administrator.school_design.activity.lanya.turnOnBluetooth;

/**
 * Created by Administrator on 2017/9/4.
 */
public class home_box extends Fragment {

    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter ;
    // 用于通信的服务
    private BluetoothChatService mChatService = null;
    // 来自BluetoothChatService Handler的消息类型
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static  final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";
    public static final String DEVICE_NAME = "device_name";
    private static final int  REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean Islegal;
    private SharedPreferences sharedPreferences ;
private   View view;
 private LinearLayout   is_internet;
 private LinearLayout  other_way;
    private PopupWindow mPopupWindow;
    private PopupWindow mPopupWindow1;
    private PopupWindow mPopupWindow2;
    private PopupWindow mPopupWindow3;
    private PopupWindow mPopupWindow4;
    private TextView lanya_open;
    private TextView lanya_cancel;
    private Button mSendButton;
    private TextView mConversationView;
    private StringBuffer mOutStringBuffer;
    private TextView mtitle_state;
    private String mConnectedDeviceName ;
    private  static int box_1=0;
    private static int box_2=0;
    private ImageView box1;
    private ImageView box2;
    private TextView is_open_kai;
    private TextView is_open_cancel;
    private TextView is_take_kai;
    private TextView is_take_cancel;
    private Button onenet_test;
//onenet定义值
   private TextView onenet_out;
String deviceId = "28374273";
    String datastream = "box_1";
    String minValueString = "1";
    String maxValueString = "4";
    float minValue = Float.parseFloat(minValueString);
    float maxValue = Float.parseFloat(maxValueString);
    private LinearLayout oth_viw;



    class GameThread implements Runnable {
     public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                             Message message = new Message();
                            message.what =0;
                            // 发送消息
                          myHandler.sendMessage(message);
                         try {
                              Thread.sleep(100);
                              } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                 }
                        }
                 }
 }
    // 在onCreate()中开启线程

    // 实例化一个handler
    Handler myHandler = new Handler() {
        //接收到消息后处理
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    is_connect();
                    view.invalidate();//刷新界面
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        if (view==null){
            Exitactivity.getInstance().addActivity(getActivity());
            view=inflater.inflate(R.layout.home_box,container,false);
            init();
            setupChat();//初始化蓝牙

            //箱子初始化
           sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
            box_1=sharedPreferences.getInt("box_1",0);
            box_2=sharedPreferences.getInt("box_2",0);
           box_init();

            new Thread(new GameThread()).start();//开线程刷新实时界面
        }

        else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }



        return view;
    }




    private void box_init() {
        if (box_1==0){
             box1.setImageResource(R.drawable.putthing);
        }else if (box_1==1){
            box1.setImageResource(R.drawable.takething);
        }
        if (box_2==0){
            box2.setImageResource(R.drawable.putthing);
        }else if (box_2==1){
            box2.setImageResource(R.drawable.takething);
        }

    }

    //判断是否有网
    private void is_connect() {
        if ( internet_connenct.isNetworkAvailable(getContext()) ){
            is_internet.setVisibility(View.GONE);
            mtitle_state.setVisibility(View.INVISIBLE);
               oth_viw.setVisibility(View.VISIBLE);
        }else {
            is_internet.setVisibility(View.VISIBLE);
            mtitle_state.setVisibility(View.VISIBLE);
            oth_viw.setVisibility(View.GONE);
        }
    }
    private void sendOnce(String deviceId, String datastream, float value) {
    //    float value = (float) (Math.random() * (maxValue - minValue) + minValue);
        JSONObject request = new JSONObject();
        try {
            request.putOpt(datastream, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OneNetApi.addDataPoints(deviceId, "3", request.toString(), new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                displayLog(response);


            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();

            }
        });
    }
    private void displayLog(String response) {
        if ((response.startsWith("{") && response.endsWith("}")) || (response.startsWith("[") && response.endsWith("]"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            response = gson.toJson(jsonParser.parse(response));
        }
        onenet_out .setText(response);
    }
//初始化
    private void init() {
        oth_viw= (LinearLayout) view.findViewById(R.id.oth_viw);
        oth_viw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName="com.zhongyun.viewer";
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        onenet_out= (TextView) view.findViewById(R.id.onenet_out);
        onenet_test= (Button) view.findViewById(R.id.onenet_test);
        onenet_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnce(deviceId, datastream,1);
            }
        });

        box1= (ImageView) view.findViewById(R.id.box_1);
        box2= (ImageView) view.findViewById(R.id.box_2);
        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (box_1==0){
                        showPopupWindow_1();
                    }else if (box_1==1){
                        showPopupWindow_2();
                    }


            }
        });
        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (box_2==0){
                        showPopupWindow_4();

                    }else if (box_2==1){
                        showPopupWindow_3();
                    }

            }
        });
        other_way= (LinearLayout) view.findViewById(R.id.other_way);
        other_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBluetoothEnabled()){
                    showPopupWindow5();
                }else {
                    showPopupWindow();

                }

            }
        });
        is_internet= (LinearLayout) view.findViewById(R.id.is_internet);
        is_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });


        mtitle_state= (TextView) view.findViewById(R.id.mtitle_state);
    }

    private void showPopupWindow5() {
        PopUtils utils = new PopUtils(getActivity(), R.layout.searchs,   ViewGroup.LayoutParams.WRAP_CONTENT,   ViewGroup.LayoutParams.WRAP_CONTENT,view, Gravity.CENTER, 0, 0, new PopUtils.ClickListener() {
            @Override
            public void setUplistener(final PopUtils.PopBuilder builder) {
                builder.getView(R.id.lanya_search).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search();
                        builder.dismiss();
                    }
                });
                builder.getView(R.id.lanya_search_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        }) {
        };
    }


    private void showPopupWindow() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.lanya_way,null);
        mPopupWindow=new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow.setContentView(contentview);

        lanya_open=(TextView) contentview.findViewById(R.id.lanya_open);
        lanya_cancel=(TextView)  contentview.findViewById(R.id.lanya_cancel);
        lanya_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                turnOnBluetooth();
                if (    turnOnBluetooth()){
                    mPopupWindow.dismiss();
                    backgroundAlpha(1f);
                    showPopupWindow5();
                }else {
                    mPopupWindow.dismiss();
                    backgroundAlpha(1f);
                }

            }
        });
        lanya_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.showAtLocation(contentview, Gravity.CENTER, 0,0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }
    public void showPopupWindow_1() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.is_open,null);
        mPopupWindow1=new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);


        mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow1.setFocusable(true);
        mPopupWindow1.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow1.setContentView(contentview);

        is_open_kai=(TextView) contentview.findViewById(R.id.is_open_kai);
        is_open_cancel=(TextView)  contentview.findViewById(R.id.is_open_cancel);

        is_open_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow1.dismiss();
                backgroundAlpha(1f);
            }
        });
        mPopupWindow1.setAnimationStyle(R.style.history_more);
        mPopupWindow1.showAtLocation(contentview, Gravity.CENTER, 0,0);
        mPopupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        is_open_kai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( internet_connenct.isNetworkAvailable(getContext()) || (internet_connenct.isNetworkAvailable(getContext())&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED) ){
                    sendOnce(deviceId,datastream,1);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendOnce(deviceId,datastream,2);
                            //execute the task
                        }
                    }, 2000);

                    box_1=1;
                    box_init();
                    mPopupWindow1.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_1", box_1);
                    editor.commit();//提交修改
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                }else if (mChatService!=null&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED){
                    sendMessage("5");
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendMessage("6");
                            //execute the task
                        }
                    }, 2000);
                    box_1=1;
                    box_init();
                    mPopupWindow1.dismiss();
                    backgroundAlpha(1f);

                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_1", box_1);

                    editor.commit();//提交修改

                }else if ( !internet_connenct.isNetworkAvailable(getContext())&&mChatService.getState() != BluetoothChatService.STATE_CONNECTED){

                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "请连上homebox后重试",R.drawable.toast_bad);
                    toastUtil.show(3000);

                }



            }
        });
    }

    public void showPopupWindow_2() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.is_take,null);
        mPopupWindow2=new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);


        mPopupWindow2.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow2.setFocusable(true);
        mPopupWindow2.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow2.setContentView(contentview);

        is_take_kai=(TextView) contentview.findViewById(R.id.is_take_kai);
        is_take_cancel=(TextView)  contentview.findViewById(R.id.is_take_cancel);

        is_take_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow2.dismiss();
                backgroundAlpha(1f);
            }
        });
        mPopupWindow2.setAnimationStyle(R.style.history_more);
        mPopupWindow2.showAtLocation(contentview, Gravity.CENTER, 0,0);
        mPopupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });



        is_take_kai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( internet_connenct.isNetworkAvailable(getContext()) || (internet_connenct.isNetworkAvailable(getContext())&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED) ){
                    sendOnce(deviceId,datastream,1);
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendOnce(deviceId,datastream,2);
                            //execute the task
                        }
                    }, 2000);
                    box_1=0;
                    box_init();
                    mPopupWindow2.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_1", box_1);
                    editor.commit();//提交修改
                }else if (mChatService!=null&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED){
                    sendMessage("5");
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendMessage("6");
                            //execute the task
                        }
                    }, 2000);
                    box_1=0;
                    box_init();
                    mPopupWindow2.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_1", box_1);
                    editor.commit();//提交修改
                }else {
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "请连上homebox后重试",R.drawable.toast_bad);
                    toastUtil.show(3000);
                }

            }
        });
    }
    public void showPopupWindow_3() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.is_take,null);
        mPopupWindow3=new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);


        mPopupWindow3.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow3.setFocusable(true);
        mPopupWindow3.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow3.setContentView(contentview);

        is_take_kai=(TextView) contentview.findViewById(R.id.is_take_kai);
        is_take_cancel=(TextView)  contentview.findViewById(R.id.is_take_cancel);

        is_take_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow3.dismiss();
                backgroundAlpha(1f);
            }
        });
        mPopupWindow3.setAnimationStyle(R.style.history_more);
        mPopupWindow3.showAtLocation(contentview, Gravity.CENTER, 0,0);
        mPopupWindow3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        is_take_kai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( internet_connenct.isNetworkAvailable(getContext()) || (internet_connenct.isNetworkAvailable(getContext())&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED) ){
                        sendOnce(deviceId,datastream,3);

                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendOnce(deviceId,datastream,4);
                            //execute the task
                        }
                    }, 2000);
                    box_2=0;
                    box_init();
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    mPopupWindow3.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_2", box_2);
                    editor.commit();//提交修改
                }else if (mChatService!=null&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED){
                    sendMessage("3");

                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendMessage("4");
                            //execute the task
                        }
                    }, 2000);
                    box_2=0;
                    box_init();
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    mPopupWindow3.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_2", box_2);
                    editor.commit();//提交修改
                }else {

                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "请连上homebox后重试",R.drawable.toast_bad);
                    toastUtil.show(3000);
                }

            }
        });

    }
    public void showPopupWindow_4() {
        View contentview= LayoutInflater.from(getActivity()).inflate(R.layout.is_open,null);
        mPopupWindow4=new PopupWindow(contentview,
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);


        mPopupWindow4.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow4.setFocusable(true);
        mPopupWindow4.setOutsideTouchable(true);
        backgroundAlpha( 0.8f);
        mPopupWindow4.setContentView(contentview);

        is_open_kai=(TextView) contentview.findViewById(R.id.is_open_kai);
        is_open_cancel=(TextView)  contentview.findViewById(R.id.is_open_cancel);

        is_open_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow4.dismiss();
                backgroundAlpha(1f);
            }
        });
        mPopupWindow4.setAnimationStyle(R.style.history_more);
        mPopupWindow4.showAtLocation(contentview, Gravity.CENTER, 0,0);
        mPopupWindow4.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        is_open_kai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( internet_connenct.isNetworkAvailable(getContext()) || (internet_connenct.isNetworkAvailable(getContext())&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED) ){
                        sendOnce(deviceId,datastream,3);

                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendOnce(deviceId,datastream,4);
                            //execute the task
                        }
                    }, 2000);
                    box_2=1;
                    box_init();
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    mPopupWindow4.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_2", box_2);
                    editor.commit();//提交修改
                }else if (mChatService!=null&&mChatService.getState() == BluetoothChatService.STATE_CONNECTED){
                    sendMessage("3");
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "开箱成功",R.drawable.toast_ok);
                    toastUtil.show(3000);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            sendMessage("4");
                            //execute the task
                        }
                    }, 2000);
                    box_2=1;
                    box_init();

                    mPopupWindow4.dismiss();
                    backgroundAlpha(1f);
                    sharedPreferences= getActivity().getSharedPreferences(box_login.name+"box_state", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("box_2", box_2);
                    editor.commit();//提交修改
                }else {
                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "请连上homebox后重试",R.drawable.toast_bad);
                    toastUtil.show(3000);
                }

            }
        });

    }
    private void search() {
        Intent intent=new Intent(getActivity(),DeviceListActivity.class);
        startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 停止蓝牙通信连接服务

            mChatService.stop();

        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D)
            Log.e(TAG, "+ ON RESUME +");
        // 在onResume（）中执行此检查包括在onStart（）期间未启用BT的情况，
        // 因此我们暂停启用它...
        // onResume（）将在ACTION_REQUEST_ENABLE活动时被调用返回.
        if (mChatService != null) {
            // 只有状态是STATE_NONE，我们知道我们还没有启动蓝牙
            if (mBluetoothAdapter==null&&mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // 启动BluetoothChat服务
                mChatService.start();
            }
        }

    }
    //返回该Activity回调函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

//search返回的
            case REQUEST_CONNECT_DEVICE:

                // DeviceListActivity返回时要连接的设备
                if (resultCode == Activity.RESULT_OK) {
                    // 获取设备的MAC地址
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    //判断设备的合法性
             //       Islegal=data.getBooleanExtra(DeviceListActivity.
            //                EXTRA_LEGAL,false);

                    // 获取BLuetoothDevice对象
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // 尝试连接到设备
                    mChatService.connect(device);
                }
                break;

   //start返回的（遇到蓝牙不可用退出）
            case REQUEST_ENABLE_BT:
                // 当启用蓝牙的请求返回时
                if (resultCode == Activity.RESULT_OK)
                {
                    //蓝牙已启用，因此设置聊天会话
                 setupChat();//初始化文本

                }
                else
                {
                    // 用户未启用蓝牙或发生错误
                    Log.d(TAG, "BT not enabled");

//                    ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "蓝牙不可用，离开程序",R.drawable.toast_bad);
//                    toastUtil.show(3000);
                   getActivity(). finish();
                }
        }
    }


    private void setupChat() {

        Log.d(TAG, "setupChat()");
        mSendButton = (Button) view.findViewById(R.id.button_send);
        mConversationView = (TextView) view.findViewById(R.id.in);
        mConversationView.setMovementMethod(ScrollingMovementMethod
                .getInstance());// 使TextView接收区可以滚动
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 使用编辑文本小部件的内容发送消息
                TextView edit_text_out = (TextView) view.findViewById(R.id.edit_text_out);
                String message =edit_text_out.getText().toString();
                sendMessage(message);
            }
        });
        // 初始化BluetoothChatService以执行app_incon_bluetooth连接
        // 获取本地蓝牙适配器

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        //初始化外发消息的缓冲区
        mOutStringBuffer = new StringBuffer("");
    }

    //重写发送函数，参数不同。
    private void sendMessage(String message) {
        // 确保已连接
        if (mChatService!=null&&mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {

//            ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, "没有设备连接",R.drawable.toast_bad);
//            toastUtil.show(3000);
            return;
        }
        // 检测是否有字符串发送
        if (message.length() > 0) {
            // 获取 字符串并告诉BluetoothChatService发送
//                byte[] send = Data_syn.hexStr2Bytes(message);
//                mChatService.write(send);
                byte[] send = message.getBytes();
                mChatService.write(send);

            // 清空输出缓冲区
            mOutStringBuffer.setLength(0);
        }else {
            Toast.makeText(getActivity(),"发送内容不能为空",
                    Toast.LENGTH_SHORT).show();
        }
    }
    // 该Handler从BluetoothChatService中获取信息
    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1)
                    {
                        case BluetoothChatService.STATE_CONNECTED:
                            mtitle_state.setText("已连接");
                            mtitle_state.append(mConnectedDeviceName);
                            mConversationView.setText(null);
                            break;

                        case BluetoothChatService.STATE_CONNECTING:
                            mtitle_state.setText("正在连接中.....");
                            break;

                        case BluetoothChatService.STATE_LISTEN:

                        case BluetoothChatService.STATE_NONE:
                            mtitle_state.setText("未连接");
                            break;

                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = null;
                    try {
                        readMessage = new String(readBuf, 0, msg.arg1, "GBK");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    mConversationView.append(readMessage);
                    // 接收计数，更新UI
                    break;

                case MESSAGE_DEVICE_NAME:
                    // 保存已连接设备的名称
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getActivity(),
                            "连接到 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getActivity(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;

            }
        }
    };
}