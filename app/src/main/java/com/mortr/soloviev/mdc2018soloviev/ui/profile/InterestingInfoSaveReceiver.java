package com.mortr.soloviev.mdc2018soloviev.ui.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;


public class InterestingInfoSaveReceiver extends BroadcastReceiver {
    public static final String INFO_WAS_SAVED_ACTION = "INFO_WAS_SAVED_ACTION";

    public interface InterestingInfoSaveObserver {
        void onSave();
    }

    @Nullable
    InterestingInfoSaveObserver interestingInfoSaveObserver;

    public void setObserver(@Nullable InterestingInfoSaveObserver interestingInfoSaveObserver) {
        this.interestingInfoSaveObserver = interestingInfoSaveObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (INFO_WAS_SAVED_ACTION.equals(intent.getAction()) && interestingInfoSaveObserver != null) {
            interestingInfoSaveObserver.onSave();
        }
    }
}
