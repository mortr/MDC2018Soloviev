package com.mortr.soloviev.mdc2018soloviev.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;


public class SquareCard extends CardView {
    public SquareCard(Context context) {
        super(context);
    }

    public SquareCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
