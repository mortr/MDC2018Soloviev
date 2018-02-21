package com.mortr.soloviev.mdc2018soloviev.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mortr.soloviev.mdc2018soloviev.network.image_sources.DefaultImgSourceLoadable;
import com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;


public class ImageLoaderService extends Service {

    public static final int IDLE_TIME = 60 * 2 * 1000;
    private volatile Date currentData;
    private Bitmap currentBitmap;
    private Set<ImageLoadingObserver> imageLoadingObservers = new HashSet<>();
    private Set<NextImageSelectObserver> nextImageLoadingObservers = new HashSet<>();
    private volatile int connectionCount;
    private volatile boolean isFinish;
    private volatile long timePeriod = 60 * 15 * 1000;
    Thread currentThread;
    private Thread timeCounterThread;

    public interface ImgSourceLoadable {
        @Nullable
        Bitmap loadImg(final Context context);

        void clearData();
    }

    public static final int BITMAPS_PRELOADED_SIZE = 1;

    final private LinkedBlockingDeque<Bitmap> bitmaps = new LinkedBlockingDeque<>();
    private ImgSourceLoadable defImgSourceLoadable = new DefaultImgSourceLoadable();
    private ImgSourceLoadable imgSourceLoadable = defImgSourceLoadable;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "MainPager service onCreate()");
        currentBitmap = StorageUtils.getBitmap(getApplicationContext());
        loadNextImage();
        isFinish = false;
        currentData = new Date();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Service", "MainPager onBind");
        isFinish = false;
        connectionCount++;
        return new ImageLoaderServiceBinder();
    }


    @Override
    public void onRebind(Intent intent) {
        Log.d("MainPager", "onRebind");
        connectionCount++;
        isFinish = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MainPager", "onUnbind");
        connectionCount--;
        if (connectionCount < 1) {
            startServiceFinishing();
        }
        return true;
    }

    private void startServiceFinishing() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ImageLoaderService.this.connectionCount < 1) {
                    Log.d("MainPager", "startServiceFinishing()" + connectionCount);
                    isFinish = true;
                    ImageLoaderService.this.stopSelf();
                }
            }
        }).start();

    }


    public void setTimePeriod(long timePeriod) {
        this.timePeriod = timePeriod;
        startTimeCounter();
    }

    private void startTimeCounter() {
        Log.d("MainPager", "startTimeCounter() 0");
        if (timeCounterThread != null) {
            timeCounterThread.interrupt();
        }
        if (isFinish) {
            return;
        }

        timeCounterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                Date newData = new Date();
                calendar.setTime(currentData);
                Log.d("MainPager", "startTimeCounter() 1 " + connectionCount);
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTime(newData);
                if (Thread.currentThread().isInterrupted() || isFinish) {
                    return;
                }
                if (calendar.get(Calendar.DAY_OF_MONTH) != newCalendar.get(Calendar.DAY_OF_MONTH) && newCalendar.get(Calendar.HOUR_OF_DAY) > 12) {
                    Log.d("MainPager", "startTimeCounter() 2");
                    imgSourceLoadable.clearData();
                    bitmaps.clear();
                    currentData = newData;
                }
                try {
                    Thread.currentThread().sleep(timePeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                if (Thread.currentThread().isInterrupted() || isFinish) {
                    return;
                }
                Log.d("Service", "MainPager startTimeCounter() 3  interapted " + Thread.currentThread().isInterrupted());
                loadNextImage();
            }
        });
        timeCounterThread.start();
    }


    private void loadNextImage() {
        Log.d("Service", "MainPager loadNextImage()");
        Bitmap bitmap = bitmaps.poll();
        bitmapPreload();
        if (bitmap != null) {
            StorageUtils.saveBitmap(getApplicationContext(), bitmap);
            currentBitmap = bitmap;
        }
        for (NextImageSelectObserver nextImageSelectObserver : nextImageLoadingObservers) {
            nextImageSelectObserver.onNextImageLoad();
        }
        startTimeCounter();
    }

    @Nullable
    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setNewSource(ImgSourceLoadable imgSourceLoadable) {//todo change to key for sourceLoadable create
        Log.d("Service", "MainPager loadNextImage()"+imgSourceLoadable.getClass().getCanonicalName()+this.imgSourceLoadable.getClass().getCanonicalName());
        if (!imgSourceLoadable.getClass().getCanonicalName().equals(this.imgSourceLoadable.getClass().getCanonicalName())) {
            this.imgSourceLoadable = imgSourceLoadable;
        }
    }

    public void addLoadObserver(ImageLoadingObserver imageLoadingObserver) {
        imageLoadingObservers.add(imageLoadingObserver);
        if (!bitmaps.isEmpty() || currentBitmap != null) {
            imageLoadingObserver.onImagesLoad();
        }
    }

    public void removeLoadObserver(ImageLoadingObserver imageLoadingObserver) {
        imageLoadingObservers.remove(imageLoadingObserver);
    }


    public void addNextImgLoadObserver(NextImageSelectObserver nextImageSelectObserver) {
        nextImageLoadingObservers.add(nextImageSelectObserver);
    }

    public void removeNextImgLoadObserver(NextImageSelectObserver nextImageSelectObserver) {
        nextImageLoadingObservers.remove(nextImageSelectObserver);
    }


    private void bitmapPreload() {

        if (currentThread == null || currentThread.getState() == Thread.State.TERMINATED) {
            Log.d("MainPager", "preload2");
            currentThread = new Thread(imageLoadRunnable);
            currentThread.start();
        }
    }


    public class ImageLoaderServiceBinder extends Binder {
        public ImageLoaderService getService() {
            return ImageLoaderService.this;
        }

    }


    private Runnable imageLoadRunnable = new Runnable() {
        @Override
        public void run() {
            if (Thread.currentThread().isInterrupted() || isFinish) {
                return;
            }
            while (bitmaps.size() < BITMAPS_PRELOADED_SIZE) {
                if (Thread.currentThread().isInterrupted() || isFinish) {
                    return;
                }
                Bitmap bitmap = imgSourceLoadable.loadImg(getApplicationContext());
                if (bitmap != null) {
                    bitmaps.push(bitmap);
                }

            }
            for (ImageLoadingObserver imageLoadingObserver : imageLoadingObservers) {
                imageLoadingObserver.onImagesLoad();
            }

        }
    };

}
