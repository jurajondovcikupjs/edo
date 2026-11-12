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

        if (user.id() != null && !user.id().equals(0L)) {
            throw new BadRequestException("Created user cannot have id");
        }

        var query = """
                INSERT INTO users
                 (email, name, password_hash, created_at, active)
                 VALUES
                 (?,?,?,?,?)
               """;

        var keyHolder = new GeneratedKeyHolder();

        this.jdbcOperations.update(conn -> {
            var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.email());
            ps.setString(2, user.name());
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

        if (user.id() == null || user.id().equals(0L)) {
            throw new BadRequestException("Updated user must have an id");
        }

        if (!findById(user.id()).active()) {
            throw new BadRequestException("Cannot update inactive user");
        }

        var query = """
                UPDATE users
                 SET name = ?,
                     email = ?,
                     password_hash = ?,
                     created_at = ?,
                     active = ?
                 WHERE id = ?
                """;
        this.jdbcOperations.update(
                query,
                user.name(),
                user.email(),
                user.passwordHash(),
                Date.valueOf(user.createdAt()),
                user.active(),
                user.id()
        );

        return this.findById(user.id());
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        var query = """
                SELECT id, email, name, password_hash, created_at, active
                 FROM users 
                 WHERE id = ? 
                """;
        try {
            return this.jdbcOperations
                    .queryForObject(query, rowMapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException(
                    "User with id %d not found"
                            .formatted(id)
            );
        }
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        var query = """
                SELECT id, email, name, password_hash, created_at, active
                 FROM users 
                 WHERE email = ? 
                """;
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
                SELECT id, email, name, password_hash, created_at, active
                 FROM users 
                """;

        return Collections.unmodifiableList(this.jdbcOperations.query(query, rowMapper));
    }

    @Override
    public void deactivate(Long id) {
        jdbcOperations.update("UPDATE users SET active = false WHERE id = ?", id);
    }

    @Override
    public User reactivate(Long id) throws NotFoundException {
        jdbcOperations.update("UPDATE users SET active = true WHERE id = ?", id);
        return findById(id);
    }
}

