package com.sweng888.androidapp_eric_bratter;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private Button mBtnPassParams;
    private Button mBtnCreateNewAccount;
    private FloatingActionButton mfabEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextUserName = (EditText) findViewById(R.id.editTextUserName);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mBtnPassParams = (Button) findViewById(R.id.btn_pass_params);
        mBtnPassParams.setOnLongClickListener(this);
        mBtnCreateNewAccount = (Button) findViewById(R.id.btnCreateNewAccount);
        mfabEmail = (FloatingActionButton) findViewById(R.id.fabEmail);
        mfabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Implicit Intent to call the email app.
                // In this case, we are using the Intent-filter "ACTION_SEND" for allowing this feature
                Intent intent = new Intent(Intent.ACTION_SEND);

                // Set up the text type to be sent
                intent.setType("text/html");

                // Add extra information for the intent
                String[] toAddress = new String[1];
                toAddress[0] = Constants.SUPPORT_EMAIL_ADDRESS;

                intent.putExtra(Intent.EXTRA_EMAIL, toAddress);
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_support_email_subject));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_support_email_default_text));

                startActivity(Intent.createChooser(intent, getString(R.string.text_support_email_no_client)));
            }
        });
    }

    private boolean inputIsProvided() {
        return mEditTextUserName.getText().toString().length() > 0 && mEditTextPassword.getText().toString().length() > 0 ? true : false;
    }

    private void navigateToWelcomeActivity() {
        Intent intent = new Intent(MainActivity.this, SecondScreenActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_USER_NAME, mEditTextUserName.getText().toString());
        startActivity(intent);
    }

    public void onSimpleClick(View v) {
        Toast.makeText(getApplicationContext(), R.string.toast_simple_click_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pass_params:
                // If this was for real we would want to probably authenticate the user first
                if (inputIsProvided()) {
                    navigateToWelcomeActivity();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.toast_enter_required_data, Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pass_params:
                Toast.makeText(getApplicationContext(), R.string.toast_glad_you_are_here, Toast.LENGTH_SHORT).show();
        }
    }
}
