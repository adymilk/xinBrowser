package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.just.library.AgentWeb;
import com.just.library.PermissionInterceptor;
import com.umeng.analytics.MobclickAgent;

import static com.adymilk.easybrowser.por.Utils.destoryImmersionBar;
import static com.adymilk.easybrowser.por.Utils.hideBar;
import static com.adymilk.easybrowser.por.Utils.slideActivity;


public class CardView1Activity extends Activity {
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;
    private WebView mWebView;
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String targetUrl=bundle.getString("targetUrl");

        hideBar(this);
        slideActivity(this);

        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setPermissionInterceptor(mPermissionInterceptor)
                .createAgentWeb()
                .ready()
                .go(targetUrl);

        mWebView=mAgentWeb.getWebCreator().get();

    }


    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {
        String TAG = null;

        //AgentWeb 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
        //例如 http//:www.taobao.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， agentWeb 会动态申请权限 ，true 该Url对应页面请求定位失败。
        //该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截 。
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "url:" + url + "  permission:" + permissions + " action:" + action);
            toastShowShort(CardView1Activity.this,"正在申请权限！");

            return false;
        }
    };

    @Override
    protected void onResume() {
//        mAgentWeb.clearWebCache();
        System.out.println("当前Activity为 onResume");
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        System.out.println("当前Activity为 onPause");
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideBar(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryImmersionBar();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toastShowShort(Context context, String msg){
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {

            mToast.setText(msg);
        }
        mToast.show();
    }

}