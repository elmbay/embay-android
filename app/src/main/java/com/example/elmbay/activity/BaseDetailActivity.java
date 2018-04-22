package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Lesson;

/**
 * The base class to load Lesson and handle action bar setup and action
 *
 * Created by kgu on 4/18/18.
 */

public class BaseDetailActivity extends AppCompatActivity {
    public static final String LOG_TAG = BaseDetailActivity.class.getName();
    Lesson mLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLesson = AppManager.getInstance().getSessionData().getCurrentLesson();
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupActionBar() {
        if (mLesson != null && mLesson.getKeyword() != null) {
            setTitle(mLesson.getKeyword());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
