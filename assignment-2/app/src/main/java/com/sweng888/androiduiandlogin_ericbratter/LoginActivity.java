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
import com.sweng888.androiduiandlogin_ericbratter.validators.EmailTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.PasswordTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.TextValidationWatcher;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new UserRepository(this);

        // Setup User ID Field
        mUserId = findViewById(R.id.userId);
        mUserIdValidatrionWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.userIdTextInputLayout), new EmailTextValidator(), R.string.invalid_email);
        mUserId.addTextChangedListener(mUserIdValidatrionWatcher);

        // Setup Password Field
        mPassword = findViewById(R.id.password);
        mPasswordValidationWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.passwordTextInputLayout), new PasswordTextValidator(), R.string.invalid_password);
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

                event.execute(id, password)
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {
                                Log.d("MainActivity", user.toString());
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(LoginActivity.TAG, "ERROR HAPPENED");
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.repository.destroy();
    }

    private boolean isValid(){
        boolean userIsValid = this.mUserIdValidatrionWatcher.validate(this.mUserId.getText().toString());
        boolean passwordIsValid = this.mPasswordValidationWatcher.validate(this.mPassword.getText().toString());

        return userIsValid && passwordIsValid;
    }
}
