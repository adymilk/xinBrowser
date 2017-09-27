package com.adymilk.easybrowser;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adymilk.easybrowser.por.Browser;
import com.adymilk.easybrowser.por.R;
import com.adymilk.easybrowser.por.SetttingActivity;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 沉浸状态栏
        setContentView(R.layout.activity_search);
        String imaePath = SharedPreferencesUtils.init(this).getString("imaePath");
        if (!(imaePath.isEmpty())){
            ((LinearLayout)findViewById(R.id.search_bg)).setBackground(Drawable.createFromPath(imaePath));
        }


        editText = (EditText) findViewById(R.id.edit_url);
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        clear = (ImageView) findViewById(R.id.clear);
        clear.setVisibility(View.GONE);
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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("字符串长度为："+s.length());
                System.out.println("状态为：beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("字符串长度为："+charSequence.length());
                System.out.println("状态为：onTextChanged");
                if (charSequence.length()!= 0){
                    System.out.println("字符不为空");
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
                    System.out.println("字符为空");
                    clear.setVisibility(View.GONE);
                    btn_submit.setText("取消");
                    clear.setVisibility(View.GONE);
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
                System.out.println("字符串长度为："+s.length());
                System.out.println("状态为：afterTextChanged");
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

//    包含 http 头
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
        intent.setClass(SearchActivity.this, Browser.class);//从一个activity跳转到另一个activity
        intent.putExtra("str", searchKey);//给intent添加额外数据，key为“str”,key值为"Intent Demo"
        startActivity(intent);
    }

    public void hideInput(View view) {
        // 强制隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // 沉浸状态栏
    public void initStatusBar() {
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }
}
