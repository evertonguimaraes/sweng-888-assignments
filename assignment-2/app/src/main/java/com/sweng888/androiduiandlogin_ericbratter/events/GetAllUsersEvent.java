package com.sweng888.androiduiandlogin_ericbratter.events;

import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetAllUsersEvent {
    private static final String LOG_TAG = "GetAllUsersEvent";

    private static LoginUserEvent event;
    private UserRepository repository;

    public static final GetAllUsersEvent getInstance(UserRepository repository) {
        return new GetAllUsersEvent(repository);
    }

    private GetAllUsersEvent(UserRepository repository) {
        this.repository = repository;
    }

    public Observable<List<User>> execute() {
        return repository.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
