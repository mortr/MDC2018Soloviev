package com.mortr.soloviev.mdc2018soloviev.ui.desktop;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.launcher.LauncherApplicationsAdapter;


public class AppChooserActivity extends AppCompatActivity implements LauncherApplicationsAdapter.AppItemClickListener {

    public static final String KEY_COMPONENT_NAME = "ComponentName";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_chooser);
    }


    @Override
    public void onClickApplicationItem(ResolveInfo appInfo, View v) {
        Intent intent = new Intent();
        intent.putExtra(KEY_COMPONENT_NAME, new ComponentName(appInfo.activityInfo.packageName,appInfo.activityInfo.name));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLongClickApplicationItem(ResolveInfo appInfo, View v) {

    }
}
