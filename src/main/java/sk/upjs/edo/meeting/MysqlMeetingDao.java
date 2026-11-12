package sk.upjs.edo.meeting;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.upjs.edo.exceptions.BadRequestException;
import sk.upjs.edo.exceptions.NotFoundException;

import java.sql.Date;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class MysqlMeetingDao implements MeetingDao {

    private final JdbcOperations jdbcOperations;
    private final RowMapper<Meeting> rowMapper =
            (rs, rowNum) -> Meeting.fromResultSet(rs, "");

    public MysqlMeetingDao(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Meeting create(Meeting meeting) throws NotFoundException, BadRequestException {
        if (meeting == null) {
            throw new BadRequestException("Meeting cannot be null");
        }

        if (meeting.meeting_id() != null && !meeting.meeting_id().equals(0L)) {
            throw new BadRequestException("Created meeting cannot have meeting_id");
        }

        var query = """
                INSERT INTO meetings
                 (meeting_id, title, duration_minutes, search_window_start, search_window_end, created_by, created_at)
                 VALUES
                 (?,?,?,?,?,?,?)
               """;

        var keyHolder = new GeneratedKeyHolder();

        this.jdbcOperations.update(conn -> {
            var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, meeting.meeting_id() != null ? meeting.meeting_id() : 0L);
            ps.setString(2, meeting.title());
            ps.setInt(3, meeting.duration_minutes());
            ps.setDate(4, Date.valueOf(meeting.search_window_start()));
            ps.setDate(5, Date.valueOf(meeting.search_window_end()));
            ps.setLong(6, meeting.created_by());
            ps.setDate(7, Date.valueOf(meeting.created_at()));
            return ps;
        }, keyHolder);

        Long newMeetingId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return this.findById(newMeetingId);
    }

    @Override
    public Meeting update(Meeting meeting) throws BadRequestException, NotFoundException {
        if (meeting == null) {
            throw new BadRequestException("Updated meeting cannot be null");
        }

        if (meeting.meeting_id() == null || meeting.meeting_id().equals(0L)) {
            throw new BadRequestException("Updated meeting must have an user_id");
        }

        var query = """
                UPDATE meetings
                 SET title = ?,
                     duration_minutes = ?,
                     search_window_start = ?,
                     search_window_end = ?,
                     created_by = ?,
                     created_at = ?
                 WHERE meeting_id = ?
                """;
        this.jdbcOperations.update(
                query,
                meeting.title(),
                meeting.duration_minutes(),
                Date.valueOf(meeting.search_window_start()),
                Date.valueOf(meeting.search_window_end()),
                meeting.created_by(),
                Date.valueOf(meeting.created_at()),
                meeting.meeting_id()
        );

        return this.findById(meeting.meeting_id());
    }

    @Override
    public Meeting findById(Long id) throws NotFoundException {
        var query = """
                SELECT meeting_id, title, duration_minutes, search_window_start, search_window_end, created_by, created_at
                 FROM meetings
                 WHERE meeting_id = ?
               """;

        try {
            return this.jdbcOperations
                    .queryForObject(query, rowMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Meeting with id " + id + " not found");
        }
    }

    @Override
    public List<Meeting> findAll() {
        var query = """
                SELECT meeting_id, title, duration_minutes, search_window_start, search_window_end, created_by, created_at
                 FROM meetings
               """;

        return this.jdbcOperations.query(query, rowMapper);
    }

    @Override
    public int deleteById(Long id) throws NotFoundException {
        var query = """
                DELETE FROM meetings
                 WHERE meeting_id = ?
               """;

        int rowsAffected = this.jdbcOperations.update(query, id);
        if (rowsAffected == 0) {
            throw new NotFoundException("Meeting with id " + id + " not found");
        }
        return rowsAffected;
    }
}
