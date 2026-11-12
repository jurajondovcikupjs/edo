package sk.upjs.edo;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.upjs.edo.users.UserDao;

public enum Factory {
    INSTANCE;

    private UserDao userDao;
    private JdbcOperations jdbcOperations;

    public JdbcOperations getJdbcOperations() {
        if (this.jdbcOperations != null) {
            return this.jdbcOperations;
        }

        var dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getProperty("DB_URL", "jdbc:mysql://localhost:3307/edodb"));
        dataSource.setUser(System.getProperty("DB_USER", "edo"));
        dataSource.setPassword(System.getProperty("DB_PASSWORD", "edoproject"));

        this.jdbcOperations = new JdbcTemplate(dataSource);
        return this.jdbcOperations;
    }
}
