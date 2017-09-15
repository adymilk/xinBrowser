package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adymilk.easybrowser.por.R;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.open.GameAppOperation;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import me.curzbin.library.BottomDialog;
import me.curzbin.library.Item;
import me.curzbin.library.OnItemClickListener;


/**
 * Created by jack on 17-9-1.
 */

public class ShareToTencent extends Activity {
    private RelativeLayout all_layout;
    private IWXAPI wxApi;
    public static Tencent mTencent;
    private static final String APP_ID = "1106376406";
    private static final String SCOPE = "get_user_info, get_simple_userinfo, add_share";// 权限：读取用户信息并分享信息
    private String 网页标题;
    private String 网页链接;
    private String 要分享的平台;
    private String WX_APP_ID = "wxee53eb68352c793e";

    private Bundle params;
    private String 网页描述;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        网页标题 = bundle.getString("网页标题");
        网页链接 = bundle.getString("网页链接");
        要分享的平台 = bundle.getString("要分享的平台");
        网页描述 = "我正在用昕浏览器看[" + 网页标题 + "]，分享给你，一起来看看吧！";

        // QQ
        mTencent = Tencent.createInstance(APP_ID,ShareToTencent.this);
        //实例化
        wxApi = WXAPIFactory.createWXAPI(this,WX_APP_ID);
        wxApi.registerApp(WX_APP_ID);

        if (mTencent == null) {
            mTencent = Tencent.createInstance(APP_ID, ShareToTencent.this);
        }
        if (要分享的平台.equals("收藏")){
            collectToQQ();
        }

        if (要分享的平台.equals("QQ")){
            shareToQQ();
        }
        if (要分享的平台.equals("QQ空间")){
            shareToQQZone();
        }
        if (要分享的平台.equals("微信")){
            wechatShare(0);
        }
        if (要分享的平台.equals("朋友圈")){
            wechatShare(1);
        }



    }

    public void collectToQQ() {
        final Bundle params = new Bundle();
        params.putString(GameAppOperation.QQFAV_DATALINE_APPNAME, String.valueOf(R.string.app_name));
        params.putString(GameAppOperation.QQFAV_DATALINE_TITLE, 网页标题);
        params.putInt(GameAppOperation.QQFAV_DATALINE_REQTYPE,GameAppOperation.QQFAV_DATALINE_TYPE_DEFAULT);
        params.putString(GameAppOperation.QQFAV_DATALINE_DESCRIPTION, 网页描述);
        params.putString(GameAppOperation.QQFAV_DATALINE_IMAGEURL, "http://oe3vwrk94.bkt.clouddn.com/Browser.png");
        params.putString(GameAppOperation.QQFAV_DATALINE_URL,网页链接 );
        mTencent.addToQQFavorites(ShareToTencent.this, params, qqShareListener);
    }


    public void shareToQQ() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, 网页标题);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, 网页描述);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, String.valueOf(R.string.app_name));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, 网页链接);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://oe3vwrk94.bkt.clouddn.com/Browser.png");
        mTencent.shareToQQ(ShareToTencent.this, params, qqShareListener);
    }


    /**
     * TODO 分享到QQ空间
     */
    private void shareToQQZone(){
        params = new Bundle();
    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, 网页标题);// 标题
    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, 网页描述);// 摘要
    params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, String.valueOf(R.string.app_name));// 摘要
    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,网页链接);// 内容地址
    ArrayList<String> imgUrlList = new ArrayList<>();
    imgUrlList.add("http://oe3vwrk94.bkt.clouddn.com/Browser.png");
    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);// 图片地址
        mTencent.shareToQzone(ShareToTencent.this, params,qqShareListener);

    }


    /**
     * TODO 分享到微信
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */

    private void wechatShare(int flag){

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = 网页链接;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = 网页标题;
        msg.description = 网页描述;
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
                toastShowShort(ShareToTencent.this, "分享取消！");
        }
        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            toastShowShort(ShareToTencent.this, "分享成功！");
        }
        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            toastShowShort(ShareToTencent.this, "分享失败！");
        }
    };



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