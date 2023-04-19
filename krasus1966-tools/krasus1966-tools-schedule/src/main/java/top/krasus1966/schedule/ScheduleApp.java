package top.krasus1966.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Krasus1966
 * @date 2023/4/19 22:02
 **/
@SpringBootApplication
@EnableScheduling
public class ScheduleApp {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApp.class, args);
    }
}
