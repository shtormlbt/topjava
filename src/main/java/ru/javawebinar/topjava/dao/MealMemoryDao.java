package ru.javawebinar.topjava.dao;


import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Singleton для хранения данных Еды в памяти
 */
public class MealMemoryDao {
    private static final Logger log = getLogger(MealMemoryDao.class);
    private static List<Meal> meals = new ArrayList<>();
    private static MealMemoryDao instance;
    private static int countid;



    private MealMemoryDao(){
       addAll(MealsUtil.mealHardCode());
    }

    public static MealMemoryDao getInstance(){
        if(instance==null){
            return new MealMemoryDao();
        }else{

            return instance;
        }
    }

    /**
     *
     * @param meal
     * @return
     */
    public Meal add(Meal meal){
        log.info("добавляем Meal ");
        log.debug("countid "+countid);
        countid++;
        log.debug("countid+1 "+countid);
        meal.setId(countid);

        meals.add(meal);
        log.debug("добавили вот такой Meal "+meal.toString());
        return meal;
    }

    /**
     * удаляет еду из списка
     * @param id - id еды
     * @return - удаленный объект еды
     */
    public Meal delete(int id){
        Meal retutnM = null;
        for(Meal m:meals){
            if(m.getId()==id){
                retutnM = m;
                meals.remove(m);
                break;
            }
        }
        return retutnM;
    }

    /**
     * добавляет в список список еды
     * @param mealList
     * @return
     */
    public synchronized List<Meal> addAll(List<Meal> mealList) {
        log.debug("addAll добавляем тестовые данные: ");
        for (Meal m : mealList) {
            log.debug("получено: " + m.toString());
        }
        List<Meal> returnList = new ArrayList<>();
        for (Meal m : mealList) {
            Meal addMeal = add(m);
            log.debug("добавили в базу: " + addMeal.toString());
            returnList.add(addMeal);
        }
        return returnList;
    }

    /**
     * возвращает все данные
     * @return
     */
    public List<Meal> getMeals(){
        return meals;
    }

    /**
     * поиск объекта Meal по id;
     * @param id
     * @return - возвражает найденный объект либо null;
     */
    public Meal findToId(int id){
        Optional<Meal> returtMeal = meals.stream().filter(x->x.getId()==id).findFirst();
        return returtMeal.orElse(null);
    }

    public Meal update(Meal meal){
        delete(meal.getId());
        meals.add(meal);
        return meal;
    }

}
