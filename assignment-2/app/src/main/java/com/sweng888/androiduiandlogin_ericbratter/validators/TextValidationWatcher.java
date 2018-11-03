package com.sweng888.androiduiandlogin_ericbratter.validators;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public class TextValidationWatcher implements TextWatcher {
    private TextInputLayout layout;
    private TextValidator validator;
    private int messageResourceId;

    public TextValidationWatcher(TextInputLayout layout, TextValidator validator, int messageResourceId){
        this.layout = layout;
        this.validator = validator;
        this.messageResourceId = messageResourceId;
    }

    public boolean validate(String s){
        boolean isValid = this.validator.validate(s);

        if(!isValid){
            this.layout.setError(this.layout.getContext().getString(this.messageResourceId));
            this.layout.setErrorEnabled(true);
        }else{
            this.layout.setError(null);
            this.layout.setErrorEnabled(false);
        }

        return isValid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.validate(s == null ? "" : s.toString());
    }
}
