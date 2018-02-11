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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.profile.ProfileActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.settings.SettingsFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.DescriptionFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.LayoutSettingsFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.ThemeChooserFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.WelcomeFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.WelcomePagesActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.WelcomePagesFragment;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AppsChangeObservable {


    public static final String TAG_LAUNCHER_FRAGMENT = "TagLauncherFragment";
    //    public static final String TAG_QUASI_LAUNCHER_FRAGMENT = "TAG_QUASI_LAUNCHER_FRAGMENT";
    public static final String TAG_LAUNCHER_LIST_FRAGMENT = "TAG_LAUNCHER_LIST_FRAGMENT";
    private DrawerLayout drawer;
    private List<AppsChangeObserver> observerList = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.isWhiteTheme(this) ? R.style.AppTheme_WhiteTheme : R.style.AppTheme_BlackTheme);
        setContentView(R.layout.activity_launcher);
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_SCREANS_CREATE, "");
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment launcherFragment = LauncherFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.fragments_container, launcherFragment, TAG_LAUNCHER_FRAGMENT)/*.addToBackStack(null)*/.commit();
        }
//
//        final FragmentManager fragmentManager = getSupportFragmentManager();
//        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragmentManager);
//
//        final ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                ((WelcomePagesFragment) pagerAdapter.getItem(position)).onFrontPagerScreen();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//
        final NavigationView navigationView = findViewById(R.id.nd_menu);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.n_drawer);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_DRAWER_OPEN, "");
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_BACK_PRESS, "");
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

//            case R.id.menu_quasi_launcher_activity_point:
//                startingFragment = com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher.LauncherFragment.newInstance();
//                startingFragmentTag = TAG_QUASI_LAUNCHER_FRAGMENT;
//                break;

            case R.id.menu_list_activity_point:
                startingFragment = LauncherListFragment.newInstance();
                startingFragmentTag = TAG_LAUNCHER_LIST_FRAGMENT;
                break;
        }
        if (startingFragment != null) {
            Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_DRAWER_ITEM_CHOOSE, "");
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.fragments_container, startingFragment, startingFragmentTag).addToBackStack(null).commit();
            //noinspection ConstantConditions
            if (startingFragment instanceof PageForegroundable) {
                ((PageForegroundable) startingFragment).onFrontPagerScreen();
            } else {
                throw new ClassCastException("Fragment mast implement PageForegroundable");
            }
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
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

//    public class ViewPagerAdapter extends FragmentStatePagerAdapter { //TODO move to package
//
//
//        ViewPagerAdapter(@NonNull final FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0: {
//                }
//                case 1: {
//                }
//                case 2: {
//                }
//                case 3: {
//                }
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//
//    }
//
//
//
//
    public class AppsReceiver extends BroadcastReceiver {
        private AppsChangeObservable observable;

        public AppsReceiver(AppsChangeObservable observable) {
            this.observable = observable;
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