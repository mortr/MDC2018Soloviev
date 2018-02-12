package com.mortr.soloviev.mdc2018soloviev.ui.desktop;


import android.content.ComponentName;
import android.graphics.drawable.Drawable;

public class DesktopItemModel {
    enum ModelType {
        APPLICATION_ACTIVITY, CUSTOM, REFERENCES

    }

    public DesktopItemModel(String name) {
        this.name = name;
    }

    private ModelType modelType;
    private String description;
    private String name;
    private ComponentName componentName;
    private Drawable icon;

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public ComponentName getComponentInfo() {
        return componentName;
    }

    public ModelType getModelType() {
        return modelType;
    }

}
