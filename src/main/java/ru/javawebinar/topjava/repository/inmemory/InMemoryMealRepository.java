package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal meal : MealsUtil.meals) {
            save(1, meal);
        }
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }else if(meal.getUserId()==userId){
            // handle case: update, but not present in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
                return meal;
            });
        }else{
            return null;
        }
    }

    @Override
    public boolean delete(Integer userId, int id) {
        if(repository.get(id).getUserId()==userId) {
            repository.remove(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Meal get(Integer userId, int id) {
        if(repository.get(id).getUserId()==userId) {
            return repository.get(id);
        }else{
            return null;
        }

    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.values().stream().filter(x->x.getUserId()==userId).sorted(
                new Comparator<Meal>() {
                    @Override
                    public int compare(Meal o1, Meal o2) {
                        return (o2.getDate().compareTo(o1.getDate()));
                    }
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFilteredByDate(Integer userId, LocalDate dateafter, LocalDate datebefore){
       //return repository.values().stream().filter(da->da.getDate().isAfter(dateafter)).filter(db->db.getDate().isBefore(datebefore)).collect(Collectors.toList());
       return repository.values().stream().filter(da->DateTimeUtil.isBetweenDate(da.getDate(),dateafter,datebefore)).filter(db->DateTimeUtil.isBetweenDate(db.getDate(),dateafter,datebefore))
                .collect(Collectors.toList());
    }


}

