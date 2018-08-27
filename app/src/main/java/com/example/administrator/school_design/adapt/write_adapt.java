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
import com.example.administrator.school_design.bean.my_shop_1;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class write_adapt extends BaseAdapter {

    private  LayoutInflater mInflater;
    private List<my_shop_1> mData1;
private   write_adapt.ViewHolder holder;
private  my_shop_1 list;


    private Context context;

    public write_adapt(LayoutInflater inflater, List<my_shop_1> data){
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
        holder = null;
         list = mData1.get(position);

        if (convertView == null) {

            holder = new write_adapt.ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_movie_item, null);
            holder.lmi_pic= (ImageView) convertView.findViewById(R.id.lmi_pic);
            holder.lmi_title = (TextView) convertView.findViewById(R.id.lmi_title);
            holder.lmi_tip = (TextView) convertView.findViewById(R.id.lmi_tip);
            holder.lmi_price= (TextView) convertView.findViewById(R.id.lmi_price);
            holder.lmi_address = (TextView) convertView.findViewById(R.id.lmi_address);

            convertView.setTag(holder);
        } else {
            holder = (write_adapt.ViewHolder) convertView.getTag();
        }



        holder.lmi_title.setText(list.getTitle());
        holder.lmi_tip .setText(list.getTip());
        holder.lmi_price.setText(list.getPrice());
        holder.lmi_address .setText(list.getAddress());



   //    holder.lmi_pic.setImageBitmap(BitmapFactory.decodeFile(list.getUrl()));


       context=mInflater.getContext();
        if(context!=null){
            Glide
                    .with(context)
                    .load(list.getUrl())
                    .into(holder.lmi_pic);
        }


        return convertView ;
    }

    private class ViewHolder {
        public ImageView lmi_pic;
        public TextView lmi_title ;
        public TextView lmi_tip;
        public TextView lmi_price;
        public TextView lmi_address;
    }
}
