package com.sweng888.androiduiandlogin_ericbratter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sweng888.androiduiandlogin_ericbratter.fragment.UserListFragment;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;

public class UserListingActivity extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_listing_activity);
    }

    @Override
    public void onFragmentInteraction(User u) {
        // Show a popup fragment I guess or something like that
    }
}
