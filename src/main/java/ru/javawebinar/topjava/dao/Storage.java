package ru.javawebinar.topjava.dao;

import java.util.List;
import java.util.Map;

public interface Storage<T> {
    List<T> getAll();

    T get(int key);

    T create(Map<String, String[]> map);

    T update(Map<String, String[]> map);

    void delete(int key);
}