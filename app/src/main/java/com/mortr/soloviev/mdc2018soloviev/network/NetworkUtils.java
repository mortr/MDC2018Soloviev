package com.mortr.soloviev.mdc2018soloviev.network;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtils {

    @Nullable
   public static Bitmap loadBitmap(String url) {
        try {
            URLConnection urlConnection = (new URL(url)).openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte [] bitmap = buffer.toByteArray();
            return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
