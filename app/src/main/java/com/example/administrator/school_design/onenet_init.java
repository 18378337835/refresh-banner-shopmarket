package com.example.administrator.school_design;

import android.app.Application;

import com.chinamobile.iot.onenet.OneNetApi;

import cn.bmob.v3.Bmob;


public class onenet_init extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SDK（必须）
        OneNetApi.init(this, true);
        String savedApiKey="etpgeAzuzguBWNK7ss8tecyDjfs=";
        OneNetApi.setAppKey(savedApiKey);
        Bmob.initialize(this, "813d01c46dd4e5c06c06ae6897347b9d");
//        String savedApiKey = Preferences.getInstance(this).getString(Preferences.API_KEY, null);
//        if (savedApiKey != null) {
//            OneNetApi.setAppKey(savedApiKey);
//        }
    }
}
