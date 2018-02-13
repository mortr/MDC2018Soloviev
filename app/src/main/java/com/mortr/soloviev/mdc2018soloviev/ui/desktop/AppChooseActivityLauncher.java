package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.widget.TextView;

public interface AppChooseActivityLauncher {
    void startAppChooseActivity(float x, float y, TextView archor);

    void setChooseAppReceiverable(ChooseAppReceiverable chooseAppReceiverable);
}
