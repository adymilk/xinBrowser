package com.adymilk.easybrowser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adymilk.easybrowser.por.R;
import com.gyf.barlibrary.ImmersionBar;
import com.heima.easysp.SharedPreferencesUtils;

import static com.just.library.AgentWebUtils.toastShowShort;

public class BookmarkActivity extends AppCompatActivity {
    private String bookmarkLink;
    private String bookmarkTitle;
    private CardView bookmark_content;
    private TextView tv_bookmark;
    private ImageView iv_back;
    private LinearLayout linearLayout_bookmark;
    private TextView tv_BookmarkTitle;
    private int 书签数量;
    private String bookmarkUrl;
    private ImageView del_bookmark;
    private int i = 0;
    private ImageView clear_Allbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //沉浸状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .init();

        setContentView(R.layout.activity_bookmark);


        // 查询书签数量
        书签数量 = SharedPreferencesUtils.init(BookmarkActivity.this).getInt("书签数量");

        System.out.println("书签总数量为：" + 书签数量 );

        iv_back = (ImageView) findViewById(R.id.iv_back);
        clear_Allbook = (ImageView) findViewById(R.id.clear_Allbook);

        linearLayout_bookmark = (LinearLayout) findViewById(R.id.linearLayout_bookmark);


        //动态添加书签

        if (书签数量 == 0){
            linearLayout_bookmark.addView(addView());
            bookmark_content = (CardView) findViewById(R.id.bookmark_content);
            TextView bookIsEmpty = (TextView) findViewById(R.id.bookIsEmpty);
            bookmark_content.setVisibility(View.GONE);
            bookIsEmpty.setText("没有书签啦～");

        }else if (书签数量 > 0){
            TextView bookIsEmpty = (TextView) findViewById(R.id.bookIsEmpty);
            bookIsEmpty.setVisibility(View.GONE);

            for ( i = 0; i <= 书签数量; i++){
                linearLayout_bookmark.addView(addView());
                tv_BookmarkTitle = (TextView) findViewById(R.id.tv_title);
                del_bookmark = (ImageView) findViewById(R.id.del_bookmark);
                bookmark_content = (CardView) findViewById(R.id.bookmark_content);

                bookmarkTitle = SharedPreferencesUtils.init(BookmarkActivity.this).getString("bookmarkTitle" + i);
                bookmarkUrl = SharedPreferencesUtils.init(BookmarkActivity.this).getString("bookmarkLink" + i);

                tv_BookmarkTitle .setText(bookmarkTitle);
                tv_BookmarkTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(BookmarkActivity.this, com.adymilk.easybrowser.por.Browser.class);
                        intent.putExtra("str", bookmarkUrl);
                        startActivity(intent);
                    }
                });

                //删除书签
                del_bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferencesUtils.init(BookmarkActivity.this).remove("bookmarkTitle" + (i-1) );
                        SharedPreferencesUtils.init(BookmarkActivity.this).remove("bookmarkLink" + (i-1) );
                        bookmark_content.setVisibility(View.GONE);
                    }
                });
                //删除所有书签
                clear_Allbook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferencesUtils.init(BookmarkActivity.this).putInt("书签数量",0 );
                        bookmark_content.setVisibility(View.GONE);
                        TextView bookIsEmpty = (TextView) findViewById(R.id.bookIsEmpty);
                        bookIsEmpty.setText("没有书签啦～");

                        toastShowShort(BookmarkActivity.this, "全部删除！");
                    }
                });

            }
        }





        bookmarkTitle = SharedPreferencesUtils.init(BookmarkActivity.this).getString("bookmarkTitle");
        bookmarkLink = SharedPreferencesUtils.init(BookmarkActivity.this).getString("bookmarkLink");

        System.out.println("你的书签title= " + bookmarkTitle);
        System.out.println("你的书签url= " + bookmarkLink);





        //返回主界面
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(BookmarkActivity.this, Drawer_Main_Activity.class));
                finish();
            }
        });
    }

    private View addView() {
        // TODO 动态添加布局(xml方式)



        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater3 = LayoutInflater.from(this);
        View view = inflater3.inflate(R.layout.bookmark_item, null);
        view.setLayoutParams(lp);

//
        return view;
    }
}
