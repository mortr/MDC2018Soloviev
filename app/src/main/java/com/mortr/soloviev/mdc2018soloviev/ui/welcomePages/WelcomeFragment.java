package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mortr.soloviev.mdc2018soloviev.R;


public class WelcomeFragment extends WelcomePagesFragment {
    public static final String KEY_PAGE_NUMBER = "KEY_PAGE_NUMBER";
    private String title;

    public static WelcomeFragment newInstance(int pagesNumber) {
        Bundle args = new Bundle();
        args.putString(KEY_PAGE_NUMBER, String.valueOf(pagesNumber + 1));
        WelcomeFragment fragment = new WelcomeFragment();
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
        title = getResources().getString(R.string.welcome_pages_welcome_title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_welcome_block_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
