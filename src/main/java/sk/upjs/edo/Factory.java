package sk.upjs.edo;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.upjs.edo.meeting.MeetingDao;
import sk.upjs.edo.meeting.MysqlMeetingDao;
import sk.upjs.edo.users.MysqlUserDao;
import sk.upjs.edo.users.UserDao;

public enum Factory {
    INSTANCE;

    private UserDao userDao;
    private JdbcOperations jdbcOperations;
    private MeetingDao meetingDao;

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

    public UserDao getUserDao() {
        if (userDao == null) {

            userDao = new MysqlUserDao(getJdbcOperations());
        }

        return userDao;
    }

    public MeetingDao getMeetingDao() {
        if (meetingDao == null) {
            meetingDao = new MysqlMeetingDao(getJdbcOperations());
        }
        return meetingDao;
    }
}
