package com.adymilk.easybrowser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;


import com.adymilk.easybrowser.por.R;
import com.lb.material_preferences_library.PreferenceActivity;
import com.lb.material_preferences_library.custom_preferences.ListPreference;


public class CommSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final String themePrefKey = getString(R.string.pref_theme), defaultTheme = getResources().getString(R.string.pref_theme_default);
        final String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(themePrefKey, defaultTheme);
        switch (theme) {
            case "dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            case "light":
                setTheme(R.style.AppTheme_Light);
                break;
        }

        super.onCreate(savedInstanceState);


        ListPreference themeListPreference = (ListPreference) findPreference(getString(R.string.pref_theme));
        themeListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                restartActivity(CommSettingsActivity.this);
                return true;
            }
        });
        String app_name = getString(R.string.clear_cache);
        findPreference(app_name).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                /**
                 * TODO: 清除浏览器缓存
                 */
                Toast.makeText(getApplicationContext(), "已清除缓存", Toast.LENGTH_SHORT).show();
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
