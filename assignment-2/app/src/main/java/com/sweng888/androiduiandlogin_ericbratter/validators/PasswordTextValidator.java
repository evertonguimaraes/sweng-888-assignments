package com.sweng888.androiduiandlogin_ericbratter.validators;

import android.text.TextUtils;

public class PasswordTextValidator implements TextValidator {
    public static final int PASSWORD_LENGTH = 8;

    public PasswordTextValidator() {
        super();
    }

    @Override
    public boolean validate(String s) {
        return TextUtils.isEmpty(s) || s.length() < PASSWORD_LENGTH ? false : true;
    }
}
