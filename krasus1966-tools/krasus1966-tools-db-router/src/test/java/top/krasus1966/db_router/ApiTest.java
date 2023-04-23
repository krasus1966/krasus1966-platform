package top.krasus1966.db_router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Krasus1966
 * @date 2023/3/13 15:23
 **/
@SpringBootTest
public class ApiTest {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        jdbcTemplate.execute("insert into user values (1,'名字')");
    }
}
