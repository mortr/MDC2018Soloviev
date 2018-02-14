package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;

public interface DesktopAppMovable {
    void removeAppFromDesktop(ComponentName componentName);
    void moveAppFromDesktop(ComponentName componentName,float x,float y);
}
