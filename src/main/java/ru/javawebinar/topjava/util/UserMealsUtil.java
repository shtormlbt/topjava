package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 11, 59), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), 2000));
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> returnList = new ArrayList<>();
        int CalCount = 0;

        Map<LocalDate, Integer> mapCalCount = new HashMap<>();

        for (UserMeal um : meals) {
            if (mapCalCount.containsKey(um.getDateTime().toLocalDate())) {
                Integer CountT = mapCalCount.get(um.getDateTime().toLocalDate());
                CountT = CountT + um.getCalories();
                mapCalCount.put(um.getDateTime().toLocalDate(), CountT);
            } else {
                mapCalCount.put(um.getDateTime().toLocalDate(), um.getCalories());
            }


        }
        for (UserMeal um : meals) {
            LocalTime umTime = um.getDateTime().toLocalTime();
            if (umTime.isAfter(startTime) && umTime.isBefore(endTime)) {
                UserMealWithExcess UMWE = new UserMealWithExcess(um.getDateTime(), um.getDescription(), um.getCalories(), mapCalCount.get(um.getDateTime().toLocalDate()) > caloriesPerDay);
                CalCount = CalCount + um.getCalories();
                returnList.add(UMWE);
            }
        }
        return returnList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        List<UserMealWithExcess> returnList = new ArrayList<>();

        //складываем значения в мапе summ map value
        Map<LocalDate, Integer> calMapCount = meals.stream().collect(Collectors.toMap(
                i -> i.getDateTime().toLocalDate(),
                i -> i.getCalories(),
                (a, b) -> Integer.sum(a, b)
        )).entrySet().stream().filter(i -> i.getValue() > caloriesPerDay).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        meals.forEach(x -> returnList.add(new UserMealWithExcess(x.getDateTime(), x.getDescription(), x.getCalories(), calMapCount.containsKey(x.getDateTime().toLocalDate()))));


        return returnList;
    }

    //метод из разбора задания
    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        Map<LocalDate, Integer> caloriesSummByDate = meals.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),Collectors.summingInt(um -> um.getCalories())));

        return meals.stream()
                .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(),startTime,endTime))
                .map(um -> new UserMealWithExcess(um.getDateTime(),um.getDescription(),um.getCalories(),
                    caloriesSummByDate.get(um.getDateTime().toLocalDate())>caloriesPerDay))
                .collect(Collectors.toList());

    }
}
