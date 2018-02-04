package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LauncherActivity extends AppCompatActivity {


    public static final String TAG_LAUNCHER_FRAGMENT = "TagLauncherFragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
//        ViewGroup container=findViewById(R.id.fragments_container);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment launcherFragment = LauncherFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.fragments_container, launcherFragment, TAG_LAUNCHER_FRAGMENT).commit();
        }



    }


}
