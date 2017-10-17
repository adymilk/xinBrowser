package com.adymilk.easybrowser;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.adymilk.easybrowser.por.Browser;
import com.adymilk.easybrowser.por.R;
import com.heima.easysp.SharedPreferencesUtils;

public class SearchActivity extends AppCompatActivity {
    private String searchEngines;
    //声明相关变量
    private TextView btn_submit;
    private String searchKey;
    private EditText editText;
    private Spinner spinner;
    private ImageView clear;
    private String s;
    private String key_Customize_Home_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        key_Customize_Home_bg = getString(R.string.Customize_Home_bg);
        final Boolean Customize_Home_bg = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key_Customize_Home_bg, true);
        String imaePath;
        imaePath = SharedPreferencesUtils.init(this).getString("imaePath");
        if (Customize_Home_bg){
            if (!(imaePath.isEmpty())){
                imaePath = SharedPreferencesUtils.init(this).getString("imaePath");

            }
        }else {
            imaePath = null;
        }
        ((LinearLayout)findViewById(R.id.search_bg)).setBackground(Drawable.createFromPath(imaePath));




        editText = (EditText) findViewById(R.id.edit_url);
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        clear = (ImageView) findViewById(R.id.clear);
        clear.setVisibility(View.INVISIBLE);
        s = editText.getText().toString();



        btn_submit.setText("取消");
        if (btn_submit.getText().equals("取消")){
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideInput(view);
                    finish();
                }
            });
        }
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

//        spinner监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: //百度
                        searchEngines = "https://m.baidu.com/s?word=";
                        break;
                    case 1: //谷歌
                        searchEngines = "https://www.google.fr/search?ei=CDiYWeLYGoKTa5r0qRg&q=";
                        break;
                    case 2: //必应
                        searchEngines = "https://cn.bing.com/search?q=";
                        break;
                    case 3: //搜狗
                        searchEngines = "https://wap.sogou.com/web/searchList.jsp?keyword=";
                        break;
                    case 4: //神马
                        searchEngines = "http://m.sm.cn/s?q=";
                        break;
                    case 5: //磁力链接
                        searchEngines = "http://www.sexba.net/search-kw-";
                        break;
                    case 6: //酷安
                        searchEngines = "https://www.coolapk.com/search?q=";
                        break;
                    case 7: //知乎
                        searchEngines = "https://www.zhihu.com/search?type=content&q=";
                        break;
                    case 8: //豆瓣
                        searchEngines = "https://m.douban.com/search/?query=";
                        break;
                    case 9: //github
                        searchEngines = "https://github.com/search?q=";
                        break;
                    case 10: //bilibili
                        searchEngines = "https://m.bilibili.com/search.html?keyword=";
                        break;

                    default:
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                System.out.println("字符串长度为："+s.length());
//                System.out.println("状态为：beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                System.out.println("字符串长度为："+charSequence.length());
//                System.out.println("状态为：onTextChanged");
                if (charSequence.length()!= 0){
//                    System.out.println("字符不为空");
                    btn_submit.setText("搜索");
                    clear.setVisibility(View.VISIBLE);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchFunction();
                        }
                    });

                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editText.getText().clear();
                        }
                    });

                }else {
//                    System.out.println("字符为空");
                    clear.setVisibility(View.INVISIBLE);
                    btn_submit.setText("取消");
                    clear.setVisibility(View.INVISIBLE);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideInput(view);
                            finish();
                        }
                    });

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                System.out.println("字符串长度为："+s.length());
//                System.out.println("状态为：afterTextChanged");
            }
        });


        //聚焦
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (s.isEmpty()){
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideInput(view);
                            finish();
                        }
                    });
                }
                spinner.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

            }
        });



//        Edittext用户输入监听按钮
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

//                点击软键盘回车键
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {


                    searchFunction();
                }
                return false;
            }
        });

    }

    private void searchFunction(){
        /**
         * TODO: 判断输入的是否为网址
         */
        searchKey = editText.getText().toString();

        if (searchKey.startsWith("http")) {
            searchKey = searchKey;

        } else if (searchKey.startsWith("www.")
                || searchKey.endsWith(".com")
                || searchKey.endsWith(".cn")
                || searchKey.endsWith(".org")
                || searchKey.endsWith(".info")
                || searchKey.endsWith(".net")
                || searchKey.endsWith("io")
                || searchKey.endsWith(".cc")) {
            searchKey = "http://" + searchKey;

        } else {
            searchKey = searchEngines + searchKey;
        }

        Intent intent = new Intent();
        intent.setClass(SearchActivity.this, Browser.class);//从一个activity跳转到另一个activity
        intent.putExtra("targetUrl", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
        startActivity(intent);

    }

    public void hideInput(View view) {
        // 强制隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
