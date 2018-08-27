package com.example.administrator.school_design.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/4/28.
 */

public class Commodity_order extends BmobObject {
    private String title;
    private String count;
    private String price;
    private BmobFile pic;
private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String named;
    private String phone;

    public Commodity_order(String title, String count, String price, String ord_url, String state) {
        this.title = title;
        this.count = count;
        this.price = price;
        this.ord_url = ord_url;
        this.state = state;
    }
    public Commodity_order() {

    }

    public String getOrd_url() {

        return ord_url;
    }

    public void setOrd_url(String ord_url) {
        this.ord_url = ord_url;
    }

    private String ord_url;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;

    public String getNamed() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

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
}
