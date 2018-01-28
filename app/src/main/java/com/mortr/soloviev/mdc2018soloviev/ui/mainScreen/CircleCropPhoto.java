package com.mortr.soloviev.mdc2018soloviev.ui.mainScreen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;

public class CircleCropPhoto extends View {

    public static final int RES_IS_ABSENT = -1;
    private Bitmap photoBitmap;
    private BitmapShader bitmapShader;
    private Matrix matrix = new Matrix();
    private Paint paint;

    public CircleCropPhoto(Context context) {
        super(context);
    }

    public CircleCropPhoto(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CircleCropPhoto(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleCropPhoto(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CircleCropPhoto, defStyleAttr, defStyleRes);
        int photoRes = a.getResourceId(R.styleable.CircleCropPhoto_photo, RES_IS_ABSENT);
        if (photoRes != RES_IS_ABSENT) {
            setPhoto(photoRes);
        }
        a.recycle();


    }


    public void setPhoto(@IdRes int photoRes) {

        photoBitmap = BitmapFactory.decodeResource(getResources(), photoRes);
        bitmapShader = new BitmapShader(photoBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        final float width = getWidth();
        final float height = getHeight();
        final float bWidth = photoBitmap.getWidth();
        final float bHeight = photoBitmap.getHeight();

        float dx = 0;
        float dy = 0;
        float scaleValue;
        if (bHeight > bWidth) {
            scaleValue = height / bHeight;
            dx = (width - bWidth * scaleValue) / 2;
        } else {
            scaleValue = width / bWidth;
            dy = (height - bHeight * scaleValue) / 2;
        }
        matrix.setScale(scaleValue, scaleValue);
        matrix.postTranslate(dx, dy);
        bitmapShader.setLocalMatrix(matrix);

        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2, paint);
    }
}
