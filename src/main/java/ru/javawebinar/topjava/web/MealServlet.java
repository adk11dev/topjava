package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryDao;
import ru.javawebinar.topjava.dao.Storage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final Storage<Meal> dao;
    private static final String INSERT_OR_EDIT = "/mealsInsertEdit.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final List<Meal> meals = new ArrayList<>();
    private static final int CALORIES_LIMIT = 2000;

    static {
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public MealServlet() {
        super();
        this.dao = new InMemoryDao(meals);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");

        if (action == null) {
            forward = LIST_MEALS;
            List<MealTo> mealTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);
            request.setAttribute("mealsList", mealTo);
            request.setAttribute("timeUtil",new TimeUtil());
        } else {
            if (action.equalsIgnoreCase("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));

                log.debug("delete meal");
                dao.delete(id);

                log.debug("redirect to meals");
                response.sendRedirect("meals");
                return;
            }
            if (action.equalsIgnoreCase("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                Meal meal = dao.get(id);
                forward = INSERT_OR_EDIT;
                request.setAttribute("id", id);
                request.setAttribute("meal", meal);
            }
            if (action.equalsIgnoreCase("insert")) {
                forward = INSERT_OR_EDIT;
            }
        }

        log.debug("forward to meals");
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal;

        if (request.getParameter("id") != "") {

            int id = Integer.parseInt(request.getParameter("id"));
            meal = dao.get(id);

            meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));

            log.debug("updated meal");
            dao.update(id, meal);
        } else {
            meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.debug("save meal");
            dao.save(meal);
        }
        List<MealTo> mealTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);

        String forward = LIST_MEALS;
        request.setAttribute("mealsList", mealTo);

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }
}