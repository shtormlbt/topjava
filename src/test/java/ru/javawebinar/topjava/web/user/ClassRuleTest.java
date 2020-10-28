package ru.javawebinar.topjava.web.user;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClassRuleTest implements TestRule {
        public static List<String> list = new ArrayList<>();


        @Override
        public Statement apply(final Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {


                    base.evaluate();

                    list.forEach(string -> System.out.println(string));


                }
            };
        }

}
