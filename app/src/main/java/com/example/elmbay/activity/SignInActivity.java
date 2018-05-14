package com.example.elmbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.elmbay.R;
import com.example.elmbay.fragment.SignInFragment;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.SessionData;

/**
 *
 * Created by kgu on 4/16/18.
 */

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setup(getApplicationContext());

        setContentView(R.layout.activity_frame);

        if (savedInstanceState == null) {
            SessionData sessionData = AppManager.getInstance().getSessionData();
            if (TextUtils.isEmpty(sessionData.getUserToken()) || sessionData.getUserTokenExpirationTime() <= System.currentTimeMillis()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment f = new SignInFragment();
                ft.add(R.id.activity_frame, f);
                ft.commit();
            }
            postSignedIn();
        }
    }

    public void postSignedIn() {
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent);
    }
}
