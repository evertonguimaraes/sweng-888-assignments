package com.sweng888.androiduiandlogin_ericbratter;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sweng888.androiduiandlogin_ericbratter.events.CreateUserEvent;
import com.sweng888.androiduiandlogin_ericbratter.exception.RepositoryException;
import com.sweng888.androiduiandlogin_ericbratter.exception.RepositoryExceptionCode;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;
import com.sweng888.androiduiandlogin_ericbratter.validators.EmailTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.PasswordTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.PlainTextValidator;
import com.sweng888.androiduiandlogin_ericbratter.validators.TextValidationWatcher;
import com.sweng888.androiduiandlogin_ericbratter.validators.TextValidator;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private UserRepository repository;
    private Disposable createUserEventSubscription;
    private TextValidationWatcher mEmailValidatrionWatcher,
            mPasswordValidationWatcher,
            mFirstNameValidationWatcher,
            mLastNameValidationWatcher,
            mBirthdayValidationWatcher;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    // Trying out Butter knife annotations
    @BindView(R.id.firstName)
    EditText mFirstName;
    @BindView(R.id.lastName)
    EditText mLastName;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.birthday)
    EditText mBirthday;
    @BindView(R.id.mobilePhone)
    EditText mMobilePhone;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.emailTextInputLayout)
    TextInputLayout mEmailInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        repository = new UserRepository(this);

        mEmailValidatrionWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.emailTextInputLayout), new EmailTextValidator(), R.string.invalid_email);
        this.mEmail.addTextChangedListener(mEmailValidatrionWatcher);

        mPasswordValidationWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.passwordTextInputLayout), new PasswordTextValidator(), R.string.invalid_password);
        mPassword.addTextChangedListener(mPasswordValidationWatcher);

        mFirstNameValidationWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.firstNameTextInputLayout), new PlainTextValidator(), R.string.required);
        mFirstName.addTextChangedListener(mFirstNameValidationWatcher);

        mLastNameValidationWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.lastNameTextInputLayout), new PlainTextValidator(), R.string.required);
        mLastName.addTextChangedListener(mLastNameValidationWatcher);

        // As we force entry via the date picker only need to validate we have a value
        mBirthdayValidationWatcher = new TextValidationWatcher((TextInputLayout) findViewById(R.id.birthdayTextInputLayout), new PlainTextValidator(), R.string.required);
        mBirthday.addTextChangedListener(mBirthdayValidationWatcher);

    }

    @OnClick(R.id.createAccountButton)
    public void onCreateAccount(View view) {
        if (this.isValid()) {
            User u = User.builder()
                    .firstName(this.mFirstName.getText().toString())
                    .lastName(this.mLastName.getText().toString())
                    .email(this.mEmail.getText().toString())
                    .phoneNumber(this.mMobilePhone.getText().toString())
                    .birthday(this.mBirthday.getText().toString())
                    .password(this.mPassword.getText().toString())
                    .build();

            CreateUserEvent event = CreateUserEvent.getInstance(repository);

            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.show();

            createUserEventSubscription = event.execute(u)
                    .subscribe(
                            this.getCreateUserEventOnNextConsumer(progressDialog),
                            this.getUserCreateEventOnErrorConsumer(progressDialog));
        }
    }

    @OnClick(R.id.backToLoginTextView)
    public void onBackToLoginSelected() {
        finish();
    }

    @OnClick(R.id.birthday)
    public void onBirthdaySelected(View view) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mBirthday.setText(sdf.format(myCalendar.getTime()));
            }

        };

        new DatePickerDialog(SignupActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Not the cleanest way but works for now. Need to think of a better way for this
    private Consumer getUserCreateEventOnErrorConsumer(final ProgressDialog progressDialog) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                progressDialog.dismiss();
                if (throwable instanceof RepositoryException) {
                    RepositoryException e = (RepositoryException) throwable;
                    if (e.getExceptionCode() == RepositoryExceptionCode.DUPLICATE_RECORD) {
                        Toast.makeText(SignupActivity.this, R.string.user_already_exists, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Toast.makeText(SignupActivity.this, R.string.user_creation_failed, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Consumer getCreateUserEventOnNextConsumer(final ProgressDialog progressDialog) {
        return new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, R.string.user_created, Toast.LENGTH_LONG).show();
                Intent upIntent = NavUtils.getParentActivityIntent(SignupActivity.this);
                NavUtils.navigateUpTo(SignupActivity.this, upIntent);
            }
        };
    }

    private boolean isValid() {
        boolean emailIsValid = this.mEmailValidatrionWatcher.validate(this.mEmail.getText().toString());
        boolean passwordIsValid = this.mPasswordValidationWatcher.validate(this.mPassword.getText().toString());
        boolean firstNameIsValid = this.mFirstNameValidationWatcher.validate(this.mFirstName.getText().toString());
        boolean lastNameIsValid = this.mLastNameValidationWatcher.validate(this.mLastName.getText().toString());
        boolean birthdayIsValid = this.mBirthdayValidationWatcher.validate(this.mBirthday.getText().toString());

        return emailIsValid && passwordIsValid && firstNameIsValid && lastNameIsValid && birthdayIsValid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.repository.destroy();

        if(this.createUserEventSubscription != null && ! this.createUserEventSubscription.isDisposed()) {
            this.createUserEventSubscription.dispose();
        }
    }
}
