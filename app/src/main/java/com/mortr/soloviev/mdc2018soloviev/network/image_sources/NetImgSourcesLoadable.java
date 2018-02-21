package com.mortr.soloviev.mdc2018soloviev.network.image_sources;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mortr.soloviev.mdc2018soloviev.network.ImageLoaderService;
import com.mortr.soloviev.mdc2018soloviev.network.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class NetImgSourcesLoadable implements ImageLoaderService.ImgSourceLoadable {

    private static final int DEFAULT_IMG_COUNT_NEEDED = 50;
    private List<String> imageUrls = new ArrayList<>();

    private int counter;


    @Nullable
    @Override
    public Bitmap loadImg(Context context) {
        if (imageUrls.isEmpty()) {
            counter = 0;
            List<String> urls = loadImageUrls(DEFAULT_IMG_COUNT_NEEDED);
            if (urls == null || urls.isEmpty()) {
                return null;
            }
            imageUrls.addAll(urls);
        }
        if (counter == imageUrls.size()) {
            counter = 0;
        }
        Log.d("NetSourcLoad","MainPager count "+counter+" size "+imageUrls.size());
        return NetworkUtils.loadBitmap(imageUrls.get(counter++));
    }

    @Override
    public void clearData() {
        imageUrls.clear();
    }

    /**
     *
     * @param countNeeded  desired element count
     * @return
     * note :  list size can be different from desired element count
     */
    @Nullable
    protected abstract List<String> loadImageUrls(int countNeeded);

}
