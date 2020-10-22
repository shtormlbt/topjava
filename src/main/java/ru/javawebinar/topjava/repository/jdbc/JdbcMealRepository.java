package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {


    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;


    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");



        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("user_id",userId)
                .addValue("id",meal.getId())
                .addValue("date_time",meal.getDateTime())
                .addValue("description",meal.getDescription())
                .addValue("calories",meal.getCalories());


        if(meal.isNew()){
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());

        }else if(namedParameterJdbcTemplate.update(
            "UPDATE meals SET user_id=:user_id, date_time=:date_time, description=:description, calories=:calories WHERE id=:id", map)==0){
            return null;
        }
            return meal;

    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals m WHERE m.user_id=? AND m.id=?;",userId,id) != 0;

    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT m.* FROM meals m WHERE m.user_id=? AND m.id=?;",ROW_MAPPER,userId,id);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
       // select mu.user_id, mu.meal_id, m.*  FROM user_meal mu INNER JOIN meals m ON mu.meal_id = m.id WHERE mu.user_id=100000;
        return jdbcTemplate.query("SELECT m.* FROM meals m WHERE m.user_id=? ORDER BY m.date_time DESC;",ROW_MAPPER,userId);
    }

    /**
     * входят даты в виде 2020-10-17T00:00
     * @param startDateTime
     * @param endDateTime
     * @param userId
     * @return
     */
    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT m.* FROM meals m WHERE m.user_id=? AND m.date_time BETWEEN ? AND ? ORDER BY m.date_time DESC",ROW_MAPPER,userId,startDateTime,endDateTime);
    }
}