package com.sweng888.androiduiandlogin_ericbratter.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.tables.UserTable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class UserRepository implements IRepository<User, String> {
    public DatabaseAccess databaseAccess;
    private Context context;

    public UserRepository(Context context) {
        this.context = context;
        this.databaseAccess = new DatabaseAccess(context);
    }

    @Override
    public Observable<List<User>> get() {
        return null;
    }

    @Override
    public Observable<User> getById(String id) {
        final String email = id;
        return Observable.<User>create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                String[] projection = {
                        UserTable.COLUMN_NAME_ID,
                        UserTable.COLUMN_NAME_FIRST_NAME,
                        UserTable.COLUMN_NAME_LAST_NAME,
                        UserTable.COLUMN_NAME_BIRTHDAY,
                        UserTable.COLUMN_NAME_PASSWORD,
                        UserTable.COLUMN_NAME_MOBILE_PHONE
                };

                String selection = UserTable.COLUMN_NAME_ID + " = ?";
                String[] selectionArgs = {email};

                SQLiteDatabase db = databaseAccess.getReadableDatabase();
                Cursor cursor = db.query(UserTable.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, null);

                if (cursor.moveToNext()) {
                    User u = User.builder()
                            .email(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_ID)))
                            .firstName(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_FIRST_NAME)))
                            .lastName(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_LAST_NAME)))
                            .birthday(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_BIRTHDAY)))
                            .password(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_PASSWORD)))
                            .phoneNumber(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_MOBILE_PHONE)))
                            .build();

                    emitter.onNext(u);
                }
                cursor.close();
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<User> create(User entity) {
        final User user = entity;
        return Observable.<User>create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                // Gets the data repository in write mode
                SQLiteDatabase db = databaseAccess.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(UserTable.COLUMN_NAME_ID, user.getEmail());
                values.put(UserTable.COLUMN_NAME_FIRST_NAME, user.getFirstName());
                values.put(UserTable.COLUMN_NAME_LAST_NAME, user.getLastName());
                values.put(UserTable.COLUMN_NAME_BIRTHDAY, user.getBirthday());
                values.put(UserTable.COLUMN_NAME_PASSWORD, user.getPassword());
                values.put(UserTable.COLUMN_NAME_MOBILE_PHONE, user.getPhoneNumber());

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(UserTable.TABLE_NAME, null, values);

                if(newRowId > 0) {
                    emitter.onNext(user);
                    emitter.onComplete();
                }else{
                    emitter.onError(new SQLException("Error saving to the database"));
                }
            }
        });
    }

    @Override
    public Observable<User> update(User entity) {
        return null;
    }

    @Override
    public Observable<User> delete(User entity) {
        return null;
    }

    @Override
    public void destroy() {
        this.databaseAccess.close();
    }
}
