package sk.upjs.edo.meeting;

import sk.upjs.edo.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record Meeting(
        Long meeting_id,
        String title,
        int duration,
        LocalDate search_window_start,
        LocalDate search_window_end,
        int created_by,
        LocalDate created_at
) {
    public Meeting(Long meeting_id, String title, int duration, LocalDate search_window_start, LocalDate search_window_end, int created_by, LocalDate created_at) {
        this.meeting_id = meeting_id;
        this.title = title;
        this.duration = duration;
        this.search_window_start = search_window_start;
        this.search_window_end = search_window_end;
        this.created_by = created_by;
        this.created_at = created_at;
    }

    public static Meeting fromResultSet(ResultSet rs, String prefix) throws SQLException {
        var id = rs.getLong(prefix + "meeting_id");
        if (rs.wasNull()) {
            return null;
        }

        return new Meeting(
                id,
                rs.getString(prefix + "title"),
                rs.getInt(prefix + "duration"),
                rs.getDate(prefix + "search_window_start").toLocalDate(),
                rs.getDate(prefix + "search_window_end").toLocalDate(),
                rs.getInt(prefix + "created_by"),
                rs.getDate(prefix + "created_at").toLocalDate()
        );
    }

    public int duration_minutes() {
        return 0;
    }
}
