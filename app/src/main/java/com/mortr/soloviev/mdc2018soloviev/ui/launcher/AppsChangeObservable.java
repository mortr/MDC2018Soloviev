package com.mortr.soloviev.mdc2018soloviev.ui.launcher;


public interface AppsChangeObservable {
    public interface AppsChangeObserver {
        void onListApplicationsWasChanged();
    }

    void addAppsChangeObserver(AppsChangeObserver observer);

    void removeAppsChangeObserver(AppsChangeObserver observer);

    void notifyAppsChangeObservers();
}
