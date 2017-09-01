package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.just.library.AgentWeb;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class CardView1Activity extends Activity {
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String targetUrl=bundle.getString("targetUrl");

        initBarAndSildeActivity();

        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()
                .ready()
                .go(targetUrl);

        mWebView=mAgentWeb.getWebCreator().get();




    }
    public void initBarAndSildeActivity(){
        // 沉浸状态栏
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();

        //滑动隐藏 Activity
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .build();
        Slidr.attach(this, config);
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
//        mAgentWeb.destroyAndKill();
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}