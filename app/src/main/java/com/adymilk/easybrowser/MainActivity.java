package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adymilk.easybrowser.por.BookmarkActivity;
import com.adymilk.easybrowser.por.Browser;
import com.adymilk.easybrowser.por.R;
import com.adymilk.easybrowser.por.SetttingActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.heima.easysp.SharedPreferencesUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private String searchEngines;
    private Intent intent;
    //声明相关变量
    private ImageView btn_submit;
    private String searchKey;

    private LinearLayout cardview_content;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private CardView cardView5;
    private CardView cardView6;
    private CardView cardView7;
    private CardView cardView8;
    private EditText editText;
    private Spinner spinner;
    private ImageView scaner;
    private IWXAPI wxApi;
    private String WX_APP_ID = "wxee53eb68352c793e";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("当前Activity状态为onCreate");
        // 用昕浏览器打开链接
        openBrowser();
        // 沉浸状态栏
        initStatusBar();

        setContentView(R.layout.activity_main);

        findViews(); //获取控件


        TextView tv_app_name = findViewById(R.id.app_name);
        tv_app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);
            }
        });
        ImageView imv_setting = findViewById(R.id.setting);
        imv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetttingActivity.class);
                startActivity(intent);
            }
        });

        cardView1.setOnClickListener(new MainActivity.myOnClickListen());
        cardView2.setOnClickListener(new MainActivity.myOnClickListen());
        cardView3.setOnClickListener(new MainActivity.myOnClickListen());
        cardView4.setOnClickListener(new MainActivity.myOnClickListen());
        cardView5.setOnClickListener(new MainActivity.myOnClickListen());
        cardView6.setOnClickListener(new MainActivity.myOnClickListen());
        cardView7.setOnClickListener(new MainActivity.myOnClickListen());
        cardView8.setOnClickListener(new MainActivity.myOnClickListen());
        scaner.setOnClickListener(new MainActivity.myOnClickListen());


//        spinner监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    searchEngines = "https://m.baidu.com/s?word=";
                    System.out.println("选择的是百度");
                } else if (i == 1) {
                    searchEngines = "https://www.google.fr/search?ei=CDiYWeLYGoKTa5r0qRg&q=";
                    System.out.println("选择的是谷歌");
                } else if (i == 2) {
                    searchEngines = "https://cn.bing.com/search?q=";
                    System.out.println("必应");
                } else if (i == 3) {
                    searchEngines = "https://wap.sogou.com/web/searchList.jsp?keyword=";
                    System.out.println("搜狗");
                } else if (i == 4) {
                    searchEngines = "http://m.sm.cn/s?q=";
                    System.out.println("神马");
                } else {
                    System.out.println("未选择");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                spinner.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                ImageView clear = (ImageView) findViewById(R.id.clear);
                clear.setVisibility(View.VISIBLE);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText.getText().clear();
                    }
                });
            }
        });


//        Edittext用户输入监听按钮
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

//                点击软键盘回车键
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    searchKey = editText.getText().toString();
                    if (!(searchKey.isEmpty())) {
                        /**
                         * TODO: 判断输入的是否为网址
                         */
//                    包含 http 头
                        if (searchKey.indexOf("http") != -1) {
                            searchKey = searchKey;
                        } else if (searchKey.indexOf("www.") != -1) {
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".com") != -1) {
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".cn") != -1) {
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".org") != -1) {
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".info") != -1) {
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".net") != -1) {
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".io") != -1) {
                            searchKey = "http://" + searchKey;
                        } else {
                            searchKey = searchEngines + searchKey;
                        }

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                        intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

//        当用户点击搜索按钮
        btn_submit = (ImageView) findViewById(com.adymilk.easybrowser.por.R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKey = editText.getText().toString();

                if (!(searchKey.isEmpty())) {
                    /**
                     * TODO: 判断输入的是否为网址
                     */
//                    包含 http 头
                    if (searchKey.indexOf("http") != -1) {
                        searchKey = searchKey;
                    } else {
                        searchKey = searchEngines + searchKey;
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                    intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);


                } else {
                    Toast.makeText(MainActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void findViews() {
        cardview_content = (LinearLayout) findViewById(R.id.cardview_content);
        editText = (EditText) findViewById(com.adymilk.easybrowser.por.R.id.edit_url);
        cardView1 = (CardView) findViewById(R.id.cardview1);
        cardView2 = (CardView) findViewById(R.id.cardview2);
        cardView3 = (CardView) findViewById(R.id.cardview3);
        cardView4 = (CardView) findViewById(R.id.cardview4);
        cardView5 = (CardView) findViewById(R.id.cardview5);
        cardView6 = (CardView) findViewById(R.id.cardview6);
        cardView7 = (CardView) findViewById(R.id.cardview7);
        cardView8 = (CardView) findViewById(R.id.cardview8);
        spinner = (Spinner) findViewById(R.id.spinner);
        scaner = (ImageView) findViewById(R.id.iv_scaner);
    }


    public void openBrowser() {
        /**
         * TODO 选择昕浏览器打开外部地址
         */
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                searchKey = uri.toString();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, com.adymilk.easybrowser.por.Browser.class);//从一个activity跳转到另一个activity
                intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                startActivity(intent);
            }
        }
    }


    class myOnClickListen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            intent = new Intent();
            String 直播 = "https://activity.uc.cn/uclive2017/index?entry=zbyp&uc_biz_str=S%3Acustom%7CC%3Aiflow_ncmt%7CK%3Atrue%7CN%3Atrue";
            String 漫画 = "https://cartoon.uc.cn/?uc_biz_str=S:custom&t=1502715479582&modal=hide&download_guide=1";
            String 腾讯游戏 = "http://h5.qq.com";
            String 段子 = "http://m.budejie.com/";
            String 生活 = "http://go.uc.cn/page/life/life?uc_param_str=dnfrpfbivecpbtntlaad&source=webapp#!/meituan";
            String 游戏 = "http://59600.com";
            String 新闻 = "http://3g.163.com/";
            String 小说 = "http://t.shuqi.com/route.php?pagename=";
            switch (v.getId()) {
                case R.id.cardview1:
                    intent.setClass(MainActivity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 直播);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview2:
                    intent.setClass(MainActivity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 漫画);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview3:
                    intent.setClass(MainActivity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 腾讯游戏);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview4:
                    intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                    intent.putExtra("str", 段子);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview5:
                    intent.setClass(MainActivity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 生活);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview6:
                    intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                    intent.putExtra("str", 游戏);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview7:
                    intent.setClass(MainActivity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 新闻);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview8:
                    intent.setClass(MainActivity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 小说);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.iv_scaner:
                    //实例化

                    wxApi = WXAPIFactory.createWXAPI(MainActivity.this, WX_APP_ID);
                    wxApi.registerApp(WX_APP_ID);

                    if (!wxApi.isWXAppInstalled()) {
                        toastShowShort(MainActivity.this, "未安装微信客户端！");
                    } else {
                        toWeChatScanDirect(MainActivity.this);
                    }
                default:
                    break;

            }
        }
    }

    // 跳转微信扫一扫
    public static void toWeChatScanDirect(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(335544320);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }


    //    清爽模式
    public void setSimpleMode() {
        final String key_simple_mode = getString(R.string.simple_mode);
        final Boolean isSimpleMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key_simple_mode, true);
        System.out.println("当前清爽模式为:" + isSimpleMode);
        if (isSimpleMode) {
            cardview_content.setVisibility(View.GONE);
        } else if (!isSimpleMode) {
            cardview_content.setVisibility(View.VISIBLE);

        }


    }


    public void initStatusBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.white)
                .init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("当前Activity状态为onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("当前Activity状态为onRestart");
    }

    @Override
    protected void onResume() {


        super.onResume();
        System.out.println("当前Activity状态为onResume");
    }

    @Override
    protected void onStart() {
        setSimpleMode();
        super.onStart();
        System.out.println("当前Activity状态为onStart");
    }

    @Override
    protected void onDestroy() {
        System.out.println("当前Activity状态为onDestroy");
        super.onDestroy();
        finish();
        System.exit(0);//退出
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

}
