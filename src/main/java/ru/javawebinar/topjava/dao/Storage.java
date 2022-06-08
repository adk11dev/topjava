package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Storage<T> {
    List<T> getAll();

    T get(int id);

    T create(T t);

    T update(T t);

    void delete(int id);
}