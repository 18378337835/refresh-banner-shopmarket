package com.example.administrator.school_design.diy_item;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Administrator on 2017/7/8.
 * 判断网络连接情况
 */

public class internet_connenct {
    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected())
                {
                    // 当前网络是连接的
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                    {
                        // 当前所连接的网络可用
                        return true;
                    }
                }
            }
        }catch (Exception e){
            Log.e("bluetooth","丢失蓝牙")   ;
        }

        return false;
    }

}
