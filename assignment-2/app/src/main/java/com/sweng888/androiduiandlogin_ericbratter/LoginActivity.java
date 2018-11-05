package com.sweng888.androiduiandlogin_ericbratter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sweng888.androiduiandlogin_ericbratter.events.LoginUserEvent;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;
import com.sweng888.androiduiandlogin_ericbratter.utilities.Constants;
import com.sweng888.androiduiandlogin_ericbratter.validators.EmailTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.PasswordTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.TextValidationWatcher;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mUserId;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mSignup;
    private UserRepository repository;
    private TextValidationWatcher mUserIdValidatrionWatcher;
    private TextValidationWatcher mPasswordValidationWatcher;
    private Disposable loginUserEventSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Not sure I like having this here need to better setup MVC of MVVM
        repository = new UserRepository(this);

        // Setup User ID Field
        mUserId = findViewById(R.id.userId);
        mUserIdValidatrionWatcher =
                new TextValidationWatcher(
                        (TextInputLayout) findViewById(R.id.userIdTextInputLayout),
                        new EmailTextValidator(), R.string.invalid_email);
        mUserId.addTextChangedListener(mUserIdValidatrionWatcher);

        // Setup Password Field
        mPassword = findViewById(R.id.password);
        mPasswordValidationWatcher =
                new TextValidationWatcher(
                        (TextInputLayout) findViewById(R.id.passwordTextInputLayout),
                        new PasswordTextValidator(), R.string.invalid_password);
        mPassword.addTextChangedListener(mPasswordValidationWatcher);

        // Setup Login Button
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!LoginActivity.this.isValid()) return;
                String id = mUserId.getText().toString();
                String password = mPassword.getText().toString();
                LoginUserEvent event = LoginUserEvent.getInstance(repository);

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.authenticating));
                progressDialog.show();

                LoginActivity.this.loginUserEventSubscription = event.execute(id, password)
                        .subscribe(
                                LoginActivity.this.handleLoginEventOnSuccess(progressDialog),
                                LoginActivity.this.handleLoginEventOnError(progressDialog));
            }
        });

        // Setup Sign-up Link
        mSignup = (TextView) findViewById(R.id.signupTextView);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private Consumer handleLoginEventOnSuccess(final ProgressDialog progressDialog) {
        return new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                Log.d(LoginActivity.TAG, user.toString());
                progressDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                intent.putExtra(Constants.INTENT_PARAM_USER_ID, user.getEmail());
                startActivity(intent);
            }
        };
    }

    private Consumer handleLoginEventOnError(final ProgressDialog progressDialog) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.repository.destroy();

        if(this.loginUserEventSubscription != null && ! this.loginUserEventSubscription.isDisposed()) {
            this.loginUserEventSubscription.dispose();
        }
    }

    private boolean isValid(){
        boolean userIsValid = this.mUserIdValidatrionWatcher.validate(this.mUserId.getText().toString());
        boolean passwordIsValid = this.mPasswordValidationWatcher.validate(this.mPassword.getText().toString());

        return userIsValid && passwordIsValid;
    }
}
