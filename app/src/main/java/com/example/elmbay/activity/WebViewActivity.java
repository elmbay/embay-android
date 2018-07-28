package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.elmbay.R;
import com.example.elmbay.fragment.WebViewFragment;
import com.example.elmbay.manager.AppManager;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class WebViewActivity extends BaseActivity {
    public static final String URL_KEY = "url";
    public static final String URL_ASSIGNMENT = "/elm/daily";
    public static final String URL_SETTING = "/elm/setup";
    public static final String URL_HELP = "/elm/index";
    public static final String URL_LOGIN = "/elm/login";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setup(getApplicationContext());

        setContentView(R.layout.activity_frame);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f = new WebViewFragment();
            String url = getIntent().getStringExtra(URL_KEY);
            if (url != null) {
                Bundle bundle = new Bundle();
                bundle.putString(URL_KEY, url);
                f.setArguments(bundle);
            }
            ft.add(R.id.activity_frame, f);
            ft.commit();
        }
    }

}
