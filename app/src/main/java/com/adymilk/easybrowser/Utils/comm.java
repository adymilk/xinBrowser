package com.adymilk.easybrowser.Utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.adymilk.easybrowser.por.R;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jack on 17-10-5.
 */

public class comm extends Activity {
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

    public static void toastShowShort(Context context, String msg) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {

            mToast.setText(msg);
        }
        mToast.show();
    }

    //屏蔽广告
    public static void adBlock(WebView mWebView) {
        mWebView.loadUrl("javascript: $('.adsbygoogle').remove();$('#topBanner').remove();$('.bar-budejie').remove();$('#content').css('margin-top','-90px');$('#downApp').remove();$('#wrapper .top-bar').remove();");
    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }




    public static void downloadFiles(String url, TextView mTitleTextView, Context context) {
        // http 下载链接（该链接为 CSDN APP 的下载链接，仅做参考）
        String downloadUrl = url;
        // 创建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        /*
        * 设置在通知栏是否显示下载通知(下载进度), 有 3 个值可选:
        *    VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
        *    VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
        *    VISIBILITY_HIDDEN:                    始终不显示通知
        */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 设置通知的标题和描述
        request.setTitle(mTitleTextView.getText());
        request.setDescription("文件下载");
        // 设置下载文件的保存位置
        File saveFile = new File(Environment.getExternalStorageDirectory(), "xinBrowserDownload.apk");
        request.setDestinationUri(Uri.fromFile(saveFile));
         /*
          * 2. 获取下载管理器服务的实例, 添加下载任务
          */
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // 将下载请求加入下载队列, 返回一个下载ID
        long downloadId = manager.enqueue(request);

        // 如果中途想取消下载, 可以调用remove方法, 根据返回的下载ID取消下载, 取消下载后下载保存的文件将被删除
        // manager.remove(downloadId);

    }
}
