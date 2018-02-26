package com.mortr.soloviev.mdc2018soloviev.ui.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yandex.metrica.push.YandexMetricaPush;


public class InterestingInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        Log.d("Interest",""+payload);
        Intent serviceIntent = new Intent(context, InterestingInfoSaver.class);
        serviceIntent.putExtra(InterestingInfoSaver.INTERESTING_INFO, payload);
        context.startService(serviceIntent);
    }
}
