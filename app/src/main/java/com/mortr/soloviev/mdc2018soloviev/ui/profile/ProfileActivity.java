package com.mortr.soloviev.mdc2018soloviev.ui.profile;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.StartActivity;
import com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.push.YandexMetricaPush;


public class ProfileActivity extends AppCompatActivity {

    private boolean isDeepLaunching;
    private View interestingContainer;
    private TextView interestingTV;
    private InterestingInfoSaveReceiver broadcastReceiver;

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
        interestingContainer = findViewById(R.id.prof_interesting_container);
        interestingTV = findViewById(R.id.prof_interesting_tv);

        Intent intent = getIntent();
        isDeepLaunching = handleDeeplink(intent);
        if (isDeepLaunching && savedInstanceState == null) {
            handlePayload(intent);
        }

        showInterestingInfoIfNeeded();

        broadcastReceiver = new InterestingInfoSaveReceiver();
        broadcastReceiver.setObserver(new InterestingInfoSaveReceiver.InterestingInfoSaveObserver() {
            @Override
            public void onSave() {
                showInterestingInfoIfNeeded();
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InterestingInfoSaveReceiver.INFO_WAS_SAVED_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    private void showInterestingInfoIfNeeded() {
        String interestingInfo = StorageUtils.getProfileInterestingInfo(this);
        if (interestingInfo != null && !TextUtils.isEmpty(interestingInfo)) {
            interestingContainer.setVisibility(View.VISIBLE);
            interestingTV.setText(interestingInfo);
        } else {
            interestingContainer.setVisibility(View.GONE);
        }
    }

    /**
     * @return true if deeplink can be extracted from open intent.
     */
    private boolean handleDeeplink(final Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && "mdc2018soloviev".equals(uri.getScheme())) {
            YandexMetrica.reportEvent("Open deeplink");
            return true;
        }
        return false;
    }

    /**
     * Deeplink push message can contain user defined payload. It can be extracted from intent
     * as {@code String} with {@link YandexMetricaPush#EXTRA_PAYLOAD} constant.
     */
    private void handlePayload(final Intent intent) {
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        if (!TextUtils.isEmpty(payload)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.profile_main_layout), payload, Snackbar.LENGTH_INDEFINITE);
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    Log.v("ProfileActivity", "Snackbar.Callback onDismissed ");
                }

                @Override
                public void onShown(Snackbar sb) {
                    super.onShown(sb);
                    Log.v("ProfileActivity", "Snackbar.Callback onShown");
                }
            });
            snackbar.show();
//            Toast.makeText(this, String.format("\nPayload: %s", payload), Toast.LENGTH_SHORT).show();
            YandexMetrica.reportEvent("Handle payload");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        YandexMetrica.reportAppOpen(this);
        isDeepLaunching = handleDeeplink(intent);
        if (isDeepLaunching) {
            handlePayload(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isDeepLaunching) {
                    startActivity(new Intent(this, StartActivity.class));
                }
                finish();
                Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_HOME, "");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isDeepLaunching) {
            startActivity(new Intent(this, StartActivity.class));
        }
        Utils.sendYAPPMEvent(Utils.YAPPEventName.PROFILE_BACK, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
