package com.mortr.soloviev.mdc2018soloviev.ui.launcher;


import android.content.ComponentName;

public interface AppsStartedFromMDCObservable {
    interface AppsStartedFromMDCObserver {
        void onAppsStarting(ComponentName componentName);
    }

    void addAppsStartedFromMDCObserver(AppsStartedFromMDCObserver observer);

    void removeAppsStartedFromMDCObserver(AppsStartedFromMDCObserver observer);

    void notifyAppsStartedFromMDCObserver(ComponentName componentName);
}
