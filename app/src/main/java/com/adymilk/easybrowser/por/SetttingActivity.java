package com.adymilk.easybrowser.por;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.adymilk.easybrowser.AboutSoft;
import com.adymilk.easybrowser.CommSettingsActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.leon.lib.settingview.LSettingItem;
import com.umeng.analytics.MobclickAgent;

import static com.adymilk.easybrowser.Utils.comm.destoryImmersionBar;
import static com.adymilk.easybrowser.Utils.comm.slideActivity;

import java.util.List;

/**
 * Created by jack on 17-9-21.
 */

public class SetttingActivity extends AppCompatActivity {
    private String appVersion;
    private String appPackName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        slideActivity(this);
        setContentView(R.layout.layout_setting);
        getPackInfo();//获取app版本信息


        LSettingItem comm_setting = (LSettingItem) findViewById(R.id.comm_setting);
        LSettingItem message = (LSettingItem) findViewById(R.id.message);
        LSettingItem about = (LSettingItem) findViewById(R.id.about);
        LSettingItem open_source = (LSettingItem) findViewById(R.id.open_source);
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

        about.setRightText(appVersion);


        //TODO: 通用设置
        comm_setting.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent();
                intent.setClass(SetttingActivity.this, CommSettingsActivity.class);
                startActivity(intent);
            }
        });

        //TODO: 信息反馈
        message.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                if (isQQClientAvailable(SetttingActivity.this)) {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=924114103";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    Toast.makeText(SetttingActivity.this, "未安装QQ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // TODO: 关于软件
        about.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent();
                intent.setClass(SetttingActivity.this, AboutSoft.class);
                startActivity(intent);
            }
        });

        open_source.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                /**
                 * TODO: 开源相关
                 * url:https://github.com/adymilk/xinBrowser
                 */
                Intent intent = new Intent();
                intent.setClass(SetttingActivity.this, com.adymilk.easybrowser.por.Browser.class);//从一个activity跳转到另一个activity
                intent.putExtra("targetUrl", "https://github.com/adymilk/xinBrowser");//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                startActivity(intent);
            }
        });
    }

    //    获取版本号
    public void getPackInfo() {
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            appVersion = packageInfo.versionName;
            appPackName = packageInfo.packageName;
            System.out.println("当前app版本号为：" + appVersion);
            System.out.println("当前app版appPackName为：" + appPackName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //    判断用户是否安装QQ
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
//        mAgentWeb.clearWebCache();
        System.out.println("当前Activity为 onResume");
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 沉浸状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.blue_color)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryImmersionBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
