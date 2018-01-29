package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.ui.launcher.LauncherActivity;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public class LayoutSettingsActivity extends AppCompatActivity {

    private RadioButton compactRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layouts_settings);
        final View nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(onClickListener);
        final RadioButton standardRadio = findViewById(R.id.standard_layout_radio);
        compactRadio = findViewById(R.id.compact_layout_radio);
        standardRadio.setChecked(true);
        standardRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactRadio.setChecked(false);
            }
        });
        compactRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardRadio.setChecked(false);
            }
        });
    }

    private void saveSettings() {
        Utils.saveLayoutSettings(this.getApplicationContext(), !compactRadio.isChecked());


    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutSettingsActivity.this.saveSettings();
            final Intent intent = new Intent(LayoutSettingsActivity.this, LauncherActivity.class);
            LayoutSettingsActivity.this.startActivity(intent);
        }
    };
}
