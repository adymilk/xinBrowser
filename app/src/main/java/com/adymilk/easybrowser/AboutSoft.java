package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.adymilk.easybrowser.por.R;
import com.adymilk.easybrowser.por.SetttingActivity;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.leon.lib.settingview.LSettingItem;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

import static com.adymilk.easybrowser.por.Utils.slideActivity;

public class AboutSoft extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 沉浸状态栏
        initStatusBar();
        slideActivity(this);

        setContentView(R.layout.about_software);
        webView = (WebView) findViewById(R.id.webView_about);
        LSettingItem join_qq_group = (LSettingItem) findViewById(R.id.join_qq_group);
        LSettingItem donate = (LSettingItem) findViewById(R.id.donate);
        LSettingItem wechat_donate = (LSettingItem) findViewById(R.id.wechat_donate);

        webView.loadUrl("file:///android_asset/about.html");

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //加入QQ群
        join_qq_group.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                joinQQGroup("JXqo5mGe-tD5-KJEhvumvUuMJnweqmA4");
            }
        });

        // 支付宝捐赠
        donate.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                if (AlipayZeroSdk.hasInstalledAlipayClient(AboutSoft.this)) {
                    AlipayZeroSdk.startAlipayClient(AboutSoft.this, "FKX02828RROAVHC0VOT05F");
                } else {
                    Toast.makeText(AboutSoft.this, "未安装支付宝", Toast.LENGTH_SHORT).show();
                }
            }
        });


        wechat_donate.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                /**
                 * TODO: 微信转账二维码
                 * url:https://ww2.sinaimg.cn/thumb300/00674Nvvgw1f83ly0wyw5j30k00joq5a.jpg
                 */
                Intent intent = new Intent();
                intent.setClass(AboutSoft.this, com.adymilk.easybrowser.por.Browser.class);//从一个activity跳转到另一个activity
                intent.putExtra("targetUrl", "https://ww2.sinaimg.cn/thumb300/00674Nvvgw1f83ly0wyw5j30k00joq5a.jpg");//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                startActivity(intent);
            }
        });
    }


    /****************
     *
     * 发起添加群流程。群号：超级脚本软件交流群(539300032) 的 key 为： JXqo5mGe-tD5-KJEhvumvUuMJnweqmA4
     * 调用 joinQQGroup(JXqo5mGe-tD5-KJEhvumvUuMJnweqmA4) 即可发起手Q客户端申请加群 超级脚本软件交流群(539300032)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


    // 沉浸状态栏
    public void initStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.blue_color)
                .fitsSystemWindows(true)
                .init();
    }

    @Override
    protected void onResume() {
//        mAgentWeb.clearWebCache();
        System.out.println("当前Activity为 onResume");
        super.onResume();
    }


}
