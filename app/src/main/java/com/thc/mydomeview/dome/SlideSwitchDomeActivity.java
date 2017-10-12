package com.thc.mydomeview.dome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thc.mydomeview.R;

import thc.com.view.slideswitch.SlideSwitch;

/**
 * Created by Nicky on 2017/10/12.
 */

public class SlideSwitchDomeActivity extends AppCompatActivity implements SlideSwitch.OnSlideListener{
    SlideSwitch view_SlideSwitch_one;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideswitch_dome);
        initView();
        initEven();
    }

    private void initView(){
        view_SlideSwitch_one= (SlideSwitch) findViewById(R.id.view_SlideSwitch_one);

    }

    private void initEven(){
        view_SlideSwitch_one.setOnSlideListener(this);
    }
    @Override
    public void onSlideChangCallback(SlideSwitch slideSwitch, boolean isOpen) {

    }
}
