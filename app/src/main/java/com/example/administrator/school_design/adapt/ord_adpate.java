package com.example.administrator.school_design.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.school_design.R;
import com.example.administrator.school_design.bean.Commodity_order;

import java.util.List;

/**
 * Created by Administrator on 2018/4/29.
 */

public class ord_adpate extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Commodity_order> mData1;
    private   ViewHolder holder;
    private Commodity_order list;


    private Context context;
    private onSwipeListener mOnSwipeListener;
    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }
    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public ord_adpate(LayoutInflater inflater, List<Commodity_order> data){
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

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.buying, null);
            holder.buying_pic1= (ImageView) convertView.findViewById(R.id.buying_pic1);
            holder.buying_title1 = (TextView) convertView.findViewById(R.id.buying_title1);
            holder. buying_tip1 = (TextView) convertView.findViewById(R.id.buying_tip1);
            holder.buying_count= (TextView) convertView.findViewById(R.id.buying_count);
            holder.buying_state = (TextView) convertView.findViewById(R.id.buying_state);
            holder.buying_price1 = (TextView) convertView.findViewById(R.id.buying_price1);

            holder.btnTop= (Button) convertView.findViewById(R.id.btnTop);
            holder.btnDelete= (Button) convertView.findViewById(R.id.btnDelete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//获取文字
        holder. buying_title1.setText(list.getTitle());
        holder. buying_tip1 .setText(list.getPrice());
        holder. buying_count .setText("*"+list.getCount());
        double a= Integer.parseInt(list.getPrice())* Integer.parseInt((list.getCount()));

        String s = String.valueOf(a);
        holder. buying_price1.setText(s);

        holder.buying_state.setText(list.getState());
        //获取图片
        context=mInflater.getContext();
        if(context!=null){
            Glide
                    .with(context)
                    .load(list.getOrd_url())
                    .into(holder.buying_pic1);
        }


       holder. btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });

        return convertView ;
    }

    private class ViewHolder {
        public ImageView buying_pic1;
        public TextView buying_title1 ;
        public TextView buying_tip1;
        public TextView buying_count;
        public TextView buying_price1;
        public TextView buying_state;

        private Button btnDelete;
        private Button btnTop;
    }


}
