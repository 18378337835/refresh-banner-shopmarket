package com.example.administrator.school_design.diy_item;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 * 单列模式，对该应用所有的Avtivity的堆栈进行记录，实现对activity的对象进行的管理
 * 总体退出activity
 */
public class Exitactivity extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static Exitactivity instance;
    private Exitactivity()
    {
    }
    public static Exitactivity getInstance(){
       if(null==instance)
       {
           instance=new Exitactivity();
       }
       return instance;
   }
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
