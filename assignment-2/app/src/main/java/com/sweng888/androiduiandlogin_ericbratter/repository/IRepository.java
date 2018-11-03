package com.sweng888.androiduiandlogin_ericbratter.repository;

import java.util.List;

import io.reactivex.Observable;

public interface IRepository <T, K> {
    Observable<List<T>> get();
    Observable<T> getById(K id);
    Observable<T> create(T entity);
    Observable<T> update(T entity);
    Observable<T> delete(T entity);
    void destroy();
}