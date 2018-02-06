package com.mortr.soloviev.mdc2018soloviev.db;

public class DBApplicationItem {
    private String packageName;
    private long dateInstalled;
    private boolean isSystem;
    private int startsCount;

    DBApplicationItem(String packageName, long dateInstalled, boolean isSystem, int startsCount) {
        this.packageName = packageName;
        this.dateInstalled = dateInstalled;
        this.isSystem = isSystem;
        this.startsCount = startsCount;
    }
    @SuppressWarnings("unused")
    public String getPackageName() {
        return packageName;
    }

    @SuppressWarnings("unused")
    public long getDateInstalled() {
        return dateInstalled;
    }
    @SuppressWarnings("unused")
    public boolean isSystem() {
        return isSystem;
    }
    @SuppressWarnings("unused")
    public int getStartsCount() {
        return startsCount;
    }
}
