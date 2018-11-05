package com.sweng888.androiduiandlogin_ericbratter;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sweng888.androiduiandlogin_ericbratter.events.FindUserByIDEvent;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;
import com.sweng888.androiduiandlogin_ericbratter.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";

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
    @BindView(R.id.headerButtonFullName)
    Button mHeaderButtonFullName;
//    @BindView(R.id.password)
//    EditText mPassword;
//    @BindView(R.id.emailTextInputLayout)
//    TextInputLayout mEmailInputLayout;

    private UserRepository repository;
    private Disposable findUserByIDEventSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Inject all the view dependencies
        ButterKnife.bind(this);

        // Not sure I like having this here need to better setup MVC of MVVM
        repository = new UserRepository(this);

        Intent intent = getIntent();
        if(intent == null) {
            this.cannotLoadUser();
            return;
        }

        String email = intent.getStringExtra(Constants.INTENT_PARAM_USER_ID);

        if(email == null || email.equals("")) {
            this.cannotLoadUser();
            return;
        }
        FindUserByIDEvent event = FindUserByIDEvent.getInstance(this.repository);

        findUserByIDEventSubscription = event.execute(email)
                .subscribe(
                        this.findUserByIdEventOnSuccess(),
                        this.findUserByIdEventOnError());
    }

    private void mapUserToUI(User u){
        mFirstName.setText(u.getFirstName());
        mLastName.setText(u.getLastName());
        mEmail.setText(u.getEmail());
        mBirthday.setText(u.getBirthday());
        mMobilePhone.setText(u.getPhoneNumber());
        mHeaderButtonFullName.setText(
                Constants.PADDING_FOR_USER_FULL_NAME +
                        u.getFirstName() +
                        " " +
                        u.getLastName() +
                        Constants.PADDING_FOR_USER_FULL_NAME
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.userListMenuItem:
                this.navigateToFullUserList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToFullUserList(){
        Intent intent = new Intent(UserProfileActivity.this, UserListingActivity.class);
        intent.putExtra(Constants.INTENT_PARAM_USER_ID, mEmail.getText().toString());
        startActivity(intent);
    }

    private Consumer<User> findUserByIdEventOnSuccess(){
        return new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                Log.d(UserProfileActivity.TAG, user.toString());
                // Set the user data to the UI Fields
                UserProfileActivity.this.mapUserToUI(user);
            }
        };
    }

    private Consumer<Throwable> findUserByIdEventOnError(){
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(UserProfileActivity.TAG, "Error on loading user");
                UserProfileActivity.this.cannotLoadUser();
            }
        };
    }

    private void cannotLoadUser(){
        Toast.makeText(UserProfileActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.repository.destroy();

        if(this.findUserByIDEventSubscription != null && ! this.findUserByIDEventSubscription.isDisposed()) {
            this.findUserByIDEventSubscription.dispose();
        }
    }
}
