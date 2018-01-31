package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LauncherActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        RecyclerView recyclerView = findViewById(R.id.launcher_recycler);
        int iconCount;
        if (Utils.isStandardLayoutsWasSaved(this)) {
            iconCount = getResources().getInteger(R.integer.standard_icon_counts);
        } else {
            iconCount = getResources().getInteger(R.integer.compact_icon_counts);
        }
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, iconCount);
        recyclerView.setLayoutManager(layoutManager);
        final int offset = getResources().getDimensionPixelOffset(R.dimen.icon_ofset);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(offset, offset, offset, offset);
            }
        });

        final LauncherIconsAdapter adapter = new LauncherIconsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setNewIconsList(generateListIcon(1000));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Random rnd = new Random();
                adapter.setNewIcon(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), 0);
            }
        });
    }

    private List<Integer> generateListIcon(int count) {
        final List<Integer> icons = new ArrayList<>();
        final Random rnd = new Random();
        for (int i = 0; i < count; i++) {
            icons.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }

        return icons;
    }


}
