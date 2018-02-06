package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;


public class ThemeChooserFragment extends WelcomePagesFragment {
    public static final String KEY_PAGE_NUMBER = "KEY_PAGE_NUMBER";
    private String title;
    private RadioButton blackRadio;
    private RadioButton whiteRadio;


    public static ThemeChooserFragment newInstance(int pagesNumber) {
        Bundle args = new Bundle();
        args.putString(KEY_PAGE_NUMBER, String.valueOf(pagesNumber + 1));
        ThemeChooserFragment fragment = new ThemeChooserFragment();
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
        title = getResources().getString(R.string.welcome_pages_theme_chooser_title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_chooser_theme_block_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        whiteRadio = view.findViewById(R.id.white_theme_radio);
        blackRadio = view.findViewById(R.id.black_theme_radio);
        if (Utils.isWhiteTheme(view.getContext())) {
            whiteRadio.setChecked(true);
        } else {
            blackRadio.setChecked(true);
        }
        view.findViewById(R.id.white_theme_radio_frame).setOnClickListener(whiteRClickListener);
        view.findViewById(R.id.black_theme_radio_frame).setOnClickListener(blackRClickListener);

        whiteRadio.setOnClickListener(whiteRClickListener);

        blackRadio.setOnClickListener(blackRClickListener);

    }


    private void saveSettings() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        Utils.saveThemeAndActivityRestart(activity, !blackRadio.isChecked());

    }

    private View.OnClickListener whiteRClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            blackRadio.setChecked(false);
            whiteRadio.setChecked(true);
            saveSettings();
        }
    };
    private View.OnClickListener blackRClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            whiteRadio.setChecked(false);
            blackRadio.setChecked(true);
            saveSettings();
        }
    };
}
