package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.dao.Storage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/mealsInsertEdit.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;
    private Storage<Meal> dao;

    @Override
    public void init() throws ServletException {
        dao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        action = (action == null) ? "list" : action.toLowerCase();
        switch (action) {
            case "list": {
                forward = LIST_MEALS;
                List<MealTo> mealTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);
                request.setAttribute("mealsList", mealTo);
                break;
            }
            case "delete": {
                int id = getIdInRequest(request);
                log.debug("delete meal");
                dao.delete(id);
                log.debug("redirect to meals");
                response.sendRedirect("meals");
                return;
            }
            case "update": {
                int id = getIdInRequest(request);
                Meal meal = dao.get(id);
                forward = INSERT_OR_EDIT;
                request.setAttribute("meal", meal);
                break;
            }
            case "insert": {
                forward = INSERT_OR_EDIT;
                break;
            }
            default: {
                response.sendRedirect("meals");
                return;
            }
        }
        log.debug("forward to meals");
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);

        if (!Objects.equals(request.getParameter("id"), "")) {
            int id = getIdInRequest(request);
            meal.setId(id);
            log.debug("updated meal");
            dao.update(meal);
        } else {
            log.debug("save meal");
            dao.create(meal);
        }
        log.debug("redirect to meals");
        response.sendRedirect("meals");
    }

    private int getIdInRequest(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}