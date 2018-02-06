package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;


public class DescriptionFragment extends WelcomePagesFragment {
    public static final String KEY_PAGE_NUMBER = "KEY_PAGE_NUMBER";
    private String title;

    public static DescriptionFragment newInstance(int pagesNumber) {
        Bundle args = new Bundle();
        args.putString(KEY_PAGE_NUMBER, String.valueOf(pagesNumber + 1));
        DescriptionFragment fragment = new DescriptionFragment();
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
        title = getResources().getString(R.string.welcome_pages_description_title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_app_description_block_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
