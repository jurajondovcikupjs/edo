package sk.upjs.edo.users;

import sk.upjs.edo.exceptions.BadRequestException;
import sk.upjs.edo.exceptions.NotFoundException;

import java.util.List;

public interface UserDao {

    User create(User user) throws NotFoundException, BadRequestException;

    User update(User user) throws BadRequestException, NotFoundException;

    User findById(Long id) throws NotFoundException;

    User findByEmail(String email) throws NotFoundException;

    List<User> findAll();

    void deactivate(Long id);

    User reactivate(Long id) throws NotFoundException;

    boolean authenticate(String username, String password);
}
