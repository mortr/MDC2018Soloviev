package com.mortr.soloviev.mdc2018soloviev.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String TAG = "SettingsFragment";
    public static final String KEY_THEME_SWITCH = "theme_switch";
    public static final String KEY_WELCOME_PAGES_SWITCH = "welcome_pages_switch";
    public static final String KEY_LAYOUTS_LIST = "layouts_list";
    public static final String KEY_SORT_TYPE_LIST = "sort_type_list";
    public static final String KEY_PREFERENCE_APP = "preference_apps_switch";
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.v(TAG, "onSharedPChanged " + key);
            final Activity context = getActivity();
            if (context == null) {
                return;
            }
            switch (key) {
                case KEY_THEME_SWITCH: {
                    Utils.applyTheme(context, !Utils.isWhiteTheme(context));
                    break;
                }
                case KEY_WELCOME_PAGES_SWITCH: {
                    Utils.saveWelcomePageShowingState(context, !Utils.isWelcomePageShowed(context));
                    break;
                }
                case KEY_LAYOUTS_LIST: {
                    Utils.saveLayoutSettings(context, getResources().getString(R.string.standard_layout_choose_text)
                            .equals(sharedPreferences.getString(key, null)));
                    Utils.restartCurrentActivity(context);
                    break;
                }
                case KEY_PREFERENCE_APP: {
                    Utils.savePreferenceAppShowingState(context, sharedPreferences.getBoolean(key, false));
//                    Utils.restartCurrentActivity(context);
                    break;
                }
                case KEY_SORT_TYPE_LIST: {
//                    final String  sortData=getResources().getString(R.string.type_sort_date);
//                    String sortAZ=getResources().getString(R.string.type_sort_name_az);
//                    String sortZA=getResources().getString(R.string.type_sort_name_za);
//                    String sortStartCount=getResources().getString(R.string.type_sort_using_count);
//                    String sortDef=getResources().getString(R.string.type_sort_none);
                    Utils.SortType sortType;
                    switch (sharedPreferences.getString(key, "default")) {
                        case "date": {
                            sortType = Utils.SortType.SORT_DATA;
                            break;
                        }
                        case "name (a-Z)": {
                            sortType = Utils.SortType.SORT_AZ;
                            break;
                        }
                        case "name (Z-a)": {
                            sortType = Utils.SortType.SORT_ZA;
                            break;
                        }
                        case "using count": {
                            sortType = Utils.SortType.SORT_START_COUNT;
                            break;
                        }
                        default: {
                            sortType = Utils.SortType.DEFAULT;
                        }

                    }
                    Utils.saveSortSettings(context, sortType);
//                    Utils.restartCurrentActivity(context);
                    break;
                }
            }
        }
    };


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        Context context = getContext();
        if (context != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_THEME_SWITCH, !Utils.isWhiteTheme(context));
            editor.putBoolean(KEY_WELCOME_PAGES_SWITCH, !Utils.isWelcomePageShowed(context));
            editor.putString(KEY_LAYOUTS_LIST,
                    Utils.isStandardLayoutsWasSaved(context)
                            ? getResources().getString(R.string.standard_layout_choose_text)
                            : getResources().getString(R.string.compact_layout_choose_text));
            editor.apply();
        }
        addPreferencesFromResource(R.xml.pref_general);
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        Log.v(TAG, "onCreatePreferences " + rootKey);
    }


}