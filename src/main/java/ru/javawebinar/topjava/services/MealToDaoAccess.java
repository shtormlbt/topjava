package ru.javawebinar.topjava.services;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealToDaoAccess {
    Meal add(Meal meal);
    Meal delete(int id);
    List<Meal> addAll(List<Meal> meals);
    List<Meal> getMeals();
    Meal findToId(int id);
    Meal update(Meal meal);
}
