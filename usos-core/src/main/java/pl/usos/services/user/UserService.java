package pl.usos.services.user;

import org.springframework.security.core.AuthenticationException;
import pl.usos.repository.user.RoleModel;
import pl.usos.repository.user.UserModel;
import pl.usos.services.exceptions.DuplicatedUserException;
import pl.usos.services.exceptions.UserNotExistsException;

import java.util.List;

public interface UserService {

    /**
     * Get user by using unique email address.
     *
     * @param email unique address belonging to user.
     * @return user object with the specified email address.
     * @throws UserNotExistsException user with email not exists in datasource.
     */
    UserModel getUserForEmail(final String email) throws UserNotExistsException;

    /**
     * Perform user authentication by checking user name and password in database.
     *
     * @param userModel user to authenticate.
     * @return user model if user will be authenticated.
     */
    UserModel authenticate(UserModel userModel) throws AuthenticationException;

    /**
     * Get roles form user by it's unique email address.
     *
     * @param userModel user which roles will be found.
     * @return list of roles.
     */
    List<RoleModel> getRolesForUser(UserModel userModel);

    /**
     * Save user to datasource. If email of new user already exists in datasource then throw exception.
     *
     * @param userModel user to save.
     * @throws DuplicatedUserException thrown when trying to save user which email already exists.
     */
    void saveUser(final UserModel userModel) throws DuplicatedUserException;
}
