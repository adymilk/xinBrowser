package com.adymilk.easybrowser;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.heima.easysp.SharedPreferencesUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;


public class MainActivity extends Activity implements android.view.GestureDetector.OnGestureListener {

    //调用系统相册-选择图片
    private static final int IMAGE = 2;
    private static final int REQUEST_QR_CODE = 1;
    private Intent intent;
    //声明相关变量
    private String searchKey;
    //定义手势检测器实例
    GestureDetector detector;

    // 状态栏
    private ImmersionBar mImmersionBar;
    private LinearLayout cardview_content;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private CardView cardView5;
    private CardView cardView6;
    private CardView cardView7;
    private CardView cardView8;
    private TextView editText;
    private Spinner spinner;
    private ImageView scaner;
    private IWXAPI wxApi;
    private String WX_APP_ID = "wxee53eb68352c793e";

    private String key_Customize_Home_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("当前Activity状态为onCreate");
        // 用昕浏览器打开链接
        openBrowser();
        // 沉浸状态栏
        initStatusBar();

        setContentView(R.layout.activity_main);


        //创建手势检测器
        detector = new GestureDetector(this,this);

        findViews(); //获取控件


        TextView tv_app_name = findViewById(R.id.app_name);
        tv_app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key_Customize_Home_bg = getString(R.string.Customize_Home_bg);
                final Boolean Customize_Home_bg = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(key_Customize_Home_bg, true);
                if (Customize_Home_bg){
                    //调用相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);
                }

            }
        });
        ImageView imv_setting = findViewById(R.id.imv_setting);
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



        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    private void findViews() {
        cardview_content =  findViewById(R.id.cardview_content);
        editText  =   findViewById(R.id.tv_search);
        cardView1 =  findViewById(R.id.cardview1);
        cardView2 =  findViewById(R.id.cardview2);
        cardView3 =  findViewById(R.id.cardview3);
        cardView4 =  findViewById(R.id.cardview4);
        cardView5 =  findViewById(R.id.cardview5);
        cardView6 =  findViewById(R.id.cardview6);
        cardView7 =  findViewById(R.id.cardview7);
        cardView8 =  findViewById(R.id.cardview8);
        spinner   =  findViewById(R.id.spinner);
        scaner    =  findViewById(R.id.iv_scaner);
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

    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me){
        return detector.onTouchEvent(me);
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * 滑屏监测
     *
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
//            Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
//            Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            intent = new Intent();
            intent.setClass(MainActivity.this, SetttingActivity.class);
            startActivity(intent);
//            Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            intent = new Intent();
            intent.setClass(MainActivity.this, SearchActivity.class);
            startActivity(intent);
//            Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
        }

        return false;
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

//                    wxApi = WXAPIFactory.createWXAPI(MainActivity.this, WX_APP_ID);
//                    wxApi.registerApp(WX_APP_ID);
//
//                    if (!wxApi.isWXAppInstalled()) {
//                        toastShowShort(MainActivity.this, "未安装微信客户端！");
//                    } else {
//                        toWeChatScanDirect(MainActivity.this);
//                    }
                    Intent i = new Intent(MainActivity.this, io.github.xudaojie.qrcodelib.CaptureActivity.class);
                    startActivityForResult(i, REQUEST_QR_CODE);
                    break;
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


    // 沉浸状态栏
    public void initStatusBar() {
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }



    @Override
    protected void onStart() {
        super.onStart();
        setSimpleMode();
        key_Customize_Home_bg = getString(R.string.Customize_Home_bg);
        final Boolean Customize_Home_bg = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key_Customize_Home_bg, true);
        String imaePath = SharedPreferencesUtils.init(this).getString("imaePath");
        if (Customize_Home_bg){
            if (!(imaePath.isEmpty())){
                ((LinearLayout)findViewById(R.id.linearLayout_main)).setBackground(Drawable.createFromPath(imaePath));
            }
        }else {
            if (!(imaePath.isEmpty())){
                LinearLayout linearLayout = findViewById(R.id.linearLayout_main);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
        System.out.println("Customize_Home_bg"+Customize_Home_bg);

        initStatusBar();
        System.out.println("当前Activity状态为onStart");
    }

    @Override
    protected void onDestroy() {
        System.out.println("当前Activity状态为onDestroy");
        super.onDestroy();
        finish();
        System.exit(0);//退出
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取二维码扫码结果
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
            intent.putExtra("str", result);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
            startActivity(intent);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
        //获取图片路径
        else if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }

    }


    //加载图片
    private void showImage(String imaePath){
//        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        SharedPreferencesUtils.init(this).putString("imaePath",imaePath);
        System.out.println("图片路径"+ imaePath);
//        toastShowShort(this,"图片路径为："+imaePath);
        ((LinearLayout)findViewById(R.id.linearLayout_main)).setBackground(Drawable.createFromPath(imaePath));
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
