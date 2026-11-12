package sk.upjs.edo.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record User(
        Long user_id,
        String name,
        String email,
        String passwordHash,
        LocalDate createdAt,
        boolean active
) {
    public User(Long user_id, String name, String email, String passwordHash, LocalDate createdAt, boolean active) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.active = active;
    }

    public static User fromResultSet(ResultSet rs, String prefix) throws SQLException {
        var id = rs.getLong(prefix + "user_id");
        if (rs.wasNull()) {
            return null;
        }

        return new User(
                id,
                rs.getString(prefix + "name"),
                rs.getString(prefix + "email"),
                rs.getString(prefix + "password_hash"),
                rs.getDate(prefix + "created_at").toLocalDate(),
                rs.getBoolean(prefix + "active")
        );
    }
}
