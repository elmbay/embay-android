package com.example.elmbay.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.elmbay.R;
import com.example.elmbay.activity.SignInActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.SessionData;
import com.example.elmbay.operation.SignInOperation;
import com.example.elmbay.operation.SignInRequest;
import com.example.elmbay.operation.SignInResponseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

/**
 * The class manages the sign in page
 *
 * Created by kgu on 4/12/18.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {
    private static final String EMAIL_PATTEN = ".+@.+\\..+";
    private static final String PHONE_START_PATTEN = "[+0-9]";
    private static final String PHONE_PATTEN = "[+0-9][0-9\\-]+[0-9]";

    EditText mUidView;
    EditText mPasswordView;
    EditText mConfirmPasswordView;
    View mSignInContainer;
    Button mSignInButton;
    Button mCreateAccountButton;
    View mSpinner;
    AlertDialog mDialog;
    String mUid;
    int mUidType;
    String mPassword;
    boolean mEnableButtons = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_sign_in, container, false);

        SessionData sessionData = AppManager.getInstance().getSessionData();

        if (TextUtils.isEmpty(sessionData.getUserToken())) {
            // There is no account or user wants to switch account
            mUidType = SessionData.UID_TYPE_UNKNOWN;
            mPassword = "";
        } else {
            mUid = sessionData.getUid();
            mUidType = sessionData.getUidType();
            mPassword = sessionData.getPassword();
        }

        mUidView = top.findViewById(R.id.uid);
        mUidView.setText(mUid);
        if (!TextUtils.isEmpty(mUid)) {
            mUidView.setInputType(mUidType == SessionData.UID_TYPE_PHONE ? TYPE_CLASS_PHONE : TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        mUidView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // This method is called to notify you that, within s, the count characters beginning at start
            // have just replaced old text that had length before.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mUidView.setInputType(TYPE_CLASS_TEXT);
                } else if (start == 0 && count == 1) {
                    String firstChar = Character.toString(s.charAt(0));
                    mUidType = firstChar.matches(PHONE_START_PATTEN) ? SessionData.UID_TYPE_PHONE : SessionData.UID_TYPE_EMAIL;
                    mUidView.setInputType(mUidType == SessionData.UID_TYPE_PHONE ? TYPE_CLASS_PHONE : TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableOrDisableButtons();
            }
        });

        mPasswordView = top.findViewById(R.id.password);
        mPasswordView.setText(mPassword);
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                enableOrDisableButtons();
            }
        });

        mConfirmPasswordView = top.findViewById(R.id.confirm_password);

        mSignInContainer = top.findViewById(R.id.signin_container);
        mSpinner = top.findViewById(R.id.spinner);

        mSignInButton = top.findViewById(R.id.sign_in);
        mSignInButton.setOnClickListener(this);

        mCreateAccountButton = top.findViewById(R.id.create_account);
        mCreateAccountButton.setOnClickListener(this);

        enableOrDisableButtons();

        top.findViewById(R.id.forget_password).setOnClickListener(this);

        return top;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_options, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    /**
     * react to the user tapping/selecting an options menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_log_out:
                mUidView.setText("");
                mUidView.setInputType(TYPE_CLASS_TEXT);
                mPasswordView.setText("");
                mConfirmPasswordView.setText("");
                mConfirmPasswordView.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
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
                mConfirmPasswordView.setText("");
                mConfirmPasswordView.setVisibility(View.GONE);
                loadData(false);
                break;

            case R.id.create_account:
                mConfirmPasswordView.setVisibility(View.VISIBLE);
                mConfirmPasswordView.requestFocus();
                loadData(true);
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
            onDataLoaded();
        } else {
            //TODO: show error
        }
    }

    private void loadData(boolean createAccount) {
        if (!validateSignIn(createAccount)) {
            return;
        }
        mSignInContainer.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);

        // Save the current sign in data
        SessionData sessionData = AppManager.getInstance().getSessionData();
        sessionData.setUid(mUid, mUidType);
        sessionData.setPassword(mPassword);

        SignInRequest request = new SignInRequest(mUid, mUid, mPassword);
        SignInOperation op = new SignInOperation(request);
        op.submit();
    }

    private void onDataLoaded() {
        mSpinner.setVisibility(View.GONE);
        ((SignInActivity) getActivity()).postSignedIn();
    }

    private void showDialog(int stringId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Material_Light_Dialog));
        builder.setMessage(stringId);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private boolean validateSignIn(boolean createAccount) {
        if (!isValidUid()) {
            showDialog(R.string.invalid_uid);
            return false;
        }

        if (!isValidPassword()) {
            showDialog(R.string.invalid_password);
            return false;
        }

        if (createAccount) {
            if (TextUtils.isEmpty(mConfirmPasswordView.getText())) {
//                showDialog(R.string.enter_confirmed_password);
                return false;
            }
            if (!mPassword.equals(mConfirmPasswordView.getText().toString())){
                showDialog(R.string.passwords_mismatch);
                return false;
            }
        }
        return true;
    }

    private boolean isValidUid() {
        mUid = null;
        if (!TextUtils.isEmpty(mUidView.getText())) {
            mUid = mUidView.getText().toString();
        }
        return mUid != null && (mUidType == SessionData.UID_TYPE_PHONE ? mUid.length() >= 6 : mUid.length() >= 8 && mUid.matches(EMAIL_PATTEN));
    }

    private boolean isValidPassword() {
        mPassword = null;
        if (!TextUtils.isEmpty(mPasswordView.getText())) {
            mPassword = mPasswordView.getText().toString();
        }
        return mPassword != null;
    }

    private void enableOrDisableButtons() {
        boolean enableButtons = isValidUid() && isValidPassword();
        if (enableButtons != mEnableButtons) {
            mEnableButtons = enableButtons;
            float alpha = (float) (mEnableButtons ? 1.0 : 0.2);
            mSignInButton.setEnabled(mEnableButtons);
            mSignInButton.setAlpha(alpha);
            mCreateAccountButton.setEnabled(mEnableButtons);
            mCreateAccountButton.setAlpha(alpha);
        }
    }
}
