package com.sweng888.androiduiandlogin_ericbratter.validators;

import android.text.TextUtils;

public class EmailTextValidator implements TextValidator {
    public EmailTextValidator() {
        super();
    }

    @Override
    public boolean validate(String s) {
        return TextUtils.isEmpty(s) ? false : android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }
}
