package com.mortr.soloviev.mdc2018soloviev.ui.desktop;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WorkSpace extends ViewGroup {

    public interface AppChooseActivityLauncher {
        void startAppChooseActivity(float x, float y);
    }

    AppChooseActivityLauncher appChooseActivityLauncher;


    public WorkSpace(Context context) {
        super(context);
        init();
    }

    public WorkSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkSpace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WorkSpace(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createDialogAppChoose(v.getX(), v.getY());
                return true;
            }
        });
    }

    public void setAppChooseActivityLauncher(AppChooseActivityLauncher appChooseActivityLauncher) {
        this.appChooseActivityLauncher = appChooseActivityLauncher;
    }


    private void createDialogAppChoose(float x, float y) {
        if (appChooseActivityLauncher != null) {
            appChooseActivityLauncher.startAppChooseActivity(x,y);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childLeft = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            ((TextView) child).setMinHeight(60);
            ((TextView) child).setMinWidth(60);
            childLeft = (int) child.getTag();
            child.measure(0, 0);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();


                child.layout(childLeft, (int) child.getTag(), childLeft + childWidth, (int) child.getTag() + child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }


    }

    public void addItem(DesktopItemModel desktopItemModel) {
        TextView textView = new TextView(getContext());
        textView.setText(desktopItemModel.getName());
        textView.setTag(60);
        textView.setBackgroundColor(Color.RED);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addView(textView);

    }
}
