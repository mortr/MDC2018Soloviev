package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public abstract class LaunchPagesFragment extends Fragment implements PageForegroundable {

    public static final String TAG = "LaunchPagesFr";


    public void onFrontPagerScreen() {
        Log.d(TAG, this.getClass().getName()+"onFrontScreen()");
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_PAGE_ON_FOREGROUND,LaunchPagesFragment.this.getClass().getName());
    }


}
