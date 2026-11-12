package sk.upjs.edo.meeting;

import sk.upjs.edo.exceptions.BadRequestException;
import sk.upjs.edo.exceptions.NotFoundException;
import sk.upjs.edo.users.User;

import java.util.List;

public interface MeetingDao {

    Meeting create(Meeting meeting) throws NotFoundException, BadRequestException;

    Meeting update(Meeting meeting) throws BadRequestException, NotFoundException;

    Meeting findById(Long id) throws NotFoundException;

    List<Meeting> findAll();

    int deleteById(Long id) throws NotFoundException;

}
