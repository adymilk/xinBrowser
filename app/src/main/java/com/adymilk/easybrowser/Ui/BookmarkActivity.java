package com.adymilk.easybrowser.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.adymilk.easybrowser.por.Browser;
import com.adymilk.easybrowser.por.R;
import com.heima.easysp.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adymilk.easybrowser.Utils.comm.initBar;
import static com.adymilk.easybrowser.Utils.comm.slideActivity;


public class BookmarkActivity extends Activity {
    private ListView lv_book_Main;
    private TextView tv_bookIsEmpty;
    private List<Map<String, Object>> data;
    private int 书签数量;
    private String 书签Url;
    private String 书签标题;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 沉浸状态栏
        initBar(this);
        slideActivity(this);
        setContentView(R.layout.activity_bookmark);


        lv_book_Main = (ListView) findViewById(R.id.lv_book_Main);
        ImageView clear_Allbook = (ImageView) findViewById(R.id.clear_Allbook);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_bookIsEmpty = (TextView) findViewById(R.id.tv_bookIsEmpty);
        书签数量 = SharedPreferencesUtils.init(BookmarkActivity.this).getInt("书签数量");
        if (书签数量 == 0){
            lv_book_Main.setVisibility(View.GONE);

            tv_bookIsEmpty.setVisibility(View.VISIBLE);
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clear_Allbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("警告！","确定要全部删除吗？");
            }
        });



        //获取将要绑定的数据设置到data中
        data = getData();
        MyAdapter adapter = new MyAdapter(this);
        lv_book_Main.setAdapter(adapter);

        lv_book_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                书签Url = SharedPreferencesUtils.init(BookmarkActivity.this).getString("bookmarkLink" + Integer.toString(i+1) );
                System.out.println("书签Url"+书签Url);
                Intent intent = new Intent();
                intent.setClass(BookmarkActivity.this, Browser.class);
                intent.putExtra("targetUrl", 书签Url);
                System.out.println();
                startActivity(intent);
            }
        });

    }

    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        书签数量 = SharedPreferencesUtils.init(BookmarkActivity.this).getInt("书签数量");
        for(int i=1; i<= 书签数量; i++)
        {
            书签标题 = SharedPreferencesUtils.init(BookmarkActivity.this).getString("bookmarkTitle" + Integer.toString(i));
            System.out.println("书签为：" + 书签标题);
            map = new HashMap<String, Object>();
            map.put("title", 书签标题);
            map.put("img", R.mipmap.delete);
            list.add(map);
        }
        return list;
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public TextView title;
        public ImageView iv_del;
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
                convertView = mInflater.inflate(R.layout.bookmark_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.tv_book_title);
                holder.iv_del = (ImageView)convertView.findViewById(R.id.del_bookmark);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.title.setText((String)data.get(position).get("title"));
//            holder.iv_del.setImageResource((Integer)data.get(position).get("img"));

            return convertView;
        }

    }

    //    对话框方法
    public void showDialog(String title,String msg){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BookmarkActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(R.string.disagree, null);
        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferencesUtils.init(BookmarkActivity.this).putInt("书签数量",0);
                lv_book_Main.setVisibility(View.GONE);

                tv_bookIsEmpty.setVisibility(View.VISIBLE);
            }
        });
        builder.show();
    }

    //del Dialog
    public void showDelItemDialog(String title,String msg, int i){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BookmarkActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(R.string.disagree, null);
        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                SharedPreferencesUtils.init(BookmarkActivity.this).putInt("书签数量",书签数量 - 1);
                SharedPreferencesUtils.init(BookmarkActivity.this).putInt("bookmarkTitle",0);
                SharedPreferencesUtils.init(BookmarkActivity.this).putInt("bookmarkLink",0);
//                SharedPreferencesUtils.init(BookmarkActivity.this).remove("bookmarkTitle" + Integer.toString(i+1));
//                SharedPreferencesUtils.init(BookmarkActivity.this).remove("bookmarkLink" + Integer.toString(i+1));


            }
        });
        builder.show();
    }

}
