package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Controller
public class JspMealController{
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService mealService;


    @GetMapping("/meals")
    public String getMeals(String startDate,String endDate,LocalTime startTime,
                           LocalTime endTime, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}",userId);
        LocalDate startDateLocal = startDate==null?null:LocalDate.parse(startDate);
        LocalDate endDateLocal = endDate==null?null:LocalDate.parse(endDate);

        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startDateLocal,endDateLocal, userId);
        model.addAttribute("meals",MealsUtil.getFilteredTos(mealsDateFiltered,SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        //model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId),SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/meals/delete")
    public String delMeal(String mealId){
        int userId = SecurityUtil.authUserId();
        log.info("delete meal for user {}",userId);
        mealService.delete(Integer.parseInt(mealId),userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/update")
    public String updateMeal(String mealId, Model model){
        int userId = SecurityUtil.authUserId();
        final Meal meal = mealId==null?new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),"",1000):
                mealService.get(Integer.parseInt(mealId),userId);

        log.info("add/update meal id {} for user {}",meal.getId(), userId);
        model.addAttribute("meal",meal);
        return "/mealForm";
    }

    @PostMapping("/meals/update")
    public String updateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        int mealId = 0;
        boolean isNew = false;
        if(request.getParameter("id")==""){
            isNew=true;
        }else{
            mealId=Integer.parseInt(request.getParameter("id"));
        }
        Meal meal = new Meal();
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if(isNew){
            mealService.create(meal,userId);
        }else{
            meal.setId(mealId);
            mealService.update(meal,userId);
        }
        return "redirect:/meals";
    }

}
