package com.adymilk.easybrowser.por;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.widget.Toast;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

/**
 * Created by jack on 17-10-5.
 */

public class Utils extends Activity{
    private static ImmersionBar mImmersionBar;

    public static void initBar(Context context){
        // 沉浸状态栏
        ImmersionBar.with((Activity) context)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .init();
    }

    //滑动隐藏 Activity
    public static void slideActivity(Context context){
        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .edge(true)
                .build();
        Slidr.attach((Activity) context, config);
    }

    //不显示状态栏
    public static void hideBar( Context context){
        ImmersionBar.with((Activity) context)
                .fullScreen(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    public static void destoryImmersionBar(){
        if (mImmersionBar != null){
            mImmersionBar.destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }
    }

    private static void toastShowShort(Context context, String msg) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {

            mToast.setText(msg);
        }
        mToast.show();
    }


}
