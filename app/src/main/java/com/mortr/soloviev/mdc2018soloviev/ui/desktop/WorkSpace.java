package com.mortr.soloviev.mdc2018soloviev.ui.desktop;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.yandex.metrica.YandexMetrica;

public class WorkSpace extends ViewGroup implements ChooseAppReceiverable {

    private LayoutInflater layoutInflater;

    @Override
    public void chooseAppReceive(ComponentName componentName, float x, float y) {
        addItem(new DesktopItemModel(componentName), x, y);
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

    private void init() {
        layoutInflater = LayoutInflater.from(getContext());
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onShowPress(MotionEvent e) {
                createDialogAppChoose(e.getX(), e.getY());
            }
        };
        final GestureDetector gestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public void setAppChooseActivityLauncher(AppChooseActivityLauncher appChooseActivityLauncher) {
        this.appChooseActivityLauncher = appChooseActivityLauncher;
    }


    private void createDialogAppChoose(float x, float y) {
        if (appChooseActivityLauncher != null) {
            appChooseActivityLauncher.startAppChooseActivity(x, y);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        float childLeft = 0;
        float childTop = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            Pair<Float, Float> pair = (Pair<Float, Float>) child.getTag();
            childLeft = pair.first;
            childTop = pair.second;
            child.measure(0, 0);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();


                child.layout((int) childLeft, (int) childTop, (int) childLeft + childWidth, (int) childTop + child.getMeasuredHeight());

            }
        }


    }

    private void fillView(final View itemView, final ComponentName componentName) {

        itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }

        });
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView appIcon = itemView.findViewById(R.id.app_icon);
        ((TextView) itemView.findViewById(R.id.app_name)).setText(componentName.getClassName());
        TextView appPackageName = itemView.findViewById(R.id.app_package_name);
        PackageManager packageManager = itemView.getContext().getPackageManager();
        try {
            appIcon.setImageDrawable(packageManager.getActivityIcon(componentName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            YandexMetrica.reportError("Icon did not found " + componentName, e);
            appIcon.setBackgroundColor(Color.RED);
        }
        if (appPackageName != null) {
            appPackageName.setText(componentName.getPackageName());
        }

    }


    public void addItem(DesktopItemModel desktopItemModel, float x, float y) {

        final View newView = layoutInflater.inflate(R.layout.application_list_item, null, false);
        switch (desktopItemModel.getDesktopModelType()) {
            case APPLICATION_ACTIVITY: {
                fillView(newView, desktopItemModel.getComponentName());
                break;
            }
            case CUSTOM: {
                fillView(newView, new ComponentName(desktopItemModel.getName(), desktopItemModel.getDescription()));
                break;
            }
            case REFERENCES: {
                break;
            }
        }
        newView.setTag(new Pair<Float, Float>(x, y));
        addView(newView);

    }
}
