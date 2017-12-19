package com.adymilk.easybrowser.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adymilk.easybrowser.por.Browser;
import com.adymilk.easybrowser.por.R;
import com.heima.easysp.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.net.URL;

import static com.adymilk.easybrowser.Utils.comm.hideBar;


public class MainActivity extends Activity implements android.view.GestureDetector.OnGestureListener {

    //调用系统相册-选择图片
    private static final int IMAGE = 2;
    private static final int REQUEST_QR_CODE = 1;
    private Intent intent;
    //声明相关变量
    private String searchKey;
    //定义手势检测器实例
    GestureDetector detector;
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
    private String key_Customize_Home_bg = "开启主页自定义背景";
    private Boolean Customize_Home_bg = true;
    private String imagePath;
    private ImageView imv_setting;
    private TextView tv_app_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 用昕浏览器打开链接
        openBrowser();
        // 沉浸状态栏
       hideBar(this);

        setContentView(R.layout.activity_main);


        //加载搜索界面随着节日活动改变的图片
        new load_image().execute("http://oe3vwrk94.bkt.clouddn.com/xingBrowser_MainActivity_logo.png");

                //创建手势检测器
        detector = new GestureDetector(this,this);

        //获取控件
        findViews();
        tv_app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startToSysImagesManager();

            }

            private void startToSysImagesManager() {
                Customize_Home_bg = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(key_Customize_Home_bg, true);
                if (Customize_Home_bg){
                    //调用相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);
                }
            }
        });


        imv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityTo(MainActivity.this, SetttingActivity.class);
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
                startActivityTo(MainActivity.this, SearchActivity.class);
            }
        });

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);


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
        imv_setting = findViewById(R.id.imv_setting);
        tv_app_name = findViewById(R.id.app_name);
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
                intent.putExtra("targetUrl", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
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

    //CardView 点击事件
    class myOnClickListen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            intent = new Intent();
            String 直播 = "https://activity.uc.cn/uclive2017/index?entry=zbyp&uc_biz_str=S%3Acustom%7CC%3Aiflow_ncmt%7CK%3Atrue%7CN%3Atrue";
            String 漫画 = "https://cartoon.uc.cn/?uc_biz_str=S:custom&t=1502715479582&modal=hide&download_guide=1";
            String 腾讯游戏 = "http://h5.qq.com";
            String 段子 = "http://m.budejie.com/";
            String 生活 = "http://go.uc.cn/page/life/life?uc_param_str=dnfrpfbivecpbtntlaad&source=webapp#!/meituan";
            String B站 = "https://m.bilibili.com";
//            String 新闻 = "http://3g.163.com/";
            String 小说 = "http://t.shuqi.com/route.php?pagename=";
            switch (v.getId()) {
                case R.id.cardview1:
                    startActivityToCardViewActivity(intent, CardViewActivity.class, 直播);
                    break;
                case R.id.cardview2:
                    startActivityToCardViewActivity(intent, CardViewActivity.class, 漫画);
                    break;
                case R.id.cardview3:
                    startActivityToCardViewActivity(intent, CardViewActivity.class, 腾讯游戏);
                    break;
                case R.id.cardview4:
                    startActivityToCardViewActivity(intent, Browser.class, 段子);
                    break;
                case R.id.cardview5:
                    startActivityToCardViewActivity(intent, CardViewActivity.class, 生活);
                    break;
                case R.id.cardview6:
                    startActivityToCardViewActivity(intent, CardViewActivity.class, 小说);
                    break;
                case R.id.cardview7:
                    startActivityToCardViewActivity(intent, Browser.class, B站);
                    break;
                case R.id.cardview8:
                    startActivityToCardViewActivity(intent, BookmarkActivity.class, "书签");
                    break;
                case R.id.iv_scaner:
                    Intent i = new Intent(MainActivity.this, io.github.xudaojie.qrcodelib.CaptureActivity.class);
                    startActivityForResult(i, REQUEST_QR_CODE);
                    break;
                default:
                    break;

            }
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

    //CardView 点击跳转
    public void startActivityToCardViewActivity(Intent intent, Class c, String s) {
        intent.setClass(MainActivity.this, c);//从一个activity跳转到另一个activity
        intent.putExtra("targetUrl", s);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSimpleMode();
        //设置主页背景图片
        imagePath = SharedPreferencesUtils.init(this).getString("imagePath");
        showImage(imagePath);
        //隐藏状态栏
        hideBar(this);
        System.out.println("当前Activity状态为onStart");
    }



    @Override
    protected void onDestroy() {
        System.out.println("当前Activity状态为onDestroy");
        super.onDestroy();
        finish();
        MobclickAgent.onKillProcess(this);
        System.exit(0);//退出
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取二维码扫码结果
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            final String result = data.getStringExtra("result");
            boolean isNum = result.matches("[0-9]+");
            if (isNum == false) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                intent.putExtra("targetUrl", result);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                startActivity(intent);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            } else {
                //如果是数字（条形码）
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BarcodeActivity.class);//从一个activity跳转到另一个activity
                intent.putExtra("barcode", result);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                startActivity(intent);
            }

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
    private void showImage(String imagePath) {
        SharedPreferencesUtils.init(this).putString("imagePath", imagePath);
        Customize_Home_bg = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(key_Customize_Home_bg, true);

        if (!Customize_Home_bg) {
            imagePath = null;
        }
        findViewById(R.id.linearLayout_main).setBackground(Drawable.createFromPath(imagePath));
        System.out.println("Customize_Home_bg" + Customize_Home_bg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //点击跳转Activity
    private void startActivityTo(Context context, Class c) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        startActivity(intent);
    }


    //加载网络图片

    Drawable LoadImageFromWebOperations(String url) {
        InputStream is = null;
        Drawable d = null;
        try {
            is = (InputStream) new URL(url).getContent();
            d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    class load_image extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            Drawable drawable = LoadImageFromWebOperations(params[0]);
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            ImageView logo = findViewById(R.id.logo);
            logo.setImageDrawable(result);
        }

    }


}
