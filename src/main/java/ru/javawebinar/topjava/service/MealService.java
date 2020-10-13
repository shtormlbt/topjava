package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.format.datetime.joda.LocalDateTimeParser;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;


@Service
public class MealService {

    @Autowired
    //@Qualifier("InMemoryMealRepository")
    private MealRepository repository;


    public Meal create(Integer userId, Meal meal){
        if(userId!=null&&meal!=null) {
            repository.save(userId, meal);
            return meal;
        }else{
            return null;
        }
    }

    public void delete(Integer userId, int mealId){
        Meal rmeal = repository.get(userId,mealId);
        if(rmeal.getUserId()==userId) {
            repository.delete(userId, mealId);
        }else {
            throw new NotFoundException("не найдена еда с id "+mealId+", или он принадлежит другому пользователю");
        }
    }

    public Meal get(Integer userId, int mealId){

        Meal rmeal = repository.get(userId,mealId);

        if(rmeal.getUserId()==userId) {
            return rmeal;
        }else {
            throw new NotFoundException("не найдена еда с id "+mealId+", или он принадлежит другому пользователю");
        }
    }

    /**
     * здесь конвертация Meal в MealTo
     * @param userId
     *
     * @return
     */
    public List<MealTo> getAll(Integer userId, int caloriesPerDay){
        Collection<Meal> rmeal;
        if((rmeal = repository.getAll(userId))!=null){
            return MealsUtil.getTos(rmeal,caloriesPerDay);
        }else {
            throw new NotFoundException("еда для переданного юзера не найдена");
        }
    }

    public void update(Integer userId, Meal meal){
        repository.save(userId,meal);
    }


    public List<MealTo> getFilteredDateTime(Integer userId, int caloriesPerDay, String dateafter, String datebefore, String timeafter, String timebefore){


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(dateafter=="")dateafter="0001-01-01";
        if(datebefore=="")datebefore="9999-01-01";
        LocalDate dateAfterLD = LocalDate.parse(dateafter,dtf);
        LocalDate dateBeforeLD = LocalDate.parse(datebefore,dtf);

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        if(timeafter=="")timeafter="00:00";
        if(timebefore=="")timebefore="23:59";
        LocalTime timeAfterLt = LocalTime.parse(timeafter,tf);
        LocalTime timeBeforeLt = LocalTime.parse(timebefore,tf);

        List<MealTo> returnList = new ArrayList<>();
        List<Meal> list;
        if(dateafter!=null||datebefore!=null){
            System.out.println(dateafter+" "+datebefore);
            list = (ArrayList<Meal>) repository.getFilteredByDate(userId,dateAfterLD,dateBeforeLD);
            returnList = MealsUtil.getFilteredTos(list,caloriesPerDay,timeAfterLt,timeBeforeLt);
        }


        return returnList;
    }


}