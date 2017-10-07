package com.adymilk.easybrowser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.widget.Toast;


import com.adymilk.easybrowser.por.R;
import com.just.library.AgentWeb;
import com.lb.material_preferences_library.PreferenceActivity;

import java.io.File;

import static com.adymilk.easybrowser.por.Utils.destoryImmersionBar;


public class CommSettingsActivity extends PreferenceActivity {
    private AgentWeb mAgentWeb;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        String darkTheme = getString(R.string.dark);
//        final String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(themePrefKey, defaultTheme);
        final Boolean theme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(darkTheme, false);
        if (!theme){
            setTheme(R.style.AppTheme_Light);
        }else {
            setTheme(R.style.AppTheme_Dark);
        }


        super.onCreate(savedInstanceState);


        findPreference(darkTheme).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                restartActivity(CommSettingsActivity.this);
                return false;
            }
        });

        String app_name = getString(R.string.clear_cache);
        findPreference(app_name).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                /**
                 * TODO: 清除浏览器缓存
                 */

                Toast.makeText(getApplicationContext(), "已清除!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        final String download = getString(R.string.downloadManager);
        findPreference(download).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("file/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                startActivity(intent1);
                return false;
            }
        });


    }


    @Override
    protected int getPreferencesXmlId() {
        return R.xml.settings;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void restartActivity(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            activity.recreate();
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                }
            });
            activity.finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryImmersionBar();
    }
}
