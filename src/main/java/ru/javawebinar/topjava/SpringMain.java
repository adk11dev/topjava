package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 31, 23, 59, 59), "Новогодний ужин1", 5000));
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 31, 23, 59, 59), "Новогодний ужин2", 5000));
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 31, 23, 59, 59), "Новогодний ужин3", 5000));
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 31, 23, 59, 59), "Новогодний ужин4", 5000));
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 31, 23, 59, 59), "Новогодний ужин5", 5000));
            System.out.println(mealRestController.get(5));
        }
    }
}