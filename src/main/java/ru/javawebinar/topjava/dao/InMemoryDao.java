package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class InMemoryDao implements Storage<Meal> {
    private final List<Meal> mealList;

    public InMemoryDao(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @Override
    public List<Meal> getAll() {
        return mealList;
    }

    @Override
    public Meal get(Integer id) {
        if (id != null) {
            Meal meal = new Meal(id);
            int index = mealList.indexOf(meal);
            return mealList.get(index);
        }
        return null;
    }

    @Override
    public void save(Meal meal) {
        if (meal != null) {
            mealList.add(meal);
        }
    }

    @Override
    public void update(Integer id, Meal meal) {
        if (meal != null) {
            mealList.add(id, meal);
        }
    }

    @Override
    public void delete(Integer id) {
        if (id != null) {
            Meal meal = get(id);
            mealList.remove(meal);
        }
    }
}