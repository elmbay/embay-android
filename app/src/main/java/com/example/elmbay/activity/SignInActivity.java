package com.example.elmbay.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.elmbay.R;
import com.example.elmbay.fragment.SignInFragment;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.PersistenceManager;

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
            SharedPreferences persistenceStore = PersistenceManager.getShagetPersistenceStore(this);
            long loginTokenExpirationTime = persistenceStore.getLong(PersistenceManager.KEY_USER_TOKEN_EXPIRATION_TIME, 0);
            if (loginTokenExpirationTime > System.currentTimeMillis()) {
                navigateToNextActivity();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f = new SignInFragment();
            ft.add(R.id.activity_frame, f);
            ft.commit();
        }
    }

    public void navigateToNextActivity() {
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent);
    }
}
