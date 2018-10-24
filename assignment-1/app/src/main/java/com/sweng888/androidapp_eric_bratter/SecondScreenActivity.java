package com.sweng888.androidapp_eric_bratter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondScreenActivity extends AppCompatActivity {

    private TextView mTextViewWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mTextViewWelcome = findViewById(R.id.textViewWelcome);
        String welcomeText = mTextViewWelcome.getText().toString();

        Intent source = getIntent();

        StringBuilder builder = new StringBuilder();
        builder.append(welcomeText);
        builder.append(" ");
        builder.append(source.getStringExtra(Constants.INTENT_EXTRA_USER_NAME));
        mTextViewWelcome.setText(builder.toString());
    }
}
