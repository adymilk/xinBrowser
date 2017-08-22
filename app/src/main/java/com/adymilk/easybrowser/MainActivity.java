package com.adymilk.easybrowser;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import moe.feng.alipay.zerosdk.AlipayZeroSdk;

public class MainActivity extends AppCompatActivity {
    private PopupMenu mPopupMenu;
    private ImageView mMoreImageView;
    private String searchEngines;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(com.adymilk.easybrowser.R.id.edit_url);
        CardView cardView1 = (CardView) findViewById(R.id.cardview1);
        CardView cardView2 = (CardView) findViewById(R.id.cardview2);
        CardView cardView3 = (CardView) findViewById(R.id.cardview3);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        intent=new Intent();


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(MainActivity.this, Browser.class);
                intent.putExtra("str", "https://github.com/trending/java");
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(MainActivity.this, Browser.class);
                intent.putExtra("str", "https://500px.me/community/discover");
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(MainActivity.this, GamesActivity.class);
                startActivity(intent);
            }
        });

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
//        右上角菜单按钮更多
        mMoreImageView = (ImageView) findViewById(com.adymilk.easybrowser.R.id.iv_home_more);
        mMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu = new PopupMenu(MainActivity.this,mMoreImageView);
                mPopupMenu.getMenuInflater().inflate(com.adymilk.easybrowser.R.menu.home_popup_menu,mPopupMenu.getMenu());
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case com.adymilk.easybrowser.R.id.open_qq:
                                String url = "mqqwpa://im/chat?chat_type=wpa&uin=924114103";
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                return true;
                            case com.adymilk.easybrowser.R.id.donate:
                                if (AlipayZeroSdk.hasInstalledAlipayClient(MainActivity.this)) {
                                    AlipayZeroSdk.startAlipayClient(MainActivity.this, "FKX02828RROAVHC0VOT05F");
                                } else {
                                    Toast.makeText(MainActivity.this, "未安装支付宝", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case com.adymilk.easybrowser.R.id.join_qq:
                                joinQQGroup("JXqo5mGe-tD5-KJEhvumvUuMJnweqmA4");
                                return true;
                            case R.id.about:
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.title);
                                builder.setMessage(R.string.aboutContent);
                                builder.setNegativeButton(R.string.disagree, null);
                                builder.setPositiveButton(R.string.agree, null);
                                builder.show();
                        }

                        return false;
                    }
                });

                mPopupMenu.show();
            }
        });



//        用户输入监听按钮
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&event.getAction() == KeyEvent.ACTION_DOWN){
                    String url = editText.getText().toString();
                    if (!(url.isEmpty())){
                        url = searchEngines + url;
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                        intent.putExtra("str", url);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                        startActivity(intent);

                    }else{
                        Toast.makeText(MainActivity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

//        当用户点击搜索按钮
        ImageView btn_submit = (ImageView) findViewById(com.adymilk.easybrowser.R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString();

                if (!(url.isEmpty())){
//                    判断输入的是否为网址
                    if ((url.substring(0,4)).equals("http")){
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                        intent.putExtra("str", url);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                        startActivity(intent);
                    }else {
                        url = searchEngines + url;
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this, Browser.class);//从一个activity跳转到另一个activity
                        intent.putExtra("str", url);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(MainActivity.this,"输入不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

}
