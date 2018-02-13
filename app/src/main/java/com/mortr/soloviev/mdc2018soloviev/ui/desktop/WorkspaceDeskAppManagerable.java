package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;

public interface WorkspaceDeskAppManagerable {
    void onDeskAppLongClick(ComponentName componentName);
    void onDeskAppClick(ComponentName componentName);
    void setDesktopAppRemovable(DesktopAppRemovable desktopAppRemovable);
}
