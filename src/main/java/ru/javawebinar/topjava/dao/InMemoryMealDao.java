package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements Storage<Meal> {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger();

    public InMemoryMealDao() {
        fillMealsMap();
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal get(int id) {
        return mealsMap.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(counter.incrementAndGet());
        mealsMap.put(meal.getId(), meal);
        return mealsMap.get(meal.getId());
    }

    @Override
    public Meal update(Meal meal) {
        mealsMap.replace(meal.getId(), meal);
        return mealsMap.get(meal.getId());
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }

    private void fillMealsMap() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        for (Meal meal : meals) {
            create(meal);
        }
    }
}