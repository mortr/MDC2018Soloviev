package com.mortr.soloviev.mdc2018soloviev.ui.launcher;


import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mortr.soloviev.mdc2018soloviev.R;

import java.util.ArrayList;
import java.util.List;

public class LauncherApplicationsAdapter extends RecyclerView.Adapter<LauncherApplicationsAdapter.Holder> {
    private static final String TAG = "LauncherAppsAdapter";
    @Nullable
    private RecyclerView recycler;

    class Holder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickListener);

            itemView.setOnLongClickListener(onLongClickListener);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
        }

        void setApp(ApplicationInfo app, int position) {
//            ((TextView) itemView).setText(position + "\n" + Integer.toHexString(app));
            appName.setText(app.name);
            appIcon.setImageDrawable(app.loadIcon(itemView.getContext().getPackageManager()));
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recycler = recyclerView;
    }

    private List<ApplicationInfo> applicationInfos = new ArrayList<>();

    public LauncherApplicationsAdapter() {

    }


    public void setNewIconsList(List<ApplicationInfo> icons) {
        this.applicationInfos.clear();
        this.applicationInfos.addAll(icons);
        notifyDataSetChanged();
        Log.v(TAG, "setNewIconsList");
    }

    public void setNewIcon(Integer icon, int insertPosition) {

//        applicationInfos.add(insertPosition, icon);
        notifyItemInserted(insertPosition);
        Log.v(TAG, "setNewIcon " + insertPosition);
    }

    public void deleteIcon(int iconPosition) {
        applicationInfos.remove(iconPosition);
        notifyItemRemoved(iconPosition);
        Log.v(TAG, " deleteIcon " + iconPosition);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setApp(applicationInfos.get(position), position);

    }


    @Override
    public int getItemCount() {
        return applicationInfos.size();
    }

    final private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "onclick",Toast.LENGTH_LONG).show();
        }

    };

    final private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (recycler != null) {
                int position = 0;
                RecyclerView.ViewHolder viewHolder = recycler.findContainingViewHolder(v);
                if (viewHolder != null) {
                    position = viewHolder.getAdapterPosition();
                }
                final int deletingAdapterPosition = position;
                Snackbar snackbar = Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction(String.format("Delete icon № %s", position + 1), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteIcon(deletingAdapterPosition);
                            }
                        });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        Log.v(TAG, "Snackbar.Callback onDismissed ");
                    }

                    @Override
                    public void onShown(Snackbar sb) {
                        super.onShown(sb);
                        Log.v(TAG, "Snackbar.Callback onShown");
                    }
                });
                snackbar.show();

            }
            return true;
        }
    };
}


