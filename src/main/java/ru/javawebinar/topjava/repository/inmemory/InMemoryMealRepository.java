package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(1, meal));
        save(2, new Meal(LocalDateTime.now(), "Завтрак второго пользователя", 300));
        save(2, new Meal(LocalDateTime.now(), "Обед второго пользователя", 1200));
        save(2, new Meal(LocalDateTime.now(), "Ужин второго пользователя", 501));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer,Meal> tmp = repository.getOrDefault(userId, new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            tmp.put(meal.getId(), meal);
            repository.put(userId,tmp);
            return meal;
        }
        if (get(userId, meal.getId()).getUserId() == userId) {
            meal.setUserId(userId);
            return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        Meal meal = repository.get(userId).get(id);
        if (meal == null) {
            return false;
        }

        if (meal.getUserId() == userId) {
            return repository.get(userId).remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(userId).get(id);

        if (meal == null) {
            return null;
        }

        if (meal.getUserId() == userId) {
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.get(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}