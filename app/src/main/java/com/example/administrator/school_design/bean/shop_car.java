package com.example.administrator.school_design.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/4/26.
 */

public class shop_car extends BmobObject {
    private String title;
    private  String count;
    private String price;
    private BmobFile pic;
    private String car_url;

    public shop_car(String title, String count, String price, String car_url) {
        this.title = title;
        this.count = count;
        this.price = price;
        this.car_url = car_url;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public String getCar_url() {
        return car_url;
    }

    public void setCar_url(String car_url) {
        this.car_url = car_url;
    }
}
