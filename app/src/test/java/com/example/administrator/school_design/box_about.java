package com.example.administrator.school_design;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.school_design.activity.back_activity;
import com.example.administrator.school_design.diy_item.GifView;

/**
 * Created by Administrator on 2017/5/20.
 * “关于”界面
 */

public class box_about extends back_activity {
    private TextView title_main;
    private ImageView more;
    private  ImageView back_icon;
    private GifView gifView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.nfc_about);
        title_main= (TextView) findViewById(R.id.title_main);
        title_main.setText("关于");
        more= (ImageView) findViewById(R.id.more);
        more.setVisibility(View.GONE);
        back_icon= (ImageView) findViewById(R.id.back_icon);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        gifView= (GifView) findViewById(R.id.gif);
        gifView.setMovieResource(R.drawable.test1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }
}
