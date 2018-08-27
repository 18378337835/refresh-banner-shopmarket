package com.example.administrator.school_design.bean;

/**
 * Created by Administrator on 2017/7/3.
 */

public class define_wite {
     public   int lmi_pic;
    public String lmi_title ;
    public String lmi_tip;
    public   String lmi_price;
    public String lmi_address;
    private boolean mycheck;
private String mcount;

    public String getMcount() {
        return mcount;
    }

    public void setMcount(String mcount) {
        this.mcount = mcount;
    }

    public boolean isMycheck() {
        return mycheck;
    }

    public void setMycheck(boolean mycheck) {
        this.mycheck = mycheck;
    }

    public define_wite(int lmi_pic, String lmi_title, String lmi_tip,  String mcount, String lmi_price,String lmi_address) {
        this.lmi_pic = lmi_pic;
        this.lmi_title = lmi_title;
        this.lmi_tip = lmi_tip;
        this.lmi_price = lmi_price;
        this.lmi_address = lmi_address;
        this.mcount = mcount;
    }

    public define_wite(int lmi_pic, String lmi_title, String lmi_tip, String lmi_price, String lmi_address) {
        this.lmi_pic = lmi_pic;
        this.lmi_title = lmi_title;
        this.lmi_tip = lmi_tip;
        this.lmi_price = lmi_price;
        this.lmi_address = lmi_address;
    }



    public int getLmi_pic() {
        return lmi_pic;
    }

    public void setLmi_pic(int lmi_pic) {
        this.lmi_pic = lmi_pic;
    }

    public String getLmi_title() {
        return lmi_title;
    }

    public void setLmi_title(String lmi_title) {
        this.lmi_title = lmi_title;
    }

    public String getLmi_tip() {
        return lmi_tip;
    }

    public void setLmi_tip(String lmi_tip) {
        this.lmi_tip = lmi_tip;
    }

    public String getLmi_price() {
        return lmi_price;
    }

    public void setLmi_price(String lmi_price) {
        this.lmi_price = lmi_price;
    }

    public String getLmi_address() {
        return lmi_address;
    }

    public void setLmi_address(String lmi_address) {
        this.lmi_address = lmi_address;
    }




}
