package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;

/**
 * Created by kgu on 4/16/18.
 */

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setup(getApplicationContext());

        setContentView(R.layout.activity_sign_in);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

    }

}
