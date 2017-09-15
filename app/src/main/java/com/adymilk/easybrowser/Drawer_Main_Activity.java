package com.adymilk.easybrowser;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.gyf.barlibrary.ImmersionBar;
import com.heima.easysp.SharedPreferencesUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

import static android.R.attr.onClick;


public class Drawer_Main_Activity extends ActionBarActivity {
    private String searchEngines;
    private Intent intent;
    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private LinearLayout linerlay_main_content;
    private List<Map<String, Object>> data;
    private String appVersion;
    private String appPackName;
    private TextView app_name;
    private ImageView btn_submit;
    private String searchKey;
    private boolean isLightTheme;
    private boolean isSimpleMode;
    private LinearLayout cardview_content;
    private LinearLayout lnlayout_drawerView_left;
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

        // 多彩主题
        System.out.println("当前Activity状态为onCreate");
        // 用昕浏览器打开链接
       openBrowser();
        // 沉浸状态栏
        initStatusBar();

        setContentView(R.layout.drawer_maincontent);
        findViews(); //获取控件
        getPackInfo();//获取app版本信息


        cardView1.setOnClickListener(new myOnClickListen());
        cardView2.setOnClickListener(new myOnClickListen());
        cardView3.setOnClickListener(new myOnClickListen());
        cardView4.setOnClickListener(new myOnClickListen());
        cardView5.setOnClickListener(new myOnClickListen());
        cardView6.setOnClickListener(new myOnClickListen());
        cardView7.setOnClickListener(new myOnClickListen());
        cardView8.setOnClickListener(new myOnClickListen());
        scaner.setOnClickListener(new myOnClickListen());



//        spinner监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    searchEngines = "https://m.baidu.com/s?word=";
                    System.out.println("选择的是百度");
                }else if (i==1){
                    searchEngines = "https://www.google.fr/search?ei=CDiYWeLYGoKTa5r0qRg&q=";
                    System.out.println("选择的是谷歌");
                }else if (i==2){
                    searchEngines = "https://cn.bing.com/search?q=";
                    System.out.println("必应");
                }else if (i==3){
                    searchEngines = "https://wap.sogou.com/web/searchList.jsp?keyword=";
                    System.out.println("搜狗");
                }else if (i==4){
                    searchEngines = "http://m.sm.cn/s?q=";
                    System.out.println("神马");
                }
                else {
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
                if (keyCode == KeyEvent.KEYCODE_ENTER &&event.getAction() == KeyEvent.ACTION_DOWN){
                    searchKey = editText.getText().toString();
                    if (!(searchKey.isEmpty())){
                        /**
                         * TODO: 判断输入的是否为网址
                         */
//                    包含 http 头
                        if (searchKey.indexOf("http") != -1){
                            searchKey = searchKey;
                        }else if(searchKey.indexOf("www.") != -1){
                            searchKey = "http://" + searchKey;
                        }else if (searchKey.lastIndexOf(".com") != -1){
                            searchKey = "http://" + searchKey;
                        }else if (searchKey.lastIndexOf(".cn") != -1){
                            searchKey = "http://" + searchKey;
                        }else if (searchKey.lastIndexOf(".org") != -1){
                            searchKey = "http://" + searchKey;
                        }else if (searchKey.lastIndexOf(".info") != -1){
                            searchKey = "http://" + searchKey;
                        }else if (searchKey.lastIndexOf(".net") != -1){
                            searchKey = "http://" + searchKey;
                        } else if (searchKey.lastIndexOf(".io") != -1){
                            searchKey = "http://" + searchKey;
                        }
                        else {
                            searchKey = searchEngines + searchKey;
                        }

                        Intent intent=new Intent();
                        intent.setClass(Drawer_Main_Activity.this, Browser.class);//从一个activity跳转到另一个activity
                        intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                        startActivity(intent);

                    }else{
                        Toast.makeText(Drawer_Main_Activity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
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

                if (!(searchKey.isEmpty())){
                    /**
                     * TODO: 判断输入的是否为网址
                     */
//                    包含 http 头
                    if (searchKey.indexOf("http") != -1){
                        searchKey = searchKey;
                    }else {
                        searchKey = searchEngines + searchKey;
                    }
                    Intent intent=new Intent();
                    intent.setClass(Drawer_Main_Activity.this, Browser.class);//从一个activity跳转到另一个activity
                    intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);


                }else{
                    Toast.makeText(Drawer_Main_Activity.this,"输入不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });


        toolbar.setTitle("");//设置Toolbar标题
//        toolbar.setNavigationIcon(R.mipmap.menu);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置左侧菜单
        data = getData();
        MyAdapter adapter = new MyAdapter(this);
        lvLeftMenu.setAdapter(adapter);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("点击了"+i);
                switch (i){
                    case 0:
                        if (isQQClientAvailable(Drawer_Main_Activity.this)){
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=924114103";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        }else {
                            Toast.makeText(Drawer_Main_Activity.this,"未安装QQ",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 1:
                        showDialog("酷安有你们更精彩","@城岚，@先生we，@吃猫的咸鱼，@yuensui，@有关部门负责人，@谷歌惊奇，@罗强，@都顾,@爱幻想的大白 \n\n感谢以上网友提出的宝贵建议，如果你也想要你的 ID 出现在这里，赶快点击上面参与设计与我交流吧。\n生活中每个人都是设计师！");
                        break;

                    case 2:
                        if (AlipayZeroSdk.hasInstalledAlipayClient(Drawer_Main_Activity.this)) {
                            AlipayZeroSdk.startAlipayClient(Drawer_Main_Activity.this, "FKX02828RROAVHC0VOT05F");
                        } else {
                            Toast.makeText(Drawer_Main_Activity.this, "未安装支付宝", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 3:
                        joinQQGroup("JXqo5mGe-tD5-KJEhvumvUuMJnweqmA4");
                        break;

                    case 4:
                        setThemeOnClick();
                        break;

                    case 5:
                        setSimpleMode();
                        break;
                    case 6:
                        intent = new Intent();
                        intent.setClass(Drawer_Main_Activity.this, BookmarkActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent=new Intent();
                        intent.setClass(Drawer_Main_Activity.this, Browser.class);//从一个activity跳转到另一个activity
                        intent.putExtra("str", "https://www.coolapk.com/apk/157097");//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                        startActivity(intent);
                        break;
                }
                mDrawerLayout.closeDrawers();
            }
        });
        //设置主题

        isLightTheme = SharedPreferencesUtils.init(Drawer_Main_Activity.this).getBoolean("isLightTheme");
        isSimpleMode = SharedPreferencesUtils.init(Drawer_Main_Activity.this).getBoolean("isSimpleMode");
        onStartSetTheme();
        onStartSetSimpleMode();
        System.out.println("当前isLightTheme主题" + isLightTheme);
        System.out.println("当前isSimpleMode主题" + isSimpleMode);




    }



    private void findViews() {
        app_name = (TextView) findViewById(R.id.app_name);
        cardview_content = (LinearLayout) findViewById(R.id.cardview_content);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        lnlayout_drawerView_left = (LinearLayout) findViewById(R.id.lnlayout_drawerView_left);
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
        linerlay_main_content  = (LinearLayout) findViewById(R.id.linerlay_main_content);
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

//    自定义Adapter

    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.design);
            map.put("title", "反馈建议");
            map.put("info", "用户才是产品的好设计师");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.advices);
            map.put("title", "最佳建议奖");
            map.put("info", "提名致谢酷安网友提出的建议");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.donate);
            map.put("title", "捐赠开发者");
            map.put("info", "全职开发者需要你的支持");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.qq_group);
            map.put("title", "加入官方群");
            map.put("info", "获取新版本第一时间内测机会");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.themes);
            map.put("title", "主题切换");
            map.put("info", "开启白天/夜间模式");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.simple);
            map.put("title", "清爽模式");
            map.put("info", "隐藏主页CardView布局");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.bookmark);
            map.put("title", "我的书签");
            map.put("info", "你的书签都在这儿");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.about);
            map.put("title", "关于软件");
            map.put("info", "当前版本："+ appVersion);
            list.add(map);




        return list;
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;
    }

    public class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater = null;
        private MyAdapter(Context context)
        {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }
        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }


        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.drawer_listview_item, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.tv);
                holder.info = (TextView)convertView.findViewById(R.id.info);
//                将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.img.setImageResource((Integer) data.get(position).get("img"));
            holder.title.setText((String)data.get(position).get("title"));
            holder.info.setText((String)data.get(position).get("info"));

            return convertView;
        }

    }

//    对话框方法
    public void showDialog(String title,String msg){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Drawer_Main_Activity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(R.string.disagree, null);
        builder.setPositiveButton(R.string.agree, null);
        builder.show();
    }

//    获取版本号

    public void getPackInfo(){
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            appVersion = packageInfo.versionName;
            appPackName = packageInfo.packageName;
            System.out.println("当前app版本号为："+ appVersion);
            System.out.println("当前app版appPackName为："+ appPackName);
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

//    设置主题

    public void setThemeOnClick(){
        if (isLightTheme){
            lvLeftMenu.setBackgroundColor(getResources().getColor(R.color.white));
            linerlay_main_content.setBackgroundResource(R.color.white);
            //沉浸状态栏
            ImmersionBar.with(Drawer_Main_Activity.this)
                    .statusBarDarkFont(true)
                    .statusBarColor(R.color.white)
                    .init();
            toolbar.setBackgroundResource(R.color.white);

            app_name.setTextColor(getResources().getColor(R.color.gray3));
            toolbar.setNavigationIcon(R.mipmap.menu);
            SharedPreferencesUtils.init(Drawer_Main_Activity.this).putBoolean("isLightTheme", false);
            isLightTheme = false;
            System.out.println("主题配置写入"+ isLightTheme);



        }else if (!isLightTheme){
            lvLeftMenu.setBackgroundColor(getResources().getColor(R.color.black));
            linerlay_main_content.setBackgroundResource(R.color.black);
            //沉浸状态栏
            ImmersionBar.with(Drawer_Main_Activity.this)
                    .statusBarDarkFont(false)
                    .statusBarColor(R.color.black)
                    .init();
            toolbar.setBackgroundResource(R.color.black);
            toolbar.setNavigationIcon(R.mipmap.menu);
            app_name.setTextColor(getResources().getColor(R.color.gray3));
            SharedPreferencesUtils.init(Drawer_Main_Activity.this).putBoolean("isLightTheme", true);
            isLightTheme = true;
            System.out.println("主题配置写入"+ isLightTheme);
        }


    }

//    清爽模式
    public void setSimpleMode(){
        if (isSimpleMode){
            System.out.println("模式配置为"+ isSimpleMode);
            cardview_content.setVisibility(View.VISIBLE);
            SharedPreferencesUtils.init(Drawer_Main_Activity.this).putBoolean("isSimpleMode", false);
            isSimpleMode = false;

        }else if (!isSimpleMode){
            System.out.println("模式配置为"+ isSimpleMode);
            cardview_content.setVisibility(View.GONE);
            SharedPreferencesUtils.init(Drawer_Main_Activity.this).putBoolean("isSimpleMode", true);
            isSimpleMode = true;

        }



    }

// 设置主题方法
    public void onStartSetTheme(){

        if (!isLightTheme){
            lvLeftMenu.setBackgroundColor(getResources().getColor(R.color.white));
            linerlay_main_content.setBackgroundResource(R.color.white);
            //沉浸状态栏
            ImmersionBar.with(Drawer_Main_Activity.this)
                    .statusBarDarkFont(true)
                    .statusBarColor(R.color.white)
                    .init();
            toolbar.setBackgroundResource(R.color.white);
            toolbar.setNavigationIcon(R.mipmap.menu);
            app_name.setTextColor(getResources().getColor(R.color.gray3));

            System.out.println("主题配置写入"+ isLightTheme);
        }else if (isLightTheme){
            lvLeftMenu.setBackgroundColor(getResources().getColor(R.color.black));
            linerlay_main_content.setBackgroundResource(R.color.black);
            //沉浸状态栏
            ImmersionBar.with(Drawer_Main_Activity.this)
                    .statusBarDarkFont(false)
                    .statusBarColor(R.color.black)
                    .init();
            toolbar.setBackgroundResource(R.color.black);
            toolbar.setNavigationIcon(R.mipmap.menu);
            app_name.setTextColor(getResources().getColor(R.color.gray3));

            System.out.println("主题配置写入"+ isLightTheme);
        }

    }

//    设置清爽模式

    public void onStartSetSimpleMode(){
        if (!isSimpleMode){
            System.out.println("onStartSetSimpleMode=" + isSimpleMode);
            cardview_content.setVisibility(View.VISIBLE);
        }else if (isSimpleMode){
            System.out.println("onStartSetSimpleMode=" + isSimpleMode);
            cardview_content.setVisibility(View.GONE);
        }

    }


    public void openBrowser(){
        /**
         * TODO 选择昕浏览器打开外部地址
         */
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)){
            Uri uri = i_getvalue.getData();
            if (uri != null ){
                searchKey = uri.toString();
                Intent intent=new Intent();
                intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.por.Browser.class);//从一个activity跳转到另一个activity
                intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                startActivity(intent);
            }
        }
    }


    class myOnClickListen implements View.OnClickListener {

        @Override
        public void onClick (View v){
            intent= new Intent();
            String 直播 = "https://activity.uc.cn/uclive2017/index?entry=zbyp&uc_biz_str=S%3Acustom%7CC%3Aiflow_ncmt%7CK%3Atrue%7CN%3Atrue";
            String 漫画 = "https://cartoon.uc.cn/?uc_biz_str=S:custom&t=1502715479582&modal=hide&download_guide=1";
            String 腾讯游戏 = "http://h5.qq.com";
            String 段子 = "http://m.budejie.com/";
            String 生活 = "http://go.uc.cn/page/life/life?uc_param_str=dnfrpfbivecpbtntlaad&source=webapp#!/meituan";
            String 游戏 = "http://59600.com";
            String 新闻 = "http://3g.163.com/";
            String 小说 = "http://t.shuqi.com/route.php?pagename=";
            switch (v.getId()){
                case R.id.cardview1:
                    intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 直播);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview2:
                    intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 漫画);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview3:
                    intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 腾讯游戏);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview4:
                    intent.setClass(Drawer_Main_Activity.this, Browser.class);//从一个activity跳转到另一个activity
                    intent.putExtra("str", 段子);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview5:
                    intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 生活);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview6:
                    intent.setClass(Drawer_Main_Activity.this, Browser.class);//从一个activity跳转到另一个activity
                    intent.putExtra("str", 游戏);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview7:
                    intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 新闻);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.cardview8:
                    intent.setClass(Drawer_Main_Activity.this, com.adymilk.easybrowser.CardView1Activity.class);//从一个activity跳转到另一个activity
                    intent.putExtra("targetUrl", 小说);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                    startActivity(intent);
                    break;
                case R.id.iv_scaner:
                    //实例化

                    wxApi = WXAPIFactory.createWXAPI(Drawer_Main_Activity.this,WX_APP_ID);
                    wxApi.registerApp(WX_APP_ID);

                    if(!wxApi.isWXAppInstalled()){
                        toastShowShort(Drawer_Main_Activity.this, "未安装微信客户端！");
                    }else {
                        toWeChatScanDirect(Drawer_Main_Activity.this);
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


    public void initStatusBar(){
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
        super.onStart();
        System.out.println("当前Activity状态为onStart"+ isLightTheme);
    }

    @Override
    protected void onDestroy() {
        System.out.println("当前Activity状态为onDestroy");
        super.onDestroy();
        finish();
        System.exit(0);//退出
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