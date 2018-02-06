package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.profile.ProfileActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AppsChangeObservable {


    public static final String TAG_LAUNCHER_FRAGMENT = "TagLauncherFragment";
    public static final String TAG_QUASI_LAUNCHER_FRAGMENT = "TAG_QUASI_LAUNCHER_FRAGMENT";
    public static final String TAG_LAUNCHER_LIST_FRAGMENT = "TAG_LAUNCHER_LIST_FRAGMENT";
    private DrawerLayout drawer;
    private List<AppsChangeObserver> observerList = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

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
        final NavigationView navigationView = findViewById(R.id.nd_menu);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.n_drawer);
        final View navigationHeaderView = navigationView.getHeaderView(0);
        final View profileAvatar = navigationHeaderView.findViewById(R.id.navigation_header_avatar);
        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(v.getContext(), ProfileActivity.class));
            }
        });

        broadcastReceiver = new AppsReceiver(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment startingFragment = null;
        String startingFragmentTag = null;
        switch (id) {
//            case R.id.menu_device_settings_point:
//                return true;
            case R.id.app_menu_settings_point:
                startingFragment = new SettingsFragment();
                startingFragmentTag = "TAG_SETTINGS_FRAGMENT";
                break;
            case R.id.menu_launcher_activity_point:
                startingFragment = LauncherFragment.newInstance();
                startingFragmentTag = TAG_LAUNCHER_FRAGMENT;

                break;

            case R.id.menu_quasi_launcher_activity_point:
                startingFragment = com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher.LauncherFragment.newInstance();
                startingFragmentTag = TAG_QUASI_LAUNCHER_FRAGMENT;
                break;

            case R.id.menu_list_activity_point:
                startingFragment = LauncherListFragment.newInstance();
                startingFragmentTag = TAG_LAUNCHER_LIST_FRAGMENT;
                break;
        }
        if (startingFragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragments_container, startingFragment, startingFragmentTag)/*.addToBackStack(null)*/.commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void addAppsChangeObserver(AppsChangeObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void removeAppsChangeObserver(AppsChangeObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyAppsChangeObservers() {
        for (AppsChangeObserver observer : observerList) {
            observer.onListApplicationsWasChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    public class AppsReceiver extends BroadcastReceiver {
        private AppsChangeObservable observable;

        public AppsReceiver(AppsChangeObservable observable) {
            this.observable=observable;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("AppsReceiver", "onReceive: " + intent.getAction());
            // This condition will be called when package removed
            if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction()) ||
                    Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                if (observable != null)
                    observable.notifyAppsChangeObservers();
            }
        }

    }
}