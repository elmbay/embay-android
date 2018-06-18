package com.example.elmbay.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;

/**
 * The base class to load Lesson and handle action bar setup and action
 *
 * Created by kgu on 4/18/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_item_log_out:
                showConfirmationDialog();
                return true;

            case R.id.menu_item_settings:
                showSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    private void doLogOut() {
        AppManager.getInstance().getSessionData().logout();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

        // Don't come back to this activity anymore
        finish();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Material_Light_Dialog));

        builder.setMessage(R.string.confirm_log_out)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            doLogOut();
                        }
                    })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }
}
