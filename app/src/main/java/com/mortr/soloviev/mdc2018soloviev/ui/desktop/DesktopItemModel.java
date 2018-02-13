package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.mortr.soloviev.mdc2018soloviev.db.DBApplicationItem;

public class DesktopItemModel {
    enum DesktopModelType {
        APPLICATION_ACTIVITY, CUSTOM, REFERENCES

    }

    public DesktopItemModel(String name) {
        this.desktopModelType = DesktopModelType.CUSTOM;
        this.description = name;
        this.name = name;
    }

    public DesktopItemModel(ResolveInfo info) {
        this.desktopModelType = DesktopModelType.APPLICATION_ACTIVITY;
        this.componentName = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
    }


    public DesktopItemModel(DBApplicationItem item) {
        this(new ComponentName(item.getPackageName(), item.getActivityName()), item.getDesktopX(), item.getDesktopY());
        this.dateInstaling = item.getDateInstalled();
    }

    public DesktopItemModel(ComponentName componentName, float x, float y) {
        this.desktopModelType = DesktopModelType.APPLICATION_ACTIVITY;
        this.componentName = componentName;
        this.x = x;
        this.y = y;
    }

    private DesktopModelType desktopModelType;
    private String description;
    private String name;
    private float x;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public long getDateInstaling() {
        return dateInstaling;
    }

    private float y;
    private ComponentName componentName;
    private Drawable icon;
    private long dateInstaling;

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    public DesktopModelType getDesktopModelType() {
        return desktopModelType;
    }

}
