package com.example.administrator.school_design.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.school_design.R;
import com.example.administrator.school_design.bean.shop_car;
import com.example.administrator.school_design.my_checkbox.SmoothCheckBox;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class history_adapt extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<shop_car> mData1;
    private onChecked checked;
    private Context context;

    public onChecked getChecked() {
        return checked;
    }

    public void setChecked(onChecked checked) {
        this.checked = checked;
    }

    public history_adapt(LayoutInflater inflater, List<shop_car> data){
        mInflater = inflater;
        mData1 = data;

    }
    @Override
    public int getCount() {
        return mData1.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        history_adapt.ViewHolder holder = null;
        shop_car list = mData1.get(position);

        if (convertView == null) {

            holder = new history_adapt.ViewHolder();

            convertView = mInflater.inflate(R.layout.history_list_item, null);
            holder.lmi_pic= (ImageView) convertView.findViewById(R.id.lmi_pic1);
            holder.lmi_title = (TextView) convertView.findViewById(R.id.lmi_title1);
            holder.lmi_tip = (TextView) convertView.findViewById(R.id.lmi_tip1);
            holder.lmi_price= (TextView) convertView.findViewById(R.id.lmi_price1);
            holder.mycheck= (SmoothCheckBox) convertView.findViewById(R.id.my_check);
            holder.mcount= (TextView) convertView.findViewById(R.id.count);

            holder.mycheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        if (checked != null){
            //回调方法传参数
            checked.CheckedChanged(position,isChecked);
        }
    }
});

            convertView.setTag(holder);
        } else {
            holder = (history_adapt.ViewHolder) convertView.getTag();
        }

        //将数据一一添加到自定义的布局中。
        holder.mcount.setText("*"+list.getCount()) ;
        holder.lmi_title.setText(list.getTitle());
        holder.lmi_tip .setText(list.getPrice());
       int a= Integer.parseInt(list.getPrice())* Integer.parseInt((list.getCount()));
        String s = String.valueOf(a);
        holder.lmi_price.setText(s);

     //   holder.mycheck.setChecked(list.isMycheck());
        context=mInflater.getContext();
        if (context!=null){
            Glide
                    .with(context)
                    .load(list.getCar_url())
                    .into(holder.lmi_pic);
        }

        return convertView ;
    }


    private class ViewHolder {
        private ImageView lmi_pic;
        private TextView lmi_title ;
        private TextView lmi_tip;
        private TextView lmi_price;
        public SmoothCheckBox mycheck;
        private TextView mcount;
    }
    public  interface onChecked{
        public void CheckedChanged(int position,boolean isChecked);

    }
}
