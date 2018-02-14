package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;
import android.view.View;

public interface WorkspaceDeskAppManagerable {
    void onDeskAppLongClick(ComponentName componentName, View v);
    void onDeskAppClick(ComponentName componentName, View v);
    void onDeskAppChangePlace(ComponentName componentName, View v);
    void setDesktopAppMovable(DesktopAppMovable desktopAppMovable);
}
