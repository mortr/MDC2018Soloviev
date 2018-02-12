package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DesktopAdapter extends RecyclerView.Adapter<DesktopAdapter.Holder> {
    private static final String TAG = "LauncherAppsAdapter";
    @LayoutRes
    private int itemLayoutRes;
    @Nullable
    private RecyclerView recycler;
    private List<DesktopItemModel> itemModels = new ArrayList<>();

    class Holder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemName;
        TextView itemDescription;

        Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickListener);

            itemView.setOnLongClickListener(onLongClickListener);
            itemIcon = itemView.findViewById(R.id.app_icon);
            itemName = itemView.findViewById(R.id.app_name);
            itemDescription = itemView.findViewById(R.id.app_package_name);

        }

        void setDesktopItem(DesktopItemModel itemModel, int position) {
//            ((TextView) itemView).setText(position + "\n" + Integer.toHexString(app));
            itemName.setText(itemModel.getName());
            itemIcon.setImageDrawable(itemModel.getIcon());
            if (itemDescription != null) {
                itemDescription.setText(itemModel.getDescription());
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recycler = recyclerView;
    }


    DesktopAdapter() {
        this.itemLayoutRes = R.layout.application_list_item;
    }

    DesktopAdapter(@LayoutRes int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;

    }

    void setNewAppList(List<DesktopItemModel> app) {
        this.itemModels.clear();
        if (app != null) {
            this.itemModels.addAll(app);
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
        holder.setDesktopItem(itemModels.get(position), position);

    }


    @Override
    public int getItemCount() {
        return itemModels.size();
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
            DesktopItemModel desktopItem = itemModels.get(position);
            if (desktopItem.getModelType() == DesktopItemModel.ModelType.APPLICATION_ACTIVITY) {
                Utils.launchApp(desktopItem.getComponentInfo(), recycler.getContext());
            }
        }

    };

    final private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            if (recycler != null) {
                int position = 0;
                RecyclerView.ViewHolder viewHolder = recycler.findContainingViewHolder(v);
                if (viewHolder != null) {
                    position = viewHolder.getAdapterPosition();
                }

            }
            return true;
        }
    };


}


