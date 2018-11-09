package com.sweng888.androiduiandlogin_ericbratter.events;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;


import javax.security.auth.login.LoginException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CreateUserEvent {
    private static final String TAG = "CreateUserEvent";

    private UserRepository repository;
    private FirebaseAuth mAuth;

    private CreateUserEvent(UserRepository repository){
        this.repository = repository;
    }

    public static final CreateUserEvent getInstance(UserRepository repository) {
        return new CreateUserEvent(repository);
    }

    public Single<User> execute(@NonNull final User u) {
        mAuth = FirebaseAuth.getInstance();

        return Observable.create(new ObservableOnSubscribe<FirebaseUser>(){
            @Override
            public void subscribe(final ObservableEmitter<FirebaseUser> emitter) throws Exception {
                mAuth.createUserWithEmailAndPassword(u.getEmail(), u.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    emitter.onNext(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    emitter.onError(new LoginException("Authentication Failed"));
                                }
                                emitter.onComplete();
                            }
                        });
            }
        }).switchMap(new Function<FirebaseUser, Observable<User>>() {
            @Override
            public Observable<User> apply(FirebaseUser firebaseUser) throws Exception {
                return repository.create(u);
            }
        }).subscribeOn(Schedulers.io()).firstOrError().observeOn(AndroidSchedulers.mainThread());
    }
}
