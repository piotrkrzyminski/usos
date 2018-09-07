package pl.usos.services.user;

import org.springframework.security.core.AuthenticationException;
import pl.usos.repository.user.UserModel;
import pl.usos.services.exceptions.UserNotExistsException;

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
}