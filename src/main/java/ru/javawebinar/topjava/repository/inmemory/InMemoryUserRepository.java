package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);



    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()){
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) ->{
            return user;
        });
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<User>(repository.values().stream().sorted(
                new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        if(o1.getName().compareTo(o2.getName())==0){
                            return o1.getEmail().compareTo(o2.getEmail());
                        }else{
                            return o1.getName().compareTo(o2.getName());
                        }

//                        if(o1.getId()==o2.getId()){
//                            return o1.getEmail().compareTo(o2.getEmail());
//                        }else{
//                            if(o1.getId()>o2.getId()){
//                                return 1;
//                            }else{
//                                return -1;
//                            }
//                        }

                    }
                }
        ).collect(Collectors.toList()));
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Optional<User> r = repository.values().stream().filter(x->x.getEmail().equalsIgnoreCase(email)).findFirst();
        return r.get();
    }
}
