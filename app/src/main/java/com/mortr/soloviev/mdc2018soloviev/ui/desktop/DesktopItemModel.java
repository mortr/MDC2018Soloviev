package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class DesktopItemModel {
    enum DesktopModelType {
        APPLICATION_ACTIVITY, CUSTOM, REFERENCES

    }

    public DesktopItemModel(String name) {
        this.desktopModelType = DesktopModelType.CUSTOM;
        this.description=name;
        this.name = name;
    }

    public DesktopItemModel(ResolveInfo info){
        this.desktopModelType = DesktopModelType.APPLICATION_ACTIVITY;
        this.componentName=new ComponentName(info.activityInfo.packageName,info.activityInfo.name);
    }
    public DesktopItemModel(ComponentName componentName){
        this.desktopModelType = DesktopModelType.APPLICATION_ACTIVITY;
        this.componentName=componentName;
    }
    private DesktopModelType desktopModelType;
    private String description;
    private String name;
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
