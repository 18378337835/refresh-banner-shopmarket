package com.example.administrator.school_design.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/4/27.
 */

public class my_shop extends BmobObject {
    public String getUrl() {
        return url;
    }

    private final String url;
    private BmobFile pic;
    private BmobFile pic2;
    private BmobFile pic3;

    private String title;

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public BmobFile getPic2() {
        return pic2;
    }

    public void setPic2(BmobFile pic2) {
        this.pic2 = pic2;
    }

    public BmobFile getPic3() {
        return pic3;
    }

    public void setPic3(BmobFile pic3) {
        this.pic3 = pic3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BmobFile getMy_pic1() {
        return my_pic1;
    }

    public void setMy_pic1(BmobFile my_pic1) {
        this.my_pic1 = my_pic1;
    }

    public BmobFile getMy_pic2() {
        return my_pic2;
    }

    public my_shop(String title, String tip, String price, String address, String url) {
        this.title = title;
        this.tip = tip;
        this.price = price;
        this.address = address;
        this. url =  url;
    }

    public void setMy_pic2(BmobFile my_pic2) {
        this.my_pic2 = my_pic2;
    }

    public BmobFile getMy_pic3() {
        return my_pic3;
    }

    public void setMy_pic3(BmobFile my_pic3) {
        this.my_pic3 = my_pic3;
    }

    public BmobFile getMy_pic4() {
        return my_pic4;
    }

    public void setMy_pic4(BmobFile my_pic4) {
        this.my_pic4 = my_pic4;
    }

    public BmobFile getMy_pic5() {
        return my_pic5;
    }

    public void setMy_pic5(BmobFile my_pic5) {
        this.my_pic5 = my_pic5;
    }

    private String tip;
    private  String price;
    private  String address;

    private BmobFile my_pic1;
    private BmobFile my_pic2;
    private BmobFile my_pic3;
    private BmobFile my_pic4;
    private BmobFile my_pic5;
}
