package pl.usos.services.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import pl.usos.repository.user.UserModel;
import pl.usos.repository.user.UserRepository;
import pl.usos.services.exceptions.UserNotExistsException;
import pl.usos.services.user.UserService;

import javax.annotation.Resource;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Default implementation of {@link UserService} interface.
 *
 * @author Piotr Krzyminski
 */
@Service
public class DefaultUserService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

    private UserRepository userRepository;

    @Override
    public UserModel getUserForEmail(final String email) throws UserNotExistsException {

        notBlank(email);

        LOG.debug(format("Searching for user with email [%s]", email));

        final UserModel user = getUserRepository().findUserByEmail(email);
        if (user == null) {
            throw new UserNotExistsException(format("User with email [%s] not exists", email));
        }

        return user;
    }

    @Override
    public UserModel authenticate(UserModel userModel) throws AuthenticationException {

        notNull(userModel, "Passed data are wrong!");

        LOG.debug("Performing user authentication");
        UserModel result = userRepository.findUserByEmail(userModel.getEmail());
        if(result != null) {
            if(result.equals(userModel)) {
                return result;
            }
        }

        throw new BadCredentialsException("Email or password are incorrect");
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Resource
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
