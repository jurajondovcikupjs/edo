package sk.upjs.edo.settings;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import sk.upjs.edo.meeting.Meeting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlSettingsDao implements SettingsDao {

    private final JdbcOperations jdbcOperations;
    private final RowMapper<Meeting> rowMapper =
            (rs, rowNum) -> Meeting.fromResultSet(rs, "");

    public MysqlSettingsDao(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Settings initialize(long userId, Settings settings) throws SQLException {
        String sql = """
            INSERT INTO app_settings (user_id, language_id, theme_mode_id, accent_color_id, notifications_enabled)
            VALUES (?, (SELECT language_id FROM enum_languages WHERE code = ?),
                    (SELECT theme_mode_id FROM enum_theme_modes WHERE code = ?),
                    (SELECT accent_color_id FROM enum_accent_colors WHERE code = ?), ?)
           \s""";

        this.jdbcOperations.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setString(2, settings.language_id());
            stmt.setString(3, settings.theme_mode_id());
            stmt.setString(4, settings.accent_color_id());
            stmt.setBoolean(5, settings.notifications_enabled());
            return stmt;
        });
        return settings;
    }

    @Override
    public Settings loadSettings(long userId) throws SQLException {
        String sql = """
            SELECT\s
                s.user_id,
                s.notifications_enabled,
                l.code AS language_code,
                t.code AS theme_mode,
                c.code AS accent_color,
                c.hex_value AS accent_hex
            FROM app_settings s
            JOIN enum_languages l ON s.language_id = l.language_id
            JOIN enum_theme_modes t ON s.theme_mode_id = t.theme_mode_id
            JOIN enum_accent_colors c ON s.accent_color_id = c.accent_color_id
            WHERE s.user_id = ?
           \s""";

        return this.jdbcOperations.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
            String languageCode = rs.getString("language_code");
            String themeMode = rs.getString("theme_mode");
            String accentColor = rs.getString("accent_color");
            String accentHex = rs.getString("accent_hex");
            boolean notificationsEnabled = rs.getBoolean("notifications_enabled");

            return new Settings(userId, languageCode, themeMode, accentColor, notificationsEnabled);
        });
    }

    @Override
    public boolean updateSettings(long userId, Settings settings) throws SQLException {
        String sql = """
        UPDATE app_settings
        SET\s
            language_id = (SELECT language_id FROM enum_languages WHERE code = ?),
            theme_mode_id = (SELECT theme_mode_id FROM enum_theme_modes WHERE code = ?),
            accent_color_id = (SELECT accent_color_id FROM enum_accent_colors WHERE code = ?),
            notifications_enabled = ?
        WHERE user_id = ?
       \s""";

        int rowsAffected = this.jdbcOperations.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, settings.language_id());
            stmt.setString(2, settings.theme_mode_id());
            stmt.setString(3, settings.accent_color_id());
            stmt.setBoolean(4, settings.notifications_enabled());
            stmt.setLong(5, userId);
            return stmt;
        });
        return rowsAffected > 0;
    }
}
