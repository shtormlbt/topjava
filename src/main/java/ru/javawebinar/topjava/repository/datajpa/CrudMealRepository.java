package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal getByIdAndUserId(Integer id,Integer userId);


    Meal getById(Integer id);
    List<Meal> getAllByUserIdOrderByDateTimeDesc(Integer userId);

    @Query(
            value = "SELECT m FROM Meal m WHERE m.user.id = :userId AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime ORDER BY m.dateTime DESC"
    )
    List<Meal> getDateTimeDateBetweenStartDateAndStopDate(@Param("userId")int userId, @Param("startDateTime")LocalDateTime startDateTime, @Param("endDateTime")LocalDateTime endDateTime);


}
