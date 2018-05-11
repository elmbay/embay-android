package com.example.elmbay.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.elmbay.R;
import com.example.elmbay.activity.SignInActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.PersistenceManager;
import com.example.elmbay.model.ProgressMark;
import com.example.elmbay.operation.SignInOperation;
import com.example.elmbay.operation.SignInRequest;
import com.example.elmbay.operation.SignInResponseEvent;
import com.example.elmbay.operation.SignInResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.example.elmbay.manager.PersistenceManager.HOUR_TO_MILLIS;

/**
 * Created by kgu on 4/12/18.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = SignInFragment.class.getName();
    private static final String EMAIL_PATTEN = ".+@.+";
    private static final String PHONE_PATTEN = "[0-9]";

    EditText mEmailView;
    EditText mPhoneView;
    EditText mPasswordView;
    View mSignInContainer;
    View mSpinner;
    AlertDialog mDialog;
    String mEmail;
    String mPhone;
    String mPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_sign_in, container, false);

        SharedPreferences persistenceStore = PersistenceManager.getShagetPersistenceStore(getActivity());
        mEmail = persistenceStore.getString(PersistenceManager.KEY_EMAIL, "");
        mPhone = persistenceStore.getString(PersistenceManager.KEY_PHONE, "");
        mPassword = persistenceStore.getString(PersistenceManager.KEY_PASSWORD, "");

        mEmailView = top.findViewById(R.id.email);
        mEmailView.setText(mEmail);

        mPhoneView = top.findViewById(R.id.phone);
        mPhoneView.setText(mPhone);

        mPasswordView = top.findViewById(R.id.password);
        mPasswordView.setText(mPassword);

        mSignInContainer = top.findViewById(R.id.signin_container);
        mSpinner = top.findViewById(R.id.spinner);

        top.findViewById(R.id.sign_in).setOnClickListener(this);
        top.findViewById(R.id.forget_password).setOnClickListener(this);

        return top;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        SharedPreferences persistenceStore = PersistenceManager.getShagetPersistenceStore(getActivity());
        long loginTokenExpirationTime = persistenceStore.getLong(PersistenceManager.KEY_USER_TOKEN_EXPIRATION_TIME, 0);
        if (loginTokenExpirationTime <= System.currentTimeMillis()) {
            loadData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

        mDialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                if (!isValidEmail() && !isValidPhone()) {
                    showDialog(R.string.invalid_user_id);
                }
                if (!isValidPassword()) {
                    showDialog(R.string.invalid_password);
                }
                loadData();
                break;

            case R.id.forget_password:
                //TODO: goto password recovery flow
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SignInResponseEvent event) {
        mSpinner.setVisibility(View.GONE);
        if (event == null || !event.hasError()) {
            processData();
        } else {
            //TODO: show error
        }
    }

    private void loadData() {
        mSignInContainer.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);

        // Save the current sign in data
        SharedPreferences persistenceStore = PersistenceManager.getShagetPersistenceStore(getActivity());
        PersistenceManager.setString(persistenceStore, PersistenceManager.KEY_EMAIL, mEmail);
        PersistenceManager.setString(persistenceStore, PersistenceManager.KEY_PHONE, mPhone);
        PersistenceManager.setString(persistenceStore, PersistenceManager.KEY_PASSWORD, mPassword);

        SignInRequest request = new SignInRequest(mEmail, mPhone, mPassword);
        SignInOperation op = new SignInOperation(request);
        op.submit();
    }

    private void processData() {
        mSpinner.setVisibility(View.GONE);
        try {
            SignInResult result = AppManager.getInstance().getSessionData().getSignInResult();
            long expirationTime = System.currentTimeMillis() + result.getUserTokenLifeInHours() * HOUR_TO_MILLIS;

            SharedPreferences persistenceStore = PersistenceManager.getShagetPersistenceStore(getActivity());
            PersistenceManager.setString(persistenceStore, PersistenceManager.KEY_USER_TOKEN, result.getUserToken());
            PersistenceManager.setLong(persistenceStore, PersistenceManager.KEY_USER_TOKEN_EXPIRATION_TIME, expirationTime);

            ProgressMark mark = result.getLowMark();
            if (mark != null)  {
                mark.toPersistenceStore(persistenceStore, false);
            }

            mark = result.getHighMark();
            if (mark != null) {
                mark.toPersistenceStore(persistenceStore, true);
            }

            ((SignInActivity) getActivity()).navigateToNextActivity();

        } catch (Exception e) {
            if (AppManager.DEBUG) {
                Log.w(LOG_TAG, "signin: " + e.getMessage());
            }
        }
    }

    private void showDialog(int stringId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(stringId);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private boolean isValidEmail() {
        mEmail = null;
        if (!TextUtils.isEmpty(mEmailView.getText())) {
            mEmail = mEmailView.getText().toString();
        }
        return mEmail != null && mEmail.matches(EMAIL_PATTEN);
    }

    private boolean isValidPhone() {
        mPhone = null;
        if (!TextUtils.isEmpty(mPhoneView.getText())) {
            mPhone = mPhoneView.getText().toString();
        }
        return mPhone != null && mPhone.matches(PHONE_PATTEN);
    }

    private boolean isValidPassword() {
        mPassword = null;
        if (!TextUtils.isEmpty(mPasswordView.getText())) {
            mPassword = mPasswordView.getText().toString();
        }
        return mPassword != null;
    }
}
