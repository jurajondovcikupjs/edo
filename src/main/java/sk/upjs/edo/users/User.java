package sk.upjs.edo.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record User(
        Long id,
        String email,
        String name,
        String passwordHash,
        LocalDate createdAt,
        boolean active
) {
    public User(Long id, String email, String name, String passwordHash, LocalDate createdAt, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.active = active;
    }

    public static User fromResultSet(ResultSet rs, String prefix) throws SQLException {
        var id = rs.getLong(prefix + "id");
        if (rs.wasNull()) {
            return null;
        }

        return new User(
                id,
                rs.getString(prefix + "email"),
                rs.getString(prefix + "name"),
                rs.getString(prefix + "password_hash"),
                rs.getDate(prefix + "created_at").toLocalDate(),
                rs.getBoolean(prefix + "active")
        );
    }
}
