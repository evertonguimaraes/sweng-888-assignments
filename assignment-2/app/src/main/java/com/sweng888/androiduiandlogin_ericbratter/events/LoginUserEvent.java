package com.sweng888.androiduiandlogin_ericbratter.events;

import android.support.annotation.NonNull;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class LoginUserEvent {
    private static final String LOG_TAG = "LoginUserEvent";

    private static LoginUserEvent event;
    private UserRepository repository;

    public static final LoginUserEvent getInstance(UserRepository repository) {
        return new LoginUserEvent(repository);
    }

    private LoginUserEvent(UserRepository repository) {
        this.repository = repository;
    }

    public Single<User> execute(@NonNull String email, @NonNull final String password) {
        return repository.getById(email)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        Thread.sleep(3000); // Simulator some time delay here
                        return user.getPassword().equals(password);
                    }
                })
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
