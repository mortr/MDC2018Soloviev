package com.mortr.soloviev.mdc2018soloviev.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mortr.soloviev.mdc2018soloviev.network.image_sources.DefaultImgSourceLadable;
import com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils;

import java.util.concurrent.LinkedBlockingDeque;


public class ImageLoaderService extends Service {


    private boolean isCashing;

    public interface ImgSourceLoadable {
        @Nullable
        Bitmap loadImg(final Context context);
    }

    public static final int BITMAP_CACHE_SIZE = 1;

    LinkedBlockingDeque<Bitmap> bitmaps = new LinkedBlockingDeque<>(BITMAP_CACHE_SIZE);
    ImgSourceLoadable imgSourceLoadable = new DefaultImgSourceLadable();


    @Override
    public void onCreate() {
        super.onCreate();
        enableBitmapCaching(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ImageLoaderServiceBinder();
    }


    public int getBitmapCacheSize() {
        return bitmaps.size();
    }

    public int getMaxBitmapCacheSize() {
        return BITMAP_CACHE_SIZE;
    }

    public Bitmap getImage() {
        Bitmap bitmap = bitmaps.poll();
        if (isCashing) {
            thread.start();
        }
        if (bitmap != null) {
            StorageUtils.saveBitmap(getApplicationContext(), bitmap);
        }
        return bitmap;
    }

    public void setNewSource(ImgSourceLoadable imgSourceLoadable) {
        this.imgSourceLoadable = imgSourceLoadable;
    }

    private void enableBitmapCaching(boolean isCashing) {
        this.isCashing = isCashing;
    }


    public class ImageLoaderServiceBinder extends Binder {
        public ImageLoaderService getService() {
            return ImageLoaderService.this;
        }

    }

    void onCacheLoad(){

    }

    private Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (bitmaps.size() < BITMAP_CACHE_SIZE) {
                if (!isCashing) {
                    break;
                }
                Bitmap bitmap = imgSourceLoadable.loadImg(getApplicationContext());
                if (bitmap != null) {
                    bitmaps.push(bitmap);
                }

            }

        }
    };

}
