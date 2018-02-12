package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.launcher.LaunchPagesFragment;

public class DesktopFragment extends LaunchPagesFragment {

    private WorkSpace workSpace;
    private WorkSpace.AppChooseActivityLauncher appChooseActivityLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("DesktopFragment", "onCreate");
    }

    @Override
    public void onFrontPagerScreen() {
        super.onFrontPagerScreen();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WorkSpace.AppChooseActivityLauncher) {
            appChooseActivityLauncher = (WorkSpace.AppChooseActivityLauncher) context;
            if (workSpace != null) {
                workSpace.setAppChooseActivityLauncher(appChooseActivityLauncher);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        workSpace = (WorkSpace) inflater.inflate(R.layout.desktop_content_layout, container, false);
        workSpace.addItem(new DesktopItemModel("rfh"));
        workSpace.setAppChooseActivityLauncher(appChooseActivityLauncher);
        return workSpace;
    }

    public static DesktopFragment newInstance() {
        //        Bundle args = new Bundle();

        //        fragment.setArguments(args);
        return new DesktopFragment();
    }


}
