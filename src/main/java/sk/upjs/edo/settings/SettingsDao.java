package sk.upjs.edo.settings;

import java.sql.Connection;
import java.sql.SQLException;

public interface SettingsDao {

    Settings initialize(long userId, Settings settings) throws SQLException;

    Settings loadSettings(long userId) throws SQLException;

    boolean updateSettings(long userId, Settings settings) throws SQLException;
}
