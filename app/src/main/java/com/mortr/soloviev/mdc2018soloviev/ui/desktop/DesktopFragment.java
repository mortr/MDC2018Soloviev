package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.db.DBApplicationItem;
import com.mortr.soloviev.mdc2018soloviev.db.DBHelper;
import com.mortr.soloviev.mdc2018soloviev.db.DBUtils;
import com.mortr.soloviev.mdc2018soloviev.ui.launcher.PageForegroundable;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DesktopFragment extends Fragment implements PageForegroundable, ChooseAppReceiverable, DesktopAppRemovable {
//    private boolean isNeedChangeRequestOrientation;
    private WorkSpace workSpace;
    private AppChooseActivityLauncher appChooseActivityLauncher;
    private List<DesktopItemModel> desktopItemModels = new ArrayList<>();
    private WorkspaceDeskAppManagerable workspaceDeskAppManagerable;


    @Override
    public void onFrontPagerScreen() {
//        Activity activity =(Activity) getContext();
//        if (activity != null) {
//            if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
//            isNeedChangeRequestOrientation = false;
//        } else {
//            isNeedChangeRequestOrientation = true;
//        }

        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_PAGE_ON_FOREGROUND, this.getClass().getName());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        DBHelper dbHelper = new DBHelper(getContext());
        for (DBApplicationItem item : DBUtils.getDesktopDBApplicationItemFromDB(dbHelper.getReadableDatabase())) {
            desktopItemModels.add(new DesktopItemModel(item));
        }
        dbHelper.close();
        Log.d("DesktopFragment", "onCreate");
    }

    @Override
    public void chooseAppReceive(ComponentName componentName, float x, float y) {
        DesktopItemModel model = new DesktopItemModel(componentName, x, y);
        if (desktopItemModels != null) {
            desktopItemModels.add(model);
        }
        if (workSpace != null) {
            workSpace.addItem(model, model.getX(), model.getY());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppChooseActivityLauncher) {
            appChooseActivityLauncher = (AppChooseActivityLauncher) context;
            if (workSpace != null) {
                workSpace.setAppChooseActivityLauncher(appChooseActivityLauncher);
                appChooseActivityLauncher.setChooseAppReceiverable(this);
            }
        }
        if (context instanceof WorkspaceDeskAppManagerable) {
            workspaceDeskAppManagerable = (WorkspaceDeskAppManagerable) context;
            ((WorkspaceDeskAppManagerable) context).setDesktopAppRemovable(this);
        }
//        if (isNeedChangeRequestOrientation) {
//            if (((Activity) context).getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                ((Activity) context).setRequestedOrientation((ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
//                isNeedChangeRequestOrientation=false;
//            }
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        workSpace = (WorkSpace) inflater.inflate(R.layout.desktop_content_layout, container, false);
        refreshWorkspace();
        if (appChooseActivityLauncher != null) {
            workSpace.setAppChooseActivityLauncher(appChooseActivityLauncher);
            appChooseActivityLauncher.setChooseAppReceiverable(this);
        }
        if (workspaceDeskAppManagerable != null) {
            workSpace.setWorkspaceDeskAppManagerable(workspaceDeskAppManagerable);
        }
        return workSpace;
    }

    private void refreshWorkspace() {
        workSpace.removeAllViews();
        if (desktopItemModels != null) {
            for (DesktopItemModel item : desktopItemModels) {
                workSpace.addItem(item, item.getX(), item.getY());
            }
        }
    }

    public static DesktopFragment newInstance() {
        //        Bundle args = new Bundle();

        //        fragment.setArguments(args);
        return new DesktopFragment();
    }


    @Override
    public void removeAppFromDesktop(ComponentName componentName) {
        int length = desktopItemModels.size();
        int index = -1;
        for (int i = 0; i < length; i++) {

            if (desktopItemModels.get(i).getComponentName().equals(componentName)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            desktopItemModels.remove(index);
            DBHelper dbHelper = new DBHelper(getContext());
            DBUtils.deleteAppFromDesktop(dbHelper, componentName);
            dbHelper.close();
            refreshWorkspace();
        }

    }
}
