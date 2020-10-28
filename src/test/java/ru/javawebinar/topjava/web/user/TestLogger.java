package ru.javawebinar.topjava.web.user;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealServiceTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TestLogger implements TestRule {

    private Logger logger;

    public Logger getLogger(){
        return this.logger;
    }



    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                logger = LoggerFactory.getLogger(description.getTestClass().getName()+" "+description.getDisplayName());
                LocalDateTime start = LocalDateTime.now();
                base.evaluate();
                LocalDateTime end = LocalDateTime.now();
                long diff = ChronoUnit.MILLIS.between(start,end);
                String str = "время выполнения теста "+description.getDisplayName()+" "+diff+" мс";
                ClassRuleTest.list.add(str);
                logger.warn("время выполнения теста {} - {} мс",description.getDisplayName(),diff);


            }
        };

    }

}
