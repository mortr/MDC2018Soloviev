package com.mortr.soloviev.mdc2018soloviev.network.image_sources;

import android.content.Context;
import android.graphics.Bitmap;

import com.mortr.soloviev.mdc2018soloviev.network.ImageLoaderService;
import com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils;


public class DefaultImgSourceLadable implements ImageLoaderService.ImgSourceLoadable {
    public Bitmap loadImg(final Context context) {
        return StorageUtils.getBitmap(context);
    }
}
