package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.MainPagerActivity;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public class WelcomePagesActivity extends AppCompatActivity {

    public static final int WELCOME_PAGES_COUNT = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.isWhiteTheme(this) ? R.style.AppTheme_WhiteTheme : R.style.AppTheme_BlackTheme);
        setContentView(R.layout.activity_welcome_pages);

        Utils.sendYAPPMEvent(Utils.YAPPEventName.WELC_SCREANS_CREATE, "orientation " + Utils.getOrientation(this));

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragmentManager);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((WelcomePagesFragment) pagerAdapter.getItem(position)).onFrontPagerScreen();
                Log.d("WelcomeActivity",""+pagerAdapter.getItem(position).getTag()+pagerAdapter.getItem(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.nextButton).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(WelcomePagesActivity.this, MainPagerActivity.class);
            Utils.saveWelcomePageShowingState(WelcomePagesActivity.this, true);
            WelcomePagesActivity.this.startActivity(intent);
        }
    };

    public class ViewPagerAdapter extends FragmentStatePagerAdapter { //TODO move to package


        ViewPagerAdapter(@NonNull final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return WelcomeFragment.newInstance(position);
                }
                case 1: {
                    return DescriptionFragment.newInstance(position);
                }
                case 2: {
                    return ThemeChooserFragment.newInstance(position);
                }
                case 3: {
                    return LayoutSettingsFragment.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return WELCOME_PAGES_COUNT;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
//            WelcomePagesFragment fragment = null;// (WelcomePagesFragment) getSupportFragmentManager().getFragments().get(position);
//            return fragment != null ? fragment.getTitle() : "page "+position+1+ " from "+WELCOME_PAGES_COUNT;
            return "page " + (position + 1) + " from " + WELCOME_PAGES_COUNT;
        }
    }

}
