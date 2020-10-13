package ru.javawebinar.topjava.web;

import java.time.LocalDate;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int loginUserId;

    public static int authUserId() {
        return loginUserId ;
    }

    public static void setAuthUserId(int userId){
        loginUserId = userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}