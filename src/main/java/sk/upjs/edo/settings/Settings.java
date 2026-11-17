package sk.upjs.edo.settings;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Settings(
    long user_id,
    String language_id,
    String theme_mode_id,
    String accent_color_id,
    boolean notifications_enabled
) {
    public Settings(long user_id, String language_id, String theme_mode_id, String accent_color_id, boolean notifications_enabled) {
        this.user_id = user_id;
        this.language_id = language_id;
        this.theme_mode_id = theme_mode_id;
        this.accent_color_id = accent_color_id;
        this.notifications_enabled = notifications_enabled;
    }

    public static Settings fromResultSet(ResultSet resultSet) throws SQLException {
        long user_id = resultSet.getLong("user_id");
        String language_id = resultSet.getString("language_id");
        String theme_mode_id = resultSet.getString("theme_mode_id");
        String accent_color_id = resultSet.getString("accent_color_id");
        boolean notifications_enabled = resultSet.getBoolean("notifications_enabled");

        if (user_id == 0) {
            return null;
        }

        return new Settings(user_id, language_id, theme_mode_id, accent_color_id, notifications_enabled);
    }
}
