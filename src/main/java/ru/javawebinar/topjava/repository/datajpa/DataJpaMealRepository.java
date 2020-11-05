package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Autowired
    private DataJpaUserRepository userRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepository.get(userId);
        meal.setUser(user);
        if(meal.isNew()){
            return crudRepository.save(meal);
        }else if(crudRepository.getByIdAndUserId(meal.getId(),userId)==null){
            return null;
        }
        return crudRepository.save(meal);

    }

    @Override
    public boolean delete(int id, int userId) {
        //Meal meal = crudRepository.getById(id);
        Meal delMeal = crudRepository.getByIdAndUserId(id,userId);
        if(delMeal!=null){
            crudRepository.delete(delMeal);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUserId(id,userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return  crudRepository.getAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getDateTimeDateBetweenStartDateAndStopDate(userId,startDateTime,endDateTime);
    }
}
