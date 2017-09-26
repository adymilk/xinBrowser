package com.adymilk.easybrowser.por;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adymilk.easybrowser.ShareToTencent;
import com.heima.easysp.SharedPreferencesUtils;
import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.ChromeClientCallbackManager;
import com.just.library.DefaultMsgConfig;
import com.just.library.DownLoadResultListener;
import com.just.library.FragmentKeyDown;
import com.just.library.WebDefaultSettingsManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.util.List;

import me.curzbin.library.BottomDialog;
import me.curzbin.library.Item;
import me.curzbin.library.OnItemClickListener;

import com.just.library.PermissionInterceptor;

import static android.Manifest.permission_group.LOCATION;
import static com.adymilk.easybrowser.por.R.*;


/**
 * Created by jack on 17-8-21.
 */

public class AgentWebFragment extends Fragment implements FragmentKeyDown {


    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private TextView mTitleTextView;
    protected AgentWeb mAgentWeb;
    public static final String URL_KEY = "url_key";
    private ImageView mMoreImageView;
    private PopupMenu mPopupMenu;
    private int 书签数量 = 0;
    private String 网页标题;
    private String 网页链接;
    private Tencent mTencent;// 新建Tencent实例用于调用分享方法
    private Intent intent;
    private IWXAPI wxApi;
    private String WX_APP_ID = "wxee53eb68352c793e";
    private WebView mWebView;


    public static AgentWebFragment getInstance(Bundle bundle) {

        AgentWebFragment mAgentWebFragment = new AgentWebFragment();
        if (bundle != null)
            mAgentWebFragment.setArguments(bundle);

        return mAgentWebFragment;

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_agentweb, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//
                .setIndicatorColorWithHeight(-1, 2)//
                .setAgentWebWebSettings(getSettings())//
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setPermissionInterceptor(mPermissionInterceptor)
                .setReceivedTitleCallback(mCallback)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());


        initView(view);


        DefaultMsgConfig.DownLoadMsgConfig mDownLoadMsgConfig=mAgentWeb.getDefaultMsgConfig().getDownLoadMsgConfig();
          mDownLoadMsgConfig.setCancel("放弃");  // 修改下载提示信息，这里可以语言切换
        //优化

    }


    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {
        String TAG = null;

        //AgentWeb 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
        //例如 http//:www.taobao.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， agentWeb 会动态申请权限 ，true 该Url对应页面请求定位失败。
        //该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截 。
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "url:" + url + "  permission:" + permissions + " action:" + action);
            toastShowShort(getContext(),"正在申请权限！");

            return false;
        }
    };





    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
            //do you work
            Toast.makeText(getContext(),"下载完成！",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
            //do you work
            Toast.makeText(getContext(),"下载错误！",Toast.LENGTH_SHORT).show();
        }
    };



    public AgentWebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    public String getUrl() {
        String target = "";

        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "http://www.jd.com";
        }
        return target;
    }

    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            网页标题 = title;
            if (mTitleTextView != null && !TextUtils.isEmpty(title))
                if (title.length() >= 20) {
                    title = title.substring(0, 20) + "...";
                }
            mTitleTextView.setText(title);

        }
    };
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            //Log.i("Info","onProgressChanged:"+newProgress+"  view:"+view);
        }
    };
    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            toastShowShort(getContext(),"onReceivedError");
//            startActivity(new Intent(getContext(), ErrorActivity.class));
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }


        //
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//            LogUtils.i("Info", "mWebViewClient shouldOverrideUrlLoading:" + url);
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://"))
                return false;
            /*else if (isAlipay(view, url))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
                return true;*/

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            mWebView = mAgentWeb.getWebCreator().get();



            String key_block_image = getString(string.block_image);
            String key_zoom = getString(string.zoom);
            String key_auto_phone = getString(string.auto_phone);
            String key_hide_mode = getString(string.hide_mode);
            String key_block_ad = getString(string.block_ad);
            String key_User_Agent = getString(string.User_Agent), defaultUa = getString(string.android);
            Boolean block_image = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key_block_image, true);
            Boolean zoom = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key_zoom, true);
            Boolean hide_mode = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key_hide_mode, true);
            Boolean block_ad = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key_block_ad, true);
            Boolean auto_phone = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key_auto_phone, true);
           String ua = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key_User_Agent, defaultUa);

            switch (ua){
                case "UC浏览器":
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 9_2_1 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/13D15 UCBrowser/10.9.15.793 Mobile");
                    break;
                case "Chrome":
                    mWebView.getSettings().setUserAgentString("User-Agent: Mozilla/5.0 (Linux; U; Android 2.2.1; zh-cn; HTC_Wildfire_A3333 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
                    break;
                case "百度浏览器":
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; CHM-CL00 Build/CHM-CL00) AppleWebKit/534.24 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.24 T5/2.0 baiduboxapp/7.1 (Baidu; P1 4.4.4)");
                    break;
                case "QQ浏览器":
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 5.1; zh-cn; m2 note Build/LMY47D) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/6.6 Mobile Safari/537.36");
                    break;
                case "Safari":
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_5; de-de) AppleWebKit/534.15+ (KHTML, like Gecko) Version/5.0.3 Safari/533.19.4");
                    break;
                case "Iphone":
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B179 Safari/537.4 Maxthon/%s  ");
                    break;
                case "windows":
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/%s Chrome/30.0.1551.0 Safari/537.36");
                    break;

                default:
                    break;
            }

            System.out.println("ua="+ua);
            System.out.println("block_image：" + block_image);
            if (zoom) {
                mWebView.getSettings().setBuiltInZoomControls(true);
            }

            if (block_image) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobNetInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobNetInfo.isConnected()) {
                    mWebView.getSettings().setBlockNetworkImage(true);
                    System.out.println("当前网络状态为手机：" + mobNetInfo);

                } else if (wifiNetInfo.isConnected()) {
                    mWebView.getSettings().setBlockNetworkImage(false);
                    System.out.println("当前网络状态为wifi：" + wifiNetInfo);
                }
            }
            if (auto_phone){
                mWebView.getSettings().setUseWideViewPort(true);
                mWebView.getSettings().setLoadWithOverviewMode(true);
            }


            Log.i("Info", "url:" + url + " onPageStarted  target:" + getUrl());

            if (url.equals(getUrl())) {
//                pageNavigator(View.GONE);
            } else {
//                pageNavigator(View.VISIBLE);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mAgentWeb.uploadFileResult(requestCode, resultCode, data); //1.3.0开始 废弃该api ，没有api代替 ,使用 ActionActivity 绕行该方法 ,降低使用门槛
    }

    protected void initView(View view) {
        mBackImageView = (ImageView) view.findViewById(id.iv_back);
        mLineView = view.findViewById(id.view_line);
        mTitleTextView = (TextView) view.findViewById(id.toolbar_title);
        mBackImageView.setOnClickListener(mOnClickListener);
        mMoreImageView = (ImageView) view.findViewById(id.iv_more);
        mMoreImageView.setOnClickListener(mOnClickListener);
//        pageNavigator(View.GONE);
    }



    private void pageNavigator(int tag) {

        mBackImageView.setVisibility(tag);
//        mLineView.setVisibility(tag);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()) {

                case id.iv_back:

                    if (!mAgentWeb.back())
                        AgentWebFragment.this.getActivity().finish();

                    break;
                case id.iv_more:
                    showShareDialog();
//                    showPoPup(v);
                    break;

            }
        }


    };


    private void openBrowser(String targetUrl) {
        if(!TextUtils.isEmpty(targetUrl)&&targetUrl.startsWith("file://")){
            toastShowShort(this.getContext(),targetUrl+" 该链接无法使用浏览器打开。");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(targetUrl);
        intent.setData(url);
        startActivity(intent);
    }



    private void toCopy(Context context, String text) {

        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));

    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.clearWebCache();
        mAgentWeb.destroyAndKill();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
        //  mAgentWeb.destroy();

    }
    //分享链接
    public void shareText(String shareUrl) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl+" \n分享来自昕浏览器");
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

//    加入书签
    public void asBookmark(){
        书签数量 = SharedPreferencesUtils.init(getContext()).getInt("书签数量");
        书签数量 = 书签数量 + 1;
        SharedPreferencesUtils.init(getContext()).putInt("书签数量",书签数量);
        System.out.println("书签数量为：" + 书签数量 );
        网页标题 = mTitleTextView.getText().toString();
        网页链接 = mAgentWeb.getWebCreator().get().getUrl().toString();

        // 写入书签标题和链接
        SharedPreferencesUtils.init(getContext())
                .putString("bookmarkTitle" + Integer.toString(书签数量), 网页标题 )
                .putString("bookmarkLink" + Integer.toString(书签数量), 网页链接);
        String bookmarkTitle = SharedPreferencesUtils.init(getContext()).getString("bookmarkTitle" + Integer.toString(书签数量));
        String bookmarkUrl = SharedPreferencesUtils.init(getContext()).getString("bookmarkLink" + Integer.toString(书签数量));
        System.out.println("书签为：书签为："+ bookmarkTitle + bookmarkUrl);

        toastShowShort(getContext(), "书签添加成功！");
    }


    /**
     * TODO 分享到社交平台
     * @param item PopuMenu 菜单
     */
    private void shareTo(String item){
        System.out.println("点击了那个菜单"+ item);
        网页标题 = mTitleTextView.getText().toString();
        网页链接 = mAgentWeb.getWebCreator().get().getUrl().toString();
        intent = new Intent();
        intent.putExtra("网页标题", 网页标题);
        intent.putExtra("网页链接", 网页链接);
        intent.putExtra("要分享的平台", item);
        intent.setClass(getContext(),ShareToTencent.class);
        startActivity(intent);
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

    private void showShareDialog(){
        new BottomDialog(getContext())
                .title(string.share_title)             //设置标题
                .layout(BottomDialog.GRID)              //设置内容layout,默认为线性(LinearLayout)
                .orientation(BottomDialog.VERTICAL)     //设置滑动方向,默认为横向
                .inflateMenu(menu.menu_share)         //传人菜单内容
                .itemClick(new OnItemClickListener() {  //设置监听
                    @Override
                    public void click(Item item) {
                        wxApi = WXAPIFactory.createWXAPI(getContext(),WX_APP_ID);
                        wxApi.registerApp(WX_APP_ID);


                        Intent intent = new Intent();

                        switch (item.getTitle()){
                            case "朋友圈":
                                if (!wxApi.isWXAppInstalled()){
                                    toastShowShort(getContext(),"未安装微信客户端！");
                                }else {
                                    shareTo(String.valueOf(item.getTitle()));
                                }
                                break;

                            case "微信":
                                if (!wxApi.isWXAppInstalled()){
                                    toastShowShort(getContext(),"未安装微信客户端");
                                }else {
                                    shareTo(String.valueOf(item.getTitle()));
                                }
                                break;
                            case "QQ":
                                if (!isQQClientAvailable(getContext())){
                                    toastShowShort(getContext(),"未安装QQ客户端");
                                }else {
                                    shareTo(String.valueOf(item.getTitle()));
                                }
                                break;

                            case "QQ空间":
                                if (!isQQClientAvailable(getContext())){
                                    toastShowShort(getContext(),"未安装QQ客户端");
                                }else {
                                    shareTo(String.valueOf(item.getTitle()));
                                }
                                break;

                            case "更多":
                                网页链接 = mAgentWeb.getWebCreator().get().getUrl().toString();
                                shareText(网页链接);
                                break;
                            case "刷新":
                                mAgentWeb.getLoader().reload();
                                toastShowShort(getContext(),"刷新成功！");
                                break;

                            case "复制链接":
                                if (mAgentWeb != null)
                                    toCopy(AgentWebFragment.this.getContext(), mAgentWeb.getWebCreator().get().getUrl());
                                toastShowShort(getContext(),"已复制！");
                                break;

                            case "清除缓存":
                                mAgentWeb.clearWebCache();
                                toastShowShort(getContext(),"清除成功！");
                                break;

                            case "加入书签":
                                asBookmark();
                                break;

                            case "换浏览器打开":
                                if (mAgentWeb != null)
                                    openBrowser(mAgentWeb.getWebCreator().get().getUrl());
                                break;

                            case "收藏":
                                if (!isQQClientAvailable(getContext())){
                                    toastShowShort(getContext(),"未安装QQ客户端");
                                }else {
                                    shareTo(String.valueOf(item.getTitle()));
                                }
                                break;


                            case "设置":

                                intent.setClass(getContext(), SetttingActivity.class);
                                startActivity(intent);
                                break;

                            case "打开书签":

                                intent.setClass(getContext(), com.adymilk.easybrowser.por.BookmarkActivity.class);
                                startActivity(intent);
                                break;

                            default:
                                break;

                        }
                    }
                })
                .show();

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