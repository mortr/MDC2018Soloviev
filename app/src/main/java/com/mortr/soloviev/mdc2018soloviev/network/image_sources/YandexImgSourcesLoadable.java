package com.mortr.soloviev.mdc2018soloviev.network.image_sources;


import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class YandexImgSourcesLoadable extends NetImgSourcesLoadable {


    private static final String TAG_NAME_IMG_REF = "img";
    private static final String ATTRIBUT_SIZE_NAME = "size";
    private static final String ATTRIBUT_HREF_NAME = "href";



    @Nullable
    private String getNextUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && TAG_NAME_IMG_REF.equals(parser.getName())) {
                String ref = null;
                boolean needDimension = false;
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    String attributeName = parser.getAttributeName(i);
                    if (ATTRIBUT_HREF_NAME.equals(attributeName)) {
                        ref = parser.getAttributeValue(i);
                        if (needDimension) {
                            return ref;
                        }
                    } else if (ATTRIBUT_SIZE_NAME.equals(attributeName) && "XXL".equals(parser.getAttributeValue(i))) {
                        needDimension = true;
                        if (ref != null) {
                            return ref;
                        }
                    }
                }
            }
        }

        return null;
    }


    @Override
    protected List<String> loadImageUrls(int countNeeded) {
        try {
            List<String> imageUrls = new ArrayList<>();
            if (countNeeded > 100) {
                countNeeded = 100;
            }
            final String stringUrl = "http://api-fotki.yandex.ru/api/podhistory/?limit=" + countNeeded;
            final URL url = new URL(stringUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final InputStream stream = connection.getInputStream();
                final XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);
                String imgUrl;
                while ((imgUrl = getNextUrl(parser)) != null) {
                    imageUrls.add(imgUrl);
                }
            }
            return imageUrls;
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();

        }
        return null;
    }



}
