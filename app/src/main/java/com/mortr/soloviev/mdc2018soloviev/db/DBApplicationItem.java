package com.mortr.soloviev.mdc2018soloviev.db;

public class DBApplicationItem {
    private String packageName;
    private long dateInstalled;
    private boolean isSystem;
    private int startsCount;

    public DBApplicationItem(String packageName, long dateInstalled, boolean isSystem, int startsCount) {
        this.packageName = packageName;
        this.dateInstalled = dateInstalled;
        this.isSystem = isSystem;
        this.startsCount = startsCount;
    }

    public String getPackageName() {
        return packageName;
    }

    public long getDateInstalled() {
        return dateInstalled;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public int getStartsCount() {
        return startsCount;
    }
}
