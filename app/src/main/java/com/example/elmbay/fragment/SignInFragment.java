package com.example.elmbay.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.elmbay.manager.SessionData;
import com.example.elmbay.operation.SignInOperation;
import com.example.elmbay.operation.SignInRequest;
import com.example.elmbay.operation.SignInResponseEvent;
import com.example.elmbay.operation.SignInResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by kgu on 4/12/18.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = SignInFragment.class.getName();
    private static final String EMAIL_PATTEN = ".+@.+[.com|.net|.org]";
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

        SessionData sessionData = AppManager.getInstance().getSessionData();
        mEmail = sessionData.getEmail();
        mPhone = sessionData.getPhone();
        mPassword = sessionData.getPassword();

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

        SessionData sessionData = AppManager.getInstance().getSessionData();
        if (sessionData.getUserTokenExpirationTime() <= System.currentTimeMillis()) {
            loadData();
        } else {
            ((SignInActivity) getActivity()).postSignedIn();
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
        if (!isValidEmail() && !isValidPhone()) {
            showDialog(R.string.invalid_user_id);
        }

        if (!isValidPassword()) {
            showDialog(R.string.invalid_password);
        }

        mSignInContainer.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);

        // Save the current sign in data
        SessionData sessionData = AppManager.getInstance().getSessionData();
        sessionData.setEmail(mEmail);
        sessionData.setPhone(mPhone);
        sessionData.setPassword(mPassword);

        SignInRequest request = new SignInRequest(mEmail, mPhone, mPassword);
        SignInOperation op = new SignInOperation(request);
        op.submit();
    }

    private void processData() {
        mSpinner.setVisibility(View.GONE);
        try {
            SignInResult result = AppManager.getInstance().getSessionData().getSignInResult();
            SessionData sessionData = AppManager.getInstance().getSessionData();
            sessionData.setUserToken(result.getUserToken());
            sessionData.setUserTokenExpirationTime(System.currentTimeMillis() + result.getUserTokenLifeInHours() * SessionData.HOUR_TO_MILLIS);
            sessionData.setHighMark(result.getHighMark());

            ((SignInActivity) getActivity()).postSignedIn();

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
