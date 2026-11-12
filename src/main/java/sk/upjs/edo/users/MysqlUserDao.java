package sk.upjs.edo.users;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.upjs.edo.exceptions.BadRequestException;
import sk.upjs.edo.exceptions.NotFoundException;

import java.sql.Date;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MysqlUserDao implements UserDao {

    private final JdbcOperations jdbcOperations;
    private final RowMapper<User> rowMapper =
            (rs, rowNum) -> User.fromResultSet(rs, "");

    public MysqlUserDao(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User create(User user) throws NotFoundException, BadRequestException {
        if (user == null) {
            throw new BadRequestException("User cannot be null");
        }

        if (user.user_id() != null && !user.user_id().equals(0L)) {
            throw new BadRequestException("Created user cannot have user_id");
        }

        var query = """
                INSERT INTO users
                 (name, email, password_hash, created_at, active)
                 VALUES
                 (?,?,?,?,?)
               """;

        var keyHolder = new GeneratedKeyHolder();

        this.jdbcOperations.update(conn -> {
            var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setString(3, user.passwordHash());
            ps.setDate(4, Date.valueOf(user.createdAt()));
            ps.setBoolean(5, user.active());
            return ps;
        }, keyHolder);

        Long newUserId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return this.findById(newUserId);
    }

    @Override
    public User update(User user) throws BadRequestException, NotFoundException {
        if (user == null) {
            throw new BadRequestException("Updated user cannot be null");
        }

        if (!user.active()) {
            throw new BadRequestException("Cannot update inactive user");
        }

        if (user.user_id() == null || user.user_id().equals(0L)) {
            throw new BadRequestException("Updated user must have an user_id");
        }

        if (!findById(user.user_id()).active()) {
            throw new BadRequestException("Cannot update inactive user");
        }

        var query = """
                UPDATE users
                 SET name = ?,
                     email = ?,
                     password_hash = ?,
                     created_at = ?,
                     active = ?
                 WHERE user_id = ?
                """;
        this.jdbcOperations.update(
                query,
                user.name(),
                user.email(),
                user.passwordHash(),
                Date.valueOf(user.createdAt()),
                true,
                user.user_id()
        );

        return this.findById(user.user_id());
    }

    @Override
    public User findById(Long user_id) throws NotFoundException {
        var query = """
                SELECT user_id, name, email, password_hash, created_at, active
                 FROM users\s
                 WHERE user_id = ?\s
               \s""";
        try {
            return this.jdbcOperations
                    .queryForObject(query, rowMapper, user_id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException(
                    "User with user_id %d not found"
                            .formatted(user_id)
            );
        }
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        var query = """
                SELECT user_id, name, email, password_hash, created_at, active
                 FROM users\s
                 WHERE email = ?\s
               \s""";
        try {
            return this.jdbcOperations
                    .queryForObject(query, rowMapper, email);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException(
                    "User with email %s not found"
                            .formatted(email)
            );
        }
    }

    @Override
    public List<User> findAll() {
        var query = """
                SELECT user_id, name, email, password_hash, created_at, active
                 FROM users\s
               \s""";

        return Collections.unmodifiableList(this.jdbcOperations.query(query, rowMapper));
    }

    @Override
    public void deactivate(Long user_id) {
        jdbcOperations.update("UPDATE users SET active = false WHERE user_id = ?", user_id);
    }

    @Override
    public User reactivate(Long user_id) throws NotFoundException {
        jdbcOperations.update("UPDATE users SET active = true WHERE user_id = ?", user_id);
        return findById(user_id);
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            User user = findByEmail(username);
            return user.passwordHash().equals(password);
        } catch (NotFoundException e) {
            return false;
        }
    }
}

