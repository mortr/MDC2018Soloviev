package com.mortr.soloviev.mdc2018soloviev.ui.launcher;


import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LauncherIconsAdapter extends RecyclerView.Adapter<LauncherIconsAdapter.Holder> {
    @Nullable
    private RecyclerView recycler;

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickListener);

            itemView.setOnLongClickListener(onLongClickListener);
        }

        void setIcons(Integer icons, int position) {
            ((TextView) itemView).setText(position + "\n" + Integer.toHexString(icons));
            (itemView).setBackgroundColor(icons);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recycler = recyclerView;
    }

    private List<Integer> icons = new ArrayList<>();

    public LauncherIconsAdapter() {

    }


    public void setNewIconsList(List<Integer> icons) {
        this.icons.clear();
        this.icons.addAll(icons);
        notifyDataSetChanged();
    }

    public void setNewIcon(Integer icon, int insertPosition) {

        icons.add(insertPosition, icon);
        notifyItemInserted(insertPosition);
    }

    public void deleteIcon(int iconPosition) {
        icons.remove(iconPosition);
        notifyItemRemoved(iconPosition);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = new SquareTextView(parent.getContext());
        view.setOnClickListener(onClickListener);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setIcons(icons.get(position), position);

    }


    @Override
    public int getItemCount() {
        return icons.size();
    }

    final private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), ((TextView) v).getText() + String.valueOf(((ColorDrawable) v.getBackground()).getColor()), Toast.LENGTH_SHORT).show();
        }

    };

    final private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (recycler != null) {

                deleteIcon(recycler.findContainingViewHolder(v).getAdapterPosition());
            }
            return true;
        }
    };
}


