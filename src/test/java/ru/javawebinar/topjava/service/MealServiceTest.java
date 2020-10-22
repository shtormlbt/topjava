package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() throws Exception {
        Meal meal = mealService.get(MEAL_ID,USER_ID);
        assertMatch(meal,meal);
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_ID,USER_ID);
        assertThrows(NotFoundException.class,()->mealService.get(MEAL_ID,USER_ID));
    }

    @Test
    public void delSomeFood(){
        assertThrows(NotFoundException.class,()->mealService.delete(MEAL_ID,ADMIN_ID));
    }

    @Test
    public void getSomeFood(){
        assertThrows(NotFoundException.class,()->mealService.get(MEAL_ID,ADMIN_ID));
    }

    @Test
    public void updateSomeFood(){
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class,()->mealService.get(updated.getId(),ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(LocalDateTime.of(2020,10,18,8,30,30), "завтрак", 1000),USER_ID));
    }


    @Test
    public void getBetweenInclusive() {
        List<Meal> list = mealService.getBetweenInclusive(LocalDate.of(2020,10,18),LocalDate.of(2020,10,18),USER_ID);
        assertMatch(list,mealService.getBetweenInclusive(LocalDate.of(2020,10,18),LocalDate.of(2020,10,18),USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(USER_ID);
        assertMatch(all,meal3,meal2,meal);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated,user.getId());
        assertMatch(mealService.get(MEAL_ID,USER_ID),updated);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getNew();
        Meal created = mealService.create(newMeal,USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created,newMeal);
        assertMatch(mealService.get(newId,USER_ID),newMeal);

    }
}