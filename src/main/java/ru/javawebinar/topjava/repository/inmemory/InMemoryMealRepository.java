package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (meal.getUserId() == userId) {
            repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        if (isNotExist(id)) {
            return false;
        }

        if (get(userId, id).getUserId() == userId) {
            return repository.remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int userId, int id) {
        if (isNotExist(id)) {
            return null;
        }

        if (repository.get(id).getUserId() == userId) {
            return repository.get(id);
        } else {
            throw new NotFoundException("Meal not found!");
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private boolean isNotExist(int id) {
        return repository.get(id) == null;
    }
}