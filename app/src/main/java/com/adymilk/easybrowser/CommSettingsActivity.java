package com.adymilk.easybrowser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;


import com.adymilk.easybrowser.por.R;
import com.just.library.AgentWeb;
import com.lb.material_preferences_library.PreferenceActivity;
import com.lb.material_preferences_library.custom_preferences.ListPreference;


public class CommSettingsActivity extends PreferenceActivity {
    private AgentWeb mAgentWeb;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//        final String themePrefKey = getString(R.string.pref_theme), defaultTheme = getResources().getString(R.string.pref_theme_default);
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

                mAgentWeb.clearWebCache();
                Toast.makeText(getApplicationContext(), "已清除!", Toast.LENGTH_SHORT).show();
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

}
