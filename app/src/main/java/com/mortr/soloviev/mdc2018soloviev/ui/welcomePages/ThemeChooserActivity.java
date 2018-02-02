package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public class ThemeChooserActivity extends AppCompatActivity {

    private RadioButton blackRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser_theme);
        final View nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(onClickListener);
        final RadioButton whiteRadio = findViewById(R.id.white_theme_radio);
        blackRadio = findViewById(R.id.black_theme_radio);
        if (Utils.isWhiteTheme(this)) {
            whiteRadio.setChecked(true);
        } else {
            blackRadio.setChecked(true);
        }

        whiteRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackRadio.setChecked(false);
                saveSettings();
            }
        });
        blackRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteRadio.setChecked(false);
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        Utils.applyTheme(this, !blackRadio.isChecked());


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(ThemeChooserActivity.this, LayoutSettingsActivity.class);
            ThemeChooserActivity.this.startActivity(intent);
        }
    };
}
