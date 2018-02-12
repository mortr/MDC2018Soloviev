package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.launcher.LaunchPagesFragment;
import com.mortr.soloviev.mdc2018soloviev.ui.launcher.PageForegroundable;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

public class DesktopFragment extends Fragment implements PageForegroundable {

    private WorkSpace workSpace;
    private AppChooseActivityLauncher appChooseActivityLauncher;


    @Override
    public void onFrontPagerScreen() {
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_PAGE_ON_FOREGROUND, this.getClass().getName());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("DesktopFragment", "onCreate");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppChooseActivityLauncher) {
            appChooseActivityLauncher = (AppChooseActivityLauncher) context;
            if (workSpace != null) {
                workSpace.setAppChooseActivityLauncher(appChooseActivityLauncher);
                appChooseActivityLauncher.setChooseAppReceiverable(workSpace);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        workSpace = (WorkSpace) inflater.inflate(R.layout.desktop_content_layout, container, false);
        workSpace.addItem(new DesktopItemModel("rfh"),60,60);
        if (appChooseActivityLauncher != null) {
            workSpace.setAppChooseActivityLauncher(appChooseActivityLauncher);
            appChooseActivityLauncher.setChooseAppReceiverable(workSpace);
        }
        return workSpace;
    }

    public static DesktopFragment newInstance() {
        //        Bundle args = new Bundle();

        //        fragment.setArguments(args);
        return new DesktopFragment();
    }


}
