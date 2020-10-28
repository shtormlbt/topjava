package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@Transactional
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = em.find(User.class,userId);
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            User user = em.find(User.class,userId);
            Meal meal1 = em.find(Meal.class,meal.getId());
            if(meal1.getUser()==user) {
                meal.setUser(user);
                return em.merge(meal);
            }else{
                throw new NotFoundException("");
            }
        }

    }

    @Override
    public boolean delete(int id, int userId) {

            return em.createNamedQuery(Meal.DELETE)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .executeUpdate() != 0;

    }

    @Override
    public Meal get(int id, int userId) {
//        Meal meal = (Meal) em.createNamedQuery(Meal.GET)
//                .setParameter("id",id)
//                .setParameter("userId",userId)
//                .getSingleResult();
//        if(meal==null){
//            throw new NotFoundException("not find meal");
//        }else {
//            return meal;
//        }
        Meal meal = em.find(Meal.class,id);
        User user = em.find(User.class,userId);
        if(meal==null||meal.getUser()!=user){
            throw new NotFoundException("not find meal");
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL)
                .setParameter("userId",userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN_HALF_OPEN)
                .setParameter("userId", userId)
                .setParameter("startDateTime",startDateTime)
                .setParameter("endDateTime",endDateTime)
                .getResultList();
    }
}