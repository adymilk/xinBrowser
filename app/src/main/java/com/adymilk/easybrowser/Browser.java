package com.adymilk.easybrowser;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.adymilk.easybrowser.por.R;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.just.library.AgentWeb;
import com.just.library.FragmentKeyDown;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;



public class Browser extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private FragmentManager mFragmentManager;
    private com.adymilk.easybrowser.por.AgentWebFragment mAgentWebFragment;
    private ImmersionBar mImmersionBar;
    private AgentWeb mAgentWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBarAndSildeActivity();

        setContentView(com.adymilk.easybrowser.por.R.layout.activity_browser);


        if (mAgentWeb != null)
        mAgentWeb.getWebCreator().getGroup().setBackgroundColor(getResources().getColor(R.color.black));

        Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String str=bundle.getString("str");//getString()返回指定key的值

        mFrameLayout = (FrameLayout) this.findViewById(com.adymilk.easybrowser.por.R.id.container_framelayout);


        mFragmentManager = this.getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        Bundle mBundle = null;

        ft.add(com.adymilk.easybrowser.por.R.id.container_framelayout, mAgentWebFragment = com.adymilk.easybrowser.por.SmartRefreshWebFragment.getInstance(mBundle = new Bundle()), com.adymilk.easybrowser.por.SmartRefreshWebFragment.class.getName());
        mBundle.putString(com.adymilk.easybrowser.por.AgentWebFragment.URL_KEY, str);
        ft.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
        mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        com.adymilk.easybrowser.por.AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event))
                return true;
            else
                return super.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void initBarAndSildeActivity(){
        // 沉浸状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .init();

        //滑动隐藏 Activity
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .build();
        Slidr.attach(this, config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null){
            mImmersionBar.destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }

    }
    /* 创建菜单 */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "分享");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // intent.setType("text/plain"); //纯文本
            /*
             * 图片分享 it.setType("image/png"); 　//添加图片 File f = new
             * File(Environment.getExternalStorageDirectory()+"/name.png");
             *
             * Uri uri = Uri.fromFile(f); intent.putExtra(Intent.EXTRA_STREAM,
             * uri); 　
             */
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                return true;
        }
        return false;
    }
}
