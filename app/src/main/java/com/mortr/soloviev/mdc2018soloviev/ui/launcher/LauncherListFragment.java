package com.mortr.soloviev.mdc2018soloviev.ui.launcher;


import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.List;

import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.getListApplications;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.getSortedApps;

public class LauncherListFragment extends LaunchPagesFragment {

    public static final String TAG = "LauncherFragment";
    public static final int OFFSET_WAS_NOT_RESSIVED = -1;
    private int offset = OFFSET_WAS_NOT_RESSIVED;


    @Override
    LauncherApplicationsAdapter createAdapter() {
        return new LauncherApplicationsAdapter(R.layout.app_three_line_list_item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.launcher_content_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.launcher_recycler);
        Log.d(TAG, "onViewCreated bundle = " + savedInstanceState);
        Log.d(TAG, "onViewCreated recyclerView.getAdapter() = " + recyclerView.getAdapter());
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (offset == OFFSET_WAS_NOT_RESSIVED) {
            offset = getResources().getDimensionPixelOffset(R.dimen.icon_ofset);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(offset, offset, offset, offset);
                }
            });
        }
        recyclerView.setAdapter(getAdapter());


    }

    public static LauncherListFragment newInstance() {

//        Bundle args = new Bundle();
//        args.putInt("KEY_COLUMN_COUNT", columnCount);
        //        fragment.setArguments(args);
        return new LauncherListFragment();

    }

}
