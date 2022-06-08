package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryDao;
import ru.javawebinar.topjava.dao.Storage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/mealsInsertEdit.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;
    private final Storage<Meal> dao;

    public MealServlet() {
        this.dao = new InMemoryDao();
        fillMealsMap();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        if (action == null) {
            forward = LIST_MEALS;
            List<MealTo> mealTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);
            request.setAttribute("mealsList", mealTo);
        } else {
            int id;
            switch (action.toLowerCase()) {
                case "delete":
                    id = Integer.parseInt(request.getParameter("id"));

                    log.debug("delete meal");
                    dao.delete(id);

                    log.debug("redirect to meals");
                    response.sendRedirect("meals");
                    return;
                case "update":
                    id = Integer.parseInt(request.getParameter("id"));
                    Meal meal = dao.get(id);

                    forward = INSERT_OR_EDIT;

                    request.setAttribute("meal", meal);
                    break;
                case "insert":
                    forward = INSERT_OR_EDIT;
                    break;
                default:
                    response.sendRedirect("meals");
                    return;
            }
        }

        log.debug("forward to meals");
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (!Objects.equals(request.getParameter("id"), "")) {
            log.debug("updated meal");
            dao.update(request.getParameterMap());
        } else {
            log.debug("save meal");
            dao.create(request.getParameterMap());
        }

        log.debug("redirect to meals");
        response.sendRedirect("meals");
    }

    private void fillMealsMap() {
        List<Meal> meals = new ArrayList<>();
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));


        Map<String, String[]> mealsMap = new HashMap<>();
        for (Meal meal : meals) {
            mealsMap.put("dateTime", new String[]{meal.getDateTime().toString()});
            mealsMap.put("description", new String[]{meal.getDescription()});
            mealsMap.put("calories", new String[]{String.valueOf(meal.getCalories())});

            dao.create(mealsMap);

            mealsMap.clear();
        }
    }
}