package com.sweng888.androiduiandlogin_ericbratter.events;

import android.arch.core.util.Function;
import android.support.annotation.NonNull;

import com.sweng888.androiduiandlogin_ericbratter.exception.RepositoryException;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateUserEvent {
    private static final String LOG_TAG = "CreateUserEvent";

    private UserRepository repository;

    private CreateUserEvent(UserRepository repository){
        this.repository = repository;
    }

    public static final CreateUserEvent getInstance(UserRepository repository) {
        return new CreateUserEvent(repository);
    }

    public Single<User> execute(@NonNull User u) {
        return repository.create(u)
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
