package com.sweng888.androiduiandlogin_ericbratter.validators;

public class PlainTextValidator implements TextValidator {
    public PlainTextValidator() {
        super();
    }

    @Override
    public boolean validate(String s) {
        return s == null || s.equals("") ? false : true;
    }
}
