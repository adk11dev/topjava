package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Storage<T> {
    List<T> getAll();

    T get(Integer id);

    void save(T t);

    void update(Integer id,T t);

    void delete(Integer id);
}