package com.mortr.soloviev.mdc2018soloviev.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Utils.isWhiteTheme(this) ? R.style.AppTheme_WhiteTheme : R.style.AppTheme_BlackTheme);
        super.onCreate(savedInstanceState);
        Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_OPEN, "");
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = findViewById(R.id.profile_toolbar_id);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        findViewById(R.id.prof_additional_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_ADDITIONAL_MAIL, "");
            }
        });

        findViewById(R.id.prof_main_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_MAIN_MAIL, "");
            }
        });

        findViewById(R.id.prof_home_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_HOME_ADDRESS, "");
            }
        });

        findViewById(R.id.prof_mob_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_MOB_PHONE, "");
            }
        });

        findViewById(R.id.prof_git_hub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_GIT_HUB, "");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_HOME, "");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_BACK, "");
    }
}
