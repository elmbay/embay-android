package com.example.elmbay.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.elmbay.R;
import com.example.elmbay.activity.SignInActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.UserManager;
import com.example.elmbay.operation.SignInOperation;
import com.example.elmbay.operation.SignInRequest;
import com.example.elmbay.operation.SignInResponseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
import static com.example.elmbay.activity.SignInActivity.REQUEST_SEND_SMS;

/**
 * The class manages the sign in page
 *
 * Created by kgu on 4/12/18.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {
    private static String [] permissions = { Manifest.permission.SEND_SMS };

    private static final String EMAIL_PATTEN = ".+@.+\\..+";
    private static final String PHONE_START_PATTEN = "[+0-9]";

    private static final int SIGNIN_STATE_NONE = 0;
    private static final int SIGNIN_STATE_CONFIRM_PASSWORD = 1;
    private static final int SIGNIN_STATE_VERIFY_CODE = 2;
    private static final int SIGNIN_STATE_LOAD_DATA = 3;

    private static final String LOG_TAG = SignInFragment.class.getName();
    EditText mUidView;
    EditText mPasswordView;
    EditText mConfirmPasswordView;
    View mSignInContainer;
    Button mSignInButton;
    Button mCreateAccountButton;
    View mSpinner;
    AlertDialog mDialog;
    AlertDialog mVerifyCodeDialog;
    EditText mCodeInput;

    String mUid;
    String mUidType;
    String mPassword;
    boolean mEnableButtons = true;
    int mSignInState = SIGNIN_STATE_NONE;
    int mVerificationCode;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mUidView = top.findViewById(R.id.uid);
        mUidView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // This method is called to notify you that, within s, the count characters beginning at start
            // have just replaced old text that had length before.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mUidView.setInputType(TYPE_CLASS_PHONE);
                } else if (start == 0 && count == 1) {
                    String firstChar = Character.toString(s.charAt(0));
                    mUidType = UserManager.UID_TYPE_PHONE;
                    mUidView.setInputType(UserManager.UID_TYPE_PHONE.equals(mUidType) ? TYPE_CLASS_PHONE : TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableOrDisableButtons();
            }
        });

        mPasswordView = top.findViewById(R.id.password);
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
        mConfirmPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mCreateAccountButton.setText(R.string.send_code);
                mCreateAccountButton.setEnabled(true);
                mCreateAccountButton.setAlpha((float) 1.0);
            }
        });

        mSignInContainer = top.findViewById(R.id.signin_container);
        mSpinner = top.findViewById(R.id.spinner);
        mSignInButton = top.findViewById(R.id.sign_in);
        mSignInButton.setOnClickListener(this);
        mCreateAccountButton = top.findViewById(R.id.create_account);
        mCreateAccountButton.setOnClickListener(this);
        top.findViewById(R.id.forget_password).setOnClickListener(this);

        showHintPage();

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog = null;
        mVerifyCodeDialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                mSignInState = SIGNIN_STATE_NONE;
                mConfirmPasswordView.setText("");
                mConfirmPasswordView.setVisibility(View.GONE);
                loadData(false);
                break;

            case R.id.create_account:
                onCreateAccount();
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
            showDialog(R.string.unknown_error);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SignInActivity.REQUEST_SEND_SMS_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mVerificationCode = 1000 + (int) (System.currentTimeMillis() % 997);
            String message = getContext().getString(R.string.sms_code, mVerificationCode);

            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), REQUEST_SEND_SMS, new Intent("SMS_SENT"), 0);
            if (AppManager.DEBUG) {
                Log.i(LOG_TAG, "sendTextMessage " + mUid + ": " + message);
            }
            smsManager.sendTextMessage(mUid, null, message, pendingIntent, null);
            showEnterCodePage();
        }
    }

    private void onCreateAccount() {
        switch (mSignInState) {
            case SIGNIN_STATE_NONE: {
                mSignInState = SIGNIN_STATE_CONFIRM_PASSWORD;
                mSignInButton.setEnabled(false);
                mSignInButton.setAlpha((float) 0.2);
                mCreateAccountButton.setEnabled(false);
                mCreateAccountButton.setAlpha((float) 0.2);
                mConfirmPasswordView.setVisibility(View.VISIBLE);
                Editable password = mPasswordView.getText();
                Editable password2 = mConfirmPasswordView.getText();
                if (TextUtils.isEmpty(password)) {
                    mPasswordView.requestFocus();
                } else if (TextUtils.isEmpty(password2)) {
                    mConfirmPasswordView.requestFocus();
                }
                break;
            }

            case SIGNIN_STATE_CONFIRM_PASSWORD:
                if (validateSignIn(true)) {
                    if (mVerificationCode == 0) {
                        ActivityCompat.requestPermissions(getActivity(),  permissions, SignInActivity.REQUEST_SEND_SMS_PERMISSION);
                    } else {
                        showEnterCodePage();
                    }
                }
                break;

            case SIGNIN_STATE_VERIFY_CODE:
                showEnterCodePage();
                break;
        }
    }

    private void loadData(boolean createAccount) {
        if (!validateSignIn(createAccount)) {
            return;
        }
        mSignInState = SIGNIN_STATE_LOAD_DATA;
        mSignInContainer.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);

        // Save the current sign in data
        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        userManager.setUid(mUid, mUidType);
        userManager.setPassword(mPassword);

        SignInRequest request = new SignInRequest(mUid, mUidType, mPassword, createAccount);
        SignInOperation op = new SignInOperation(request);
        op.submit();
    }

    private void onDataLoaded() {
        mSpinner.setVisibility(View.GONE);
        ((SignInActivity) getActivity()).postSignedIn();
    }

    private void showHintPage() {
        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        if (TextUtils.isEmpty(userManager.getUserToken())) {
            // Either there is no account or user wants to log out
            mUidType = UserManager.UID_TYPE_UNKNOWN;
            mPassword = "";
        } else {
            mUid = userManager.getUid();
            mUidType = userManager.getUidType();
            mPassword = userManager.getPassword();
        }

        mUidView.setText(mUid);
        if (!TextUtils.isEmpty(mUid)) {
            mUidView.setInputType(UserManager.UID_TYPE_PHONE.equals(mUidType) ? TYPE_CLASS_PHONE : TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        mPasswordView.setText(mPassword);
        enableOrDisableButtons();
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
            if (TextUtils.isEmpty(mConfirmPasswordView.getText()) || !mPassword.equals(mConfirmPasswordView.getText().toString())){
                showDialog(R.string.passwords_mismatch);
                return false;
            }
        }
        return true;
    }

    private void validateCode() {
        if (!TextUtils.isEmpty(mCodeInput.getText())) {
            if (Integer.valueOf(mCodeInput.getText().toString()) == mVerificationCode) {
                loadData(true);
            } else {
                showDialog(R.string.invalid_code);
            }
        }
    }

    private boolean isValidUid() {
        mUid = null;
        if (!TextUtils.isEmpty(mUidView.getText())) {
            mUid = mUidView.getText().toString();
        }
        return mUid != null && (UserManager.UID_TYPE_PHONE.equals(mUidType) ? mUid.length() >= 10 : mUid.length() >= 8 && mUid.matches(EMAIL_PATTEN));
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

    private void showEnterCodePage() {
        mSignInState = SIGNIN_STATE_VERIFY_CODE;
        if (mVerifyCodeDialog == null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(80);
            lp.setMarginEnd(80);
            mCodeInput = new EditText(getActivity());
            mCodeInput.setSingleLine();
            mCodeInput.setLayoutParams(lp);
            FrameLayout dialogInputContainer = new FrameLayout(getActivity());
            dialogInputContainer.addView(mCodeInput);

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Material_Light_Dialog));
            builder.setView(dialogInputContainer)
                    .setMessage(R.string.enter_code)
                    .setPositiveButton(R.string.verify, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            validateCode();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mSignInState = SIGNIN_STATE_CONFIRM_PASSWORD;
                        }
                    });

            mVerifyCodeDialog = builder.create();
        }
        mVerifyCodeDialog.show();
    }

    private void showDialog(int stringId) {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Material_Light_Dialog));
            builder.setMessage(stringId)
                    .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (mSignInState == SIGNIN_STATE_VERIFY_CODE) {
                                showEnterCodePage();
                            }
                        }
//                    })
//                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            getActivity().onBackPressed();
//                        }
                    });
            mDialog = builder.create();
        } else {
            mDialog.setMessage(getContext().getString(stringId));
        }
        mDialog.show();
    }
}
