package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

public class LayoutSettingsFragment extends WelcomePagesFragment {
    public static final String KEY_PAGE_NUMBER = "KEY_PAGE_NUMBER";
    private String title;
    private RadioButton compactRadio;

    private RadioButton standardRadio;

    public static LayoutSettingsFragment newInstance(int pagesNumber) {
        Bundle args = new Bundle();
        args.putString(KEY_PAGE_NUMBER, String.valueOf(pagesNumber + 1));
        LayoutSettingsFragment fragment = new LayoutSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(KEY_PAGE_NUMBER, "*");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = getResources().getString(R.string.welcome_pages_layout_settings_title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_choose_layout_settings_block_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        standardRadio = view.findViewById(R.id.standard_layout_radio);
        compactRadio = view.findViewById(R.id.compact_layout_radio);
        standardRadio.setChecked(Utils.isStandardLayoutsWasSaved(view.getContext()));
        compactRadio.setChecked(!Utils.isStandardLayoutsWasSaved(view.getContext()));
        view.findViewById(R.id.standard_layout_frame).setOnClickListener(standardClickListener);
        view.findViewById(R.id.compact_layout_frame).setOnClickListener(compactClickListener);

        standardRadio.setOnClickListener(standardClickListener);

        compactRadio.setOnClickListener(compactClickListener);


    }

    private void saveSettings(Context context) {
        Utils.saveLayoutSettings(context.getApplicationContext(), !compactRadio.isChecked());

    }

    private View.OnClickListener compactClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            standardRadio.setChecked(false);
            compactRadio.setChecked(true);
            saveSettings(v.getContext());
        }
    };
    private View.OnClickListener standardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            compactRadio.setChecked(false);
            standardRadio.setChecked(true);
            saveSettings(v.getContext());
        }
    };
}
