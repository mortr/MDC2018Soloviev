package com.mortr.soloviev.mdc2018soloviev.ui.launcher;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

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
        recyclerView.setAdapter(new RecyclerView.Adapter() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View view = new TextView(LauncherActivity.this);
                view.setOnClickListener(onClickListener);
                return new RecyclerView.ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView).setText("position " + position);
                ((TextView) holder.itemView).setBackgroundColor(Color.rgb(new Random().nextInt(), new Random().nextInt(), new Random().nextInt()));
            }

            @Override
            public int getItemCount() {
                return 100;
            }

            final View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), ((TextView) v).getText() + String.valueOf(((ColorDrawable) v.getBackground()).getColor()), Toast.LENGTH_SHORT).show();
                }
            };
        });
    }
}
