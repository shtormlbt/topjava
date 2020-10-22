package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ+2;
    public static final int MEAL_ID2 = START_SEQ+3;
    public static final int MEAL_ID3 = START_SEQ+4;
    public static final int MEAL_ID4 = START_SEQ+5;
    public static final int MEAL_ID5 = START_SEQ+6;
    public static final int MEAL_ID6 = START_SEQ+7;
    public static final int ADMIN_ID = START_SEQ+1;
    public static final int NOT_FOUND = 10;
    public static final int USER_ID = 100000;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);

    public static final Meal meal = new Meal(MEAL_ID,LocalDateTime.of(2020,10,18,8,30,30), "завтрак", 1000);
    public static final Meal meal2 = new Meal(MEAL_ID2,LocalDateTime.of(2020,10,18,11,17,30), "второй завтрак", 600);
    public static final Meal meal3 = new Meal(MEAL_ID3,LocalDateTime.of(2020,10,18,13,00,30), "обед", 2000);
    public static final Meal meal4 = new Meal(MEAL_ID4,LocalDateTime.of(2020,10,18,8,30,30), "завтрак", 1500);
    public static final Meal meal5 = new Meal(MEAL_ID5,LocalDateTime.of(2020,10,18,11,17,30), "второй завтрак", 900);
    public static final Meal meal6 = new Meal(MEAL_ID6,LocalDateTime.of(2020,10,18,13,00,30), "обед", 2600);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020,10,18,8,30,40), "завтрак", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = meal;
        updated.setDateTime(LocalDateTime.of(2020,10,18,8,30,30));
        updated.setCalories(330);
        return updated;
    }

    public static List<Meal> getBeetwin(LocalDate startDate,LocalDate endDate, int userId){
        List<Meal> list = new ArrayList<>();
        list.add(meal);
        list.add(meal2);
        list.add(meal3);
        return list;
    }

//    public static void assertMatch(User actual, User expected) {
//        assertThat(actual).usingRecursiveComparison().ignoringFields("registered", "roles").isEqualTo(expected);
//    }

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
