package ru.javawebinar.topjava.services;

import ru.javawebinar.topjava.dao.MealMemoryDao;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealToDaoAccessMemoryImpl implements MealToDaoAccess {

    private MealMemoryDao mealMemoryDao = MealMemoryDao.getInstance();


    @Override
    public synchronized Meal add(Meal meal) {
        return mealMemoryDao.add(meal);
    }

    @Override
    public Meal delete(int id) {
        return mealMemoryDao.delete(id);
    }

    @Override
    public List<Meal> addAll(List<Meal> meals) {
        return mealMemoryDao.addAll(meals);
    }

    @Override
    public List<Meal> getMeals() {
        return mealMemoryDao.getMeals();
    }
}
