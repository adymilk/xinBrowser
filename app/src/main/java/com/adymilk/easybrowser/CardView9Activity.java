package com.adymilk.easybrowser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.just.library.AgentWeb;

public class CardView9Activity extends Activity {
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        沉浸状态栏
        ImmersionBar.with(this)
                .init();
        AgentWeb mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
//                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go("http://m.amap.com/nearby/index/src=uc_index");// 高德地图


    }

    @Override
    protected void onDestroy() {
        finish();
        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        System.out.println("onDestory");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        System.out.println("onPause");
        super.onPause();
    }


}