package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.R;


public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        final View nextButton=findViewById(R.id.nextButton);
        nextButton.setOnClickListener(onClickListener);
    }


    private void saveSettings(){

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveSettings();
            final Intent intent=new Intent(DescriptionActivity.this,ThemeChooserActivity.class);
            DescriptionActivity.this.startActivity(intent);
        }
    };


}