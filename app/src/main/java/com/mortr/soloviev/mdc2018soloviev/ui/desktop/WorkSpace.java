package com.mortr.soloviev.mdc2018soloviev.ui.desktop;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher.SquareTextView;
import com.yandex.metrica.YandexMetrica;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.RED;

public class WorkSpace extends ViewGroup {

    private LayoutInflater layoutInflater;

    private Map<View, ComponentName> map = new HashMap<>();

    private AppChooseActivityLauncher appChooseActivityLauncher;

    private WorkspaceDeskAppManagerable workspaceDeskAppManagerable;
    private boolean isMoving;
    //    private int w;
//    private int h;


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
//        parentHeight = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
//        parentWidth =((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onShowPress(MotionEvent e) {
                createDialogAppChoose(e.getX(), e.getY());
            }


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                if (movingView!=null){
//                    movingView.setX(e2.getX());
//                    movingView.setY(e2.getY());
//
//                   return  true;
//                }
                return super.onScroll(e1, e2, distanceX, distanceY);
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

    public void setWorkspaceDeskAppManagerable(WorkspaceDeskAppManagerable workspaceDeskAppManagerable) {
        this.workspaceDeskAppManagerable = workspaceDeskAppManagerable;
    }

    private void createDialogAppChoose(float x, float y) {

        if (appChooseActivityLauncher != null) {
            final TextView archor = new SquareTextView(getContext());
            archor.setX(x);
            archor.setY(y);
            addView(archor);
            appChooseActivityLauncher.startAppChooseActivity(x, y, archor);
            removeView(archor);
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

            int parentHeight = getMeasuredHeight();
            int parentWidth = getMeasuredWidth();
            child.measure(0, 0);
            float childHeight = child.getMeasuredHeight();
            float childWidth = child.getMeasuredWidth();

            childLeft = pair.first - (childWidth / 2f);
            childTop = pair.second - (childHeight / 2f);
            childLeft = childLeft < 0 ? 0 : childLeft + childWidth > parentWidth ? parentWidth - childWidth : childLeft;
            childTop = childTop < 0 ? 0 : childTop + childHeight > parentHeight ? parentHeight - childHeight : childTop;

            if (child.getVisibility() != View.GONE) {


                child.layout((int) childLeft, (int) childTop, (int) (childLeft + childWidth), (int) (childTop + childHeight));

            }
        }


    }

    private void fillView(final View itemView, final ComponentName componentName) {

        itemView.setOnLongClickListener(onLongClickListener);

        itemView.setOnClickListener(onClickListener);
        itemView.setOnTouchListener(onItemTouch);
        PackageManager packageManager = itemView.getContext().getPackageManager();
        TextView appPackageName = itemView.findViewById(R.id.app_package_name);

        ImageView appIcon = itemView.findViewById(R.id.app_icon);
        TextView appName = (TextView) itemView.findViewById(R.id.app_name);
        String packageName = componentName.getPackageName();
        try {
            appName.setText(packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            YandexMetrica.reportError("Package did not found for name ", e);
            String[] name = TextUtils.split(componentName.getClassName(), ".");
            appName.setText(name[name.length - 1]);
        }

        try {
            appIcon.setImageDrawable(packageManager.getActivityIcon(componentName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            YandexMetrica.reportError("Icon did not found " + componentName, e);
            appIcon.setBackgroundColor(RED);
        }
        if (appPackageName != null) {
            appPackageName.setText(packageName);
        }

    }


    public void addItem(DesktopItemModel desktopItemModel, float x, float y) {

        final View newView = layoutInflater.inflate(R.layout.application_list_item, null, false);
        switch (desktopItemModel.getDesktopModelType()) {
            case APPLICATION_ACTIVITY: {
                fillView(newView, desktopItemModel.getComponentName());

                break;
            }
//            case CUSTOM: {
//                fillView(newView, new ComponentName(desktopItemModel.getName(), desktopItemModel.getDescription()));
//                break;
//            }
//            case REFERENCES: {
//                break;
//            }
        }
        map.put(newView, desktopItemModel.getComponentName());
        newView.setTag(new Pair<>(x, y));
        addView(newView);

    }


    private OnTouchListener onItemTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.setX(event.getRawX() - v.getMeasuredWidth() / 2);//TODO it is needed to add view bounds check
                v.setY(event.getRawY() - v.getMeasuredHeight() / 2);
                isMoving = true;
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isMoving) {
                    isMoving = false;
                    saveNewCoordinates(v);

                }
            }
            return false;
        }
    };

    private void saveNewCoordinates(View v) {
        if (workspaceDeskAppManagerable != null) {
            workspaceDeskAppManagerable.onDeskAppChangePlace(map.get(v), v);
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (workspaceDeskAppManagerable != null) {
                workspaceDeskAppManagerable.onDeskAppClick(map.get(v), v);
            }
        }
    };
    private OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (workspaceDeskAppManagerable != null) {
                workspaceDeskAppManagerable.onDeskAppLongClick(map.get(v), v);
            }
            return true;
        }

    };
}
