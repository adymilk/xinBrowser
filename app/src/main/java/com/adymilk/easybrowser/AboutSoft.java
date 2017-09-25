package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adymilk.easybrowser.por.R;
import com.gyf.barlibrary.ImmersionBar;
import com.leon.lib.settingview.LSettingItem;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

public class AboutSoft extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_software);

        // 沉浸状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .init();


        ImageView iv_back = findViewById(R.id.iv_back);
        LSettingItem join_qq_group = findViewById(R.id.join_qq_group);
        LSettingItem donate = findViewById(R.id.donate);


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


        //返回上一个界面
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void toastShowShort(Context context, String msg) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {

            mToast.setText(msg);
        }
        mToast.show();
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


    @Override
    protected void onResume() {
//        mAgentWeb.clearWebCache();
        System.out.println("当前Activity为 onResume");
        super.onResume();
    }


}
