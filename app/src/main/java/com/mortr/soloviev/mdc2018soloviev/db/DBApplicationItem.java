package com.mortr.soloviev.mdc2018soloviev.db;

public class DBApplicationItem {
    private final boolean isDesktop;
    private final float desktopX;
    private final float desktopY;
    private final String activityName;
    private String packageName;
    private long dateInstalled;
    private boolean isSystem;
    private int startsCount;

    DBApplicationItem(String packageName,String activityName, long dateInstalled, boolean isSystem, int startsCount, boolean isDesktop, float desktopX, float desktopY) {
        this.activityName = activityName;
        this.packageName = packageName;
        this.dateInstalled = dateInstalled;
        this.isSystem = isSystem;
        this.startsCount = startsCount;
        this.isDesktop = isDesktop;
        this.desktopX = desktopX;
        this.desktopY = desktopY;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isDesktop() {
        return isDesktop;
    }

    public float getDesktopX() {
        return desktopX;
    }

    public float getDesktopY() {
        return desktopY;
    }

    public long getDateInstalled() {
        return dateInstalled;
    }

    boolean isSystem() {
        return isSystem;
    }

    int getStartsCount() {
        return startsCount;
    }
}
