package com.example.elmbay.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.elmbay.R;
import com.example.elmbay.fragment.SignInFragment;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.UserManager;

/**
 *
 * Created by kgu on 4/16/18.
 */

public class SignInActivity extends AppCompatActivity {
    public static final int REQUEST_SEND_SMS_PERMISSION = 100;
    public static final int REQUEST_SEND_SMS = 101;

    private static final String LOG_TAG = SignInActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setup(getApplicationContext());

        setContentView(R.layout.activity_frame);

        if (savedInstanceState == null) {
            UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
            if (AppManager.DEBUG) {
                Log.i(LOG_TAG, "token=" + userManager.getUserToken()
                        + " expire_at=" + userManager.getUserTokenExpirationTime() + " now=" + System.currentTimeMillis());
            }
            if (!TextUtils.isEmpty(userManager.getUserToken()) && userManager.getUserTokenExpirationTime() > System.currentTimeMillis()) {
                postSignedIn();
            } else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment f = new SignInFragment();
                ft.add(R.id.activity_frame, f);
                ft.commit();
            }
        }
    }

    @Override
    public void onBackPressed() { /* Disable back button */ }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (AppManager.DEBUG) {
            Log.i(LOG_TAG, "onRequestPermissionsResult " + requestCode);
        }
        if (requestCode == REQUEST_SEND_SMS_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.activity_frame);
            if (f != null) {
                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void postSignedIn() {
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent);

        // Don't come back to this activity anymore
        finish();
    }
}
