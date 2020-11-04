package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class OneBaseClassTest {

    private static final Logger log = getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @Autowired
    private MealService mealService;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
        results.delete(0,results.length());
    }

    @Test
    public void mealDelete() {
        mealService.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void mealDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL_NOT_FOUND, USER_ID));
    }

    @Test
    public void mealDeleteNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void mealCreate() {
        Meal created = mealService.create(getNewMeal(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void mealDuplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }


    @Test
    public void mealGet() {
        Meal actual = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void mealGetNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void mealGetNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void mealUpdate() {
        Meal updated = getUpdatedMeal();
        mealService.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), getUpdatedMeal());
    }

    @Test
    public void mealUpdateNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mealService.update(meal1, ADMIN_ID));
        Assert.assertEquals("Not found entity with id=" + MEAL1_ID, exception.getMessage());
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void mealGetAll() {
        MEAL_MATCHER.assertMatch(mealService.getAll(USER_ID), meals);
    }

    @Test
    public void mealGetBetweenInclusive() {
        MEAL_MATCHER.assertMatch(mealService.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void mealGetBetweenWithNullDates() {
        MEAL_MATCHER.assertMatch(mealService.getBetweenInclusive(null, null, USER_ID), meals);
    }
    @Test
    public void userCreate() {
        User created = userService.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    public void userDuplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                userService.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void userDelete() {
        userService.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    public void userDeletedNotFound() {
        assertThrows(NotFoundException.class, () -> userService.delete(NOT_FOUND));
    }

    @Test
    public void userGet() {
        User user = userService.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void userGetNotFound() {
        assertThrows(NotFoundException.class, () -> userService.get(NOT_FOUND));
    }

    @Test
    public void userGetByEmail() {
        User user = userService.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void userUpdate() {
        User updated = getUpdated();
        userService.update(updated);
        USER_MATCHER.assertMatch(userService.get(USER_ID), getUpdated());
    }

    @Test
    public void userGetAll() {
        List<User> all = userService.getAll();
        USER_MATCHER.assertMatch(all, admin, user);
    }


}
