package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;
import android.view.View;

public interface WorkspaceDeskAppManagerable {
    void onDeskAppLongClick(ComponentName componentName, View v);
    void onDeskAppClick(ComponentName componentName, View v);
    void setDesktopAppRemovable(DesktopAppRemovable desktopAppRemovable);
}
