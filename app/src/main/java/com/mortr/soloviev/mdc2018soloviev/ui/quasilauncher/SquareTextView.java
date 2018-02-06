package com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class SquareTextView extends android.support.v7.widget.AppCompatTextView {
    public SquareTextView(final Context context) {
        super(context);
    }

    public SquareTextView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
