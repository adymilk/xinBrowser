package com.adymilk.easybrowser.Ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adymilk.easybrowser.por.R;
import com.show.api.ShowApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class BarcodeActivity extends AppCompatActivity {
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化图片框架
//        Fresco.initialize(this);
        //条形码扫描结果展示页面
        setContentView(R.layout.activity_barcode);

        // toobar
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String barcode = bundle.getString("barcode");

        final TextView goods_name = (TextView) findViewById(R.id.goods_name);
        final TextView goods_prodAddr = (TextView) findViewById(R.id.goods_prodAddr);
        final TextView goods_type = (TextView) findViewById(R.id.goods_goodsType);
        final TextView goods_spec = (TextView) findViewById(R.id.goods_spec);
        final TextView goods_zpg = (TextView) findViewById(R.id.zpg);
        final TextView goods_trademark = (TextView) findViewById(R.id.trademark);
        final TextView goods_code = (TextView) findViewById(R.id.code);
        final TextView goods_manuName = (TextView) findViewById(R.id.manuName);


        // TODO：解析 条形码接口返回的 json 数据
        new Thread() {
            //在新线程中发送网络请求
            public void run() {
                String appid = "47350";//要替换成自己的
                String secret = "80c96b01d8d142979a7eef72cefc32c4";//要替换成自己的
                final String res = new ShowApiRequest("http://route.showapi.com/66-22", appid, secret)
                        .addTextPara("code", barcode)
                        .post();

                System.out.println("返回的json" + res);
                //把返回内容通过handler对象更新到界面
                mHandler.post(new Thread() {
                    public void run() {
//                              // 解析json
                        try {
                            JSONObject jsonObj = new JSONObject(res).getJSONObject("showapi_res_body");
//
                            String ImgUrl = jsonObj.getString("img");

                            System.out.println("jsonObj对象=" + res);
                            goods_name.setText(jsonObj.getString("goodsName"));
                            goods_prodAddr.setText("产地：" + jsonObj.getString("prodAddr"));
                            goods_type.setText("规格：" + jsonObj.getString("goodsType"));
                            goods_spec.setText("分类：" + jsonObj.getString("spec"));
                            goods_zpg.setText("国家：" + jsonObj.getString("zpg"));
                            goods_trademark.setText("商标：" + jsonObj.getString("trademark"));
                            goods_code.setText("代码：" + jsonObj.getString("code"));
                            goods_manuName.setText("厂商：" + jsonObj.getString("manuName"));

                            //加载图片
                            new load_image().execute(ImgUrl);

                        } catch (JSONException e) {
                            System.out.println("Json parse error");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }


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
            de.hdodenhof.circleimageview.CircleImageView imageView = (CircleImageView) findViewById(R.id.goods_image);
            imageView.setImageDrawable(result);
        }

    }


}
