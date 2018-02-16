package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
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
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.db.DBHelper;
import com.mortr.soloviev.mdc2018soloviev.db.DBUtils;
import com.mortr.soloviev.mdc2018soloviev.ui.desktop.AppChooseActivityLauncher;
import com.mortr.soloviev.mdc2018soloviev.ui.desktop.AppChooserActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.desktop.ChooseAppReceiverable;
import com.mortr.soloviev.mdc2018soloviev.ui.desktop.DesktopAppMovable;
import com.mortr.soloviev.mdc2018soloviev.ui.desktop.DesktopFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.desktop.WorkspaceDeskAppManagerable;
import com.mortr.soloviev.mdc2018soloviev.ui.profile.ProfileActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.settings.SettingsFragment;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.mortr.soloviev.mdc2018soloviev.ui.desktop.AppChooserActivity.KEY_COMPONENT_NAME;


public class LauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AppsChangeObservable, AppChooseActivityLauncher,
        LauncherApplicationsAdapter.AppItemClickListener, WorkspaceDeskAppManagerable {


    public static final String TAG_LAUNCHER_FRAGMENT = "TagLauncherFragment";
    //    public static final String TAG_QUASI_LAUNCHER_FRAGMENT = "TAG_QUASI_LAUNCHER_FRAGMENT";
    public static final String TAG_LAUNCHER_LIST_FRAGMENT = "TAG_LAUNCHER_LIST_FRAGMENT";
    public static final String TAG_SETTINGS_FRAGMENT = "TAG_SETTINGS_FRAGMENT";
    public static final String TAG_DESKTOP_FRAGMENT = "TAG_DESKTOP_FRAGMENT";
    public static final int REQUEST_CODE_DESKTOP_APP_CHOOSER = 12;
    public static final String DESKTOP_X = "X";
    public static final String DESKTOP_Y = "Y";

    private DrawerLayout drawer;
    private List<AppsChangeObserver> observerList = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private ViewPager viewPager;
    @Nullable
    private ChooseAppReceiverable chooseAppReceiverable;
    @Nullable
    private Bundle placeCoordinatesForAppChoose;
    @Nullable
    private DesktopAppMovable desktopAppMovable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.isWhiteTheme(this) ? R.style.AppTheme_WhiteTheme : R.style.AppTheme_BlackTheme);
        setContentView(R.layout.activity_launcher);
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_SCREANS_CREATE, "");
//        if (savedInstanceState == null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            Fragment launcherFragment = LauncherFragment.newInstance();
//            fragmentManager.beginTransaction().add(R.id.fragments_container, launcherFragment, TAG_LAUNCHER_FRAGMENT)/*.addToBackStack(null)*/.commit();
//        }
//
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragmentManager);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(ViewPagerAdapter.FRAGMENT_COUNT);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment startingFragment = pagerAdapter.getItem(position);
                if (startingFragment instanceof PageForegroundable) {
                    ((PageForegroundable) startingFragment).onFrontPagerScreen();
                } else {
                    throw new ClassCastException("Fragment mast implement PageForegroundable");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        IntentFilter intentFilter = new IntentFilter();
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
                viewPager.setCurrentItem(3);
//                startingFragment = new SettingsFragment();
//                startingFragmentTag = TAG_SETTINGS_FRAGMENT;
                break;
            case R.id.menu_launcher_activity_point:
//                startingFragment = LauncherFragment.newInstance();
//                startingFragmentTag = TAG_LAUNCHER_FRAGMENT;
                viewPager.setCurrentItem(1);
                break;

//            case R.id.menu_quasi_launcher_activity_point:
//                startingFragment = com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher.LauncherFragment.newInstance();
//                startingFragmentTag = TAG_QUASI_LAUNCHER_FRAGMENT;
//                break;

            case R.id.menu_list_activity_point:
                viewPager.setCurrentItem(2);
//                startingFragment = LauncherListFragment.newInstance();
//                startingFragmentTag = TAG_LAUNCHER_LIST_FRAGMENT;
                break;
            case R.id.menu_desktop_point:
                viewPager.setCurrentItem(0);
                break;
        }
//        if (startingFragment != null) {
//            Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_DRAWER_ITEM_CHOOSE, "");
//            fragmentManager.popBackStack();
//            fragmentManager.beginTransaction().replace(R.id.fragments_container, startingFragment, startingFragmentTag).addToBackStack(null).commit();
//            //noinspection ConstantConditions
//            if (startingFragment instanceof PageForegroundable) {
//                ((PageForegroundable) startingFragment).onFrontPagerScreen();
//            } else {
//                throw new ClassCastException("Fragment mast implement PageForegroundable");
//            }
//        }
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

    @Override
    public void startAppChooseActivity(float x, float y, TextView archor) {

        placeCoordinatesForAppChoose = new Bundle();//TODO it is needed to refactor
        placeCoordinatesForAppChoose.putFloat(DESKTOP_X, x);
        placeCoordinatesForAppChoose.putFloat(DESKTOP_Y, y);
        createApplicationChoosePopupMenu(archor).show();
    }

    @NonNull
    public PopupMenu createApplicationChoosePopupMenu(final View v) {
        final PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.choose_app_context_menu);
        final DBHelper dbHelper = new DBHelper(v.getContext());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_choose_app: {
                        startActivityForResult(new Intent(LauncherActivity.this, AppChooserActivity.class), REQUEST_CODE_DESKTOP_APP_CHOOSER);
                    return true;
                    }

                }
                placeCoordinatesForAppChoose = null;
                return false;
            }
        });
        return popup;
    }


    @Override
    public void setChooseAppReceiverable(@Nullable ChooseAppReceiverable chooseAppReceiverable) {
        this.chooseAppReceiverable = chooseAppReceiverable;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DESKTOP_APP_CHOOSER) {
            ComponentName componentName = (ComponentName) data.getParcelableExtra(KEY_COMPONENT_NAME);
            final DBHelper dbHelper = new DBHelper(this);       // TODO  single DBhelper for activity
            DBUtils.saveDesktopApp(dbHelper, componentName, placeCoordinatesForAppChoose, this);
            dbHelper.close();
            if (chooseAppReceiverable != null && placeCoordinatesForAppChoose != null) {

                chooseAppReceiverable.chooseAppReceive(componentName,
                        placeCoordinatesForAppChoose.getFloat(DESKTOP_X, 64),
                        placeCoordinatesForAppChoose.getFloat(DESKTOP_Y, 64));
            }
        }
    }

    @Override
    public void onClickApplicationItem(ResolveInfo appInfo, View v) {
        final DBHelper dbHelper = new DBHelper(v.getContext());
        DBUtils.onStartApp(appInfo, v.getContext(), dbHelper.getWritableDatabase());
        dbHelper.close();
        Utils.launchApp(appInfo, this);
    }

    @Override
    public void onLongClickApplicationItem(ResolveInfo appInfo, View v) {

        PopupMenu popupMenu = Utils.createApplicationPopupMenu(v, appInfo);
        popupMenu.show();

    }

    @Override
    public void onDeskAppLongClick(@Nullable final ComponentName componentName, View v) {
        if (desktopAppMovable != null && componentName != null) {
            final PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.desktop_app_context_menu);
            final DBHelper dbHelper = new DBHelper(v.getContext());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_delete: {
                            desktopAppMovable.removeAppFromDesktop(componentName);
                        }

                    }
                    return false;
                }
            });
            popup.show();
        }
    }


    @Override
    public void onDeskAppClick(@Nullable ComponentName componentName, View v) {
        if (componentName != null) {
            Utils.launchApp(componentName, this);
        }
    }

    @Override
    public void onDeskAppChangePlace(ComponentName componentName, View v) {


        float x = v.getX();
        float y = v.getY();

        if (desktopAppMovable!=null){
            desktopAppMovable.moveAppFromDesktop(componentName, x, y);
        }
    }

    @Override
    public void setDesktopAppMovable(@Nullable DesktopAppMovable desktopAppMovable) {
        this.desktopAppMovable = desktopAppMovable;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter { //TODO move to package
        static final int FRAGMENT_COUNT = 4;
        //        String[] fragmentsTag = {TAG_DESKTOP_FRAGMENT, TAG_LAUNCHER_FRAGMENT, TAG_LAUNCHER_LIST_FRAGMENT, TAG_SETTINGS_FRAGMENT};
        SparseArray<Fragment> fragments = new SparseArray<>(FRAGMENT_COUNT);

        ViewPagerAdapter(@NonNull final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragments.get(position);
            if (fragment != null) {
                return fragment;
            }
            switch (position) {
                case 0: {
                    fragments.put(position, DesktopFragment.newInstance());
                    break;
                }
                case 1: {
                    fragments.put(position, LauncherFragment.newInstance());
                    break;
                }
                case 2: {
                    fragments.put(position, LauncherListFragment.newInstance());
                    break;
                }
                case 3: {
                    fragments.put(position, new SettingsFragment());
                    break;
                }
            }

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }

    }


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