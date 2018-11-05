package com.sweng888.androiduiandlogin_ericbratter.events;

import android.support.annotation.NonNull;

import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FindUserByIDEvent {
    private static final String LOG_TAG = "FindUserByIDEvent";

    private static LoginUserEvent event;
    private UserRepository repository;

    public static final FindUserByIDEvent getInstance(UserRepository repository) {
        return new FindUserByIDEvent(repository);
    }

    private FindUserByIDEvent(UserRepository repository) {
        this.repository = repository;
    }

    public Single<User> execute(@NonNull String email) {
        return repository.getById(email)
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
