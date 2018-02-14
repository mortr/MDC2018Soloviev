package com.mortr.soloviev.mdc2018soloviev;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import io.fabric.sdk.android.Fabric;


public class SoloAplication extends Application {

    public static final String YAPPM_KEY = "7fbb2c47-e769-4a99-b643-58dbae8b2dd5";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(YAPPM_KEY);
        //Задание необходимых параметров (например включение логирования)
        configBuilder.setLogEnabled();
        configBuilder.setTrackLocationEnabled(true);
        //Создание объекта расширенной конфигурации
        YandexMetricaConfig extendedConfig = configBuilder.build();
        // Инициализация AppMetrica SDK
        YandexMetrica.activate(getApplicationContext(), extendedConfig);
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this);


    }
}
