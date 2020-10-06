package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.services.MealToDaoAccess;
import ru.javawebinar.topjava.services.MealToDaoAccessMemoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealToDaoAccessMemoryImpl mealToDaoAccess = new MealToDaoAccessMemoryImpl();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("meals servlet started");


        String action = request.getParameter("action")==null?"":request.getParameter("action");
        String idStr = request.getParameter("id")==null?"":request.getParameter("id");

        if(action==""){
            List<MealTo> mealTos = MealsUtil.filteredByStreams(mealToDaoAccess.getMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            request.setAttribute("meals", mealTos);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }else if(action.equalsIgnoreCase("delete")){
            mealToDaoAccess.delete(Integer.parseInt(idStr));
            List<MealTo> mealTos = MealsUtil.filteredByStreams(mealToDaoAccess.getMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            request.setAttribute("meals", mealTos);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }else if(action.equalsIgnoreCase("add")){
            request.getRequestDispatcher("/mealsAdd.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String dateStr = request.getParameter("dateTime");
       log.debug("dateTime:"+dateStr);
       String description = new String(request.getParameter("description").getBytes("ISO-8859-1"),"UTF-8");
       log.debug("description:"+description);
       String calories = request.getParameter("calories");
       log.debug("calories:"+calories);
       DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

       Meal meal = new Meal(
               LocalDateTime.parse(dateStr,dtf),description,Integer.parseInt(calories)
       );
       meal = mealToDaoAccess.add(meal);
       log.debug("meal add:"+meal);
       doGet(request,response);
    }
}
