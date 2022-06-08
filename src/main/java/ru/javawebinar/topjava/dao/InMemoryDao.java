package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDao implements Storage<Meal> {
    private final Map<Integer, Meal> mealsMap = new HashMap<>();

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal get(int key) {
        return mealsMap.get(key);
    }

    @Override
    public Meal create(Map<String, String[]> map) {
        if (!map.isEmpty()) {
            LocalDateTime dateTime = LocalDateTime.parse(map.get("dateTime")[0]);
            String description = map.get("description")[0];
            int calories = Integer.parseInt(map.get("calories")[0]);

            Meal meal = new Meal(dateTime, description, calories);
            meal.setId(counter.incrementAndGet());

            mealsMap.put(meal.getId(),meal);

            return meal;
        } else {
            return null;
        }
    }

    @Override
    public Meal update(Map<String, String[]> map) {
        if (!map.isEmpty()) {
            int id = Integer.parseInt(map.get("id")[0]);
            Meal meal = get(id);

            LocalDateTime dateTime = LocalDateTime.parse(map.get("dateTime")[0]);
            meal.setDateTime(dateTime);

            String description = map.get("description")[0];
            meal.setDescription(description);

            int calories = Integer.parseInt(map.get("calories")[0]);
            meal.setCalories(calories);

            return meal;
        } else {
            return null;
        }
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }
}