package com.mortr.soloviev.mdc2018soloviev.ui.profile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils;


public class InterestingInfoSaver extends Service {

    public static final String INTERESTING_INFO = "INTERESTING_INFO";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("InterestingSaver","onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        final Context context=getApplicationContext();
        Log.d("InterestingSaver","onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageUtils.saveProfileInterestingInfo(context, intent.getStringExtra(INTERESTING_INFO));
                Log.d("InterestingSaver",""+intent.getStringExtra(INTERESTING_INFO));
                sendBroadcast(new Intent(InterestingInfoSaveReceiver.INFO_WAS_SAVED_ACTION));
                stopSelf(startId);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
