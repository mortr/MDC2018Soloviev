package com.mortr.soloviev.mdc2018soloviev.ui.launcher;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.db.DBHelper;
import com.mortr.soloviev.mdc2018soloviev.db.DBUtils;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LauncherApplicationsAdapter extends RecyclerView.Adapter<LauncherApplicationsAdapter.Holder> {
    private static final String TAG = "LauncherAppsAdapter";
    @LayoutRes
    private int itemLayoutRes;
    @Nullable
    private RecyclerView recycler;
    private List<ResolveInfo> applicationInfos = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView appPackageName;

        Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickListener);

            itemView.setOnLongClickListener(onLongClickListener);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appPackageName = itemView.findViewById(R.id.app_package_name);

        }

        @SuppressWarnings("unused")
        void setApp(ResolveInfo app, int position) {
//            ((TextView) itemView).setText(position + "\n" + Integer.toHexString(app));
            PackageManager packageManager = itemView.getContext().getPackageManager();
            appName.setText(app.loadLabel(packageManager));
            appIcon.setImageDrawable(app.loadIcon(packageManager));
            if (appPackageName != null) {
                appPackageName.setText(app.activityInfo.packageName);
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recycler = recyclerView;
    }


    LauncherApplicationsAdapter() {
        this.itemLayoutRes = R.layout.application_list_item;
    }

    LauncherApplicationsAdapter(@LayoutRes int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;

    }

    void setNewAppList(List<ResolveInfo> app) {
        this.applicationInfos.clear();
        if (app != null) {
            this.applicationInfos.addAll(app);
        }
        notifyDataSetChanged();
        Log.v(TAG, "setNewAppList");
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent, false);
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
            if (recycler == null) {
                return;
            }
            int position = 0;
            RecyclerView.ViewHolder viewHolder = recycler.findContainingViewHolder(v);
            if (viewHolder != null) {
                position = viewHolder.getAdapterPosition();
            }
            final ResolveInfo appInfo = applicationInfos.get(position);
            final DBHelper dbHelper = new DBHelper(v.getContext());
            DBUtils.onStartApp(appInfo, v.getContext(), dbHelper.getWritableDatabase());
            dbHelper.close();
            launchApp(appInfo, recycler.getContext());
        }

    };

    private void launchApp(ResolveInfo appInfo, Context context) {
        Log.v(TAG, "launchApp " + appInfo);
        ActivityInfo activity = appInfo.activityInfo;

        ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                activity.name);
        Utils.sendYAPPMEvent(Utils.YAPPEventName.LAUNCH_APP_START, activity.name);
        Intent intent = new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        intent.setComponent(name);
        context.startActivity(intent);
    }

    final private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            if (recycler != null) {
                int position = 0;
                RecyclerView.ViewHolder viewHolder = recycler.findContainingViewHolder(v);
                if (viewHolder != null) {
                    position = viewHolder.getAdapterPosition();
                }
                final PopupMenu popup = createPopupMenu(v, position);
                popup.show();


            }
            return true;
        }
    };

    @NonNull
    private PopupMenu createPopupMenu(final View v, int position) {
        final ResolveInfo appInfo = applicationInfos.get(position);
        return Utils.createPopupMenu(v, appInfo);
    }

}


