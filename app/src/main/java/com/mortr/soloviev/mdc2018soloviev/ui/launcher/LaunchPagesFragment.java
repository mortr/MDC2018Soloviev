package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.getListApplications;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.getSortedApps;


public abstract class LaunchPagesFragment extends Fragment implements PageForegroundable, AppsChangeObservable.AppsChangeObserver {
    @Nullable
    private LauncherApplicationsAdapter adapter;
    private Utils.SortType sortType = Utils.SortType.DEFAULT;
    public static final String TAG = "LaunchPagesFr";
    private LauncherApplicationsAdapter.AppItemClickListener appItemClickListener;
//    private boolean isNeedChangeRequestOrientation;

    @Nullable
    public LauncherApplicationsAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = createAdapter();

        LauncherApplicationsAdapter.AppItemClickListener appItemClickListener = getAppItemClickListener();
        if (appItemClickListener != null) {
            adapter.setAppItemClickListener(appItemClickListener);
        }
        Context context = getContext();
        sortType = Utils.getTypeSort(context);
        refreshAppsToAdapter(sortType, getSortedApps(getListApplications(context), sortType, context));
        setRetainInstance(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppsChangeObservable) {
            ((AppsChangeObservable) context).addAppsChangeObserver(this);
        }
        if (context instanceof LauncherApplicationsAdapter.AppItemClickListener) {
            appItemClickListener = (LauncherApplicationsAdapter.AppItemClickListener) context;
            if (adapter != null) {
                adapter.setAppItemClickListener(appItemClickListener);
                checkSortType();
            }
        }
//        if (isNeedChangeRequestOrientation){
//            if (((Activity)context).getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
//                ((Activity)context).setRequestedOrientation((ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
//            }
//            isNeedChangeRequestOrientation=false;
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();
        Utils.SortType newSortType = Utils.getTypeSort(context);
//        if (!sortType.equals(newSortType)) {
//            refreshAppsToAdapter(newSortType, getSortedApps(getListApplications(context), newSortType, context));
//        }
        if (Utils.isPreferenceAppShowed(context)) {
            addPreferenseApps();
        }
    }

    private void addPreferenseApps() {//todo

    }

    public LauncherApplicationsAdapter.AppItemClickListener getAppItemClickListener() {
        return appItemClickListener;
    }

    public void onFrontPagerScreen() {
        Log.d(TAG, this.getClass().getName() + "onFrontScreen()");
//        Activity activity = getActivity();
//        if (activity != null) {
//            if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
//                activity.setRequestedOrientation((ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED));
//            }
//            isNeedChangeRequestOrientation = false;
//        } else {
//            isNeedChangeRequestOrientation = true;
//        }
        checkSortType();
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_PAGE_ON_FOREGROUND, LaunchPagesFragment.this.getClass().getName());
    }

    private void refreshAppsToAdapter(Utils.SortType newSortType, List<ResolveInfo> sortedApps) {
        sortType = newSortType;
        if (adapter != null) {
            adapter.setNewAppList(sortedApps);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Context context = getContext();
        if (context instanceof AppsChangeObservable) {
            ((AppsChangeObservable) context).removeAppsChangeObserver(this);
        }
    }

    @Override
    public void onListApplicationsWasChanged() {
        if (adapter != null) {
            checkSortType();
        }
    }

    private void checkSortType() {//TODO maybe it is needed move to Activity(Observer-Observable)
        Context context = getContext();
        if (context == null) {
            return;
        }
        Utils.SortType newSortType = Utils.getTypeSort(context);
        if (!sortType.equals(newSortType)) {
            refreshAppsToAdapter(newSortType, getSortedApps(getListApplications(context), newSortType, context));//TODO move to asynctask
        }
    }

    abstract LauncherApplicationsAdapter createAdapter();
}
