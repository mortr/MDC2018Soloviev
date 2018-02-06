package com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LauncherFragment extends Fragment {
    public static final String TAG = "LauncherFragment";
    public static final int OFFSET_WAS_NOT_RESSIVED = -1;
    private int columnCount;
    private int offset = OFFSET_WAS_NOT_RESSIVED;
    private LauncherIconsAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LauncherIconsAdapter();
        adapter.setNewIconsList(getListIcon());
        setRetainInstance(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Utils.isStandardLayoutsWasSaved(context)) {
            columnCount = getResources().getInteger(R.integer.standard_icon_counts);
        } else {
            columnCount = getResources().getInteger(R.integer.compact_icon_counts);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quasi_launcher_content_layout, container, false);
    }

    private List<Integer> getListIcon() {
        final List<Integer> icons = new ArrayList<>();
        final Random rnd = new Random();
        for (int i = 0; i < 1000; i++) {
            icons.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
        return icons;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.launcher_recycler);
        Log.d(TAG, "onViewCreated bundle = " + savedInstanceState);
        Log.d(TAG, "onViewCreated recyclerView.getAdapter() = " + recyclerView.getAdapter());
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), columnCount);
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
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.fab).setOnClickListener(fabClickListener);

    }

    public static LauncherFragment newInstance() {

//        Bundle args = new Bundle();
//        args.putInt("KEY_COLUMN_COUNT", columnCount);
        //        fragment.setArguments(args);
        return new LauncherFragment();

    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Random rnd = new Random();
            adapter.setNewIcon(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), 0);
        }
    };

}
