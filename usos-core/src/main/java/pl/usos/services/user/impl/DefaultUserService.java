package pl.usos.services.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.usos.repository.user.RoleModel;
import pl.usos.repository.user.RoleRepository;
import pl.usos.repository.user.UserModel;
import pl.usos.repository.user.UserRepository;
import pl.usos.services.exceptions.DuplicatedUserException;
import pl.usos.services.exceptions.UserNotExistsException;
import pl.usos.services.user.UserService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Default implementation of {@link UserService} interface.
 *
 * @author Piotr Krzyminski
 */
@Service
@Transactional
public class DefaultUserService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

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
        UserModel result = getUserRepository().findUserByEmail(userModel.getEmail());
        if (result != null) {
            if (userModel.getEmail().equals(result.getEmail()) && getPasswordEncoder().matches(userModel.getPassword(), result.getPassword())) {
                return result;
            }
        }

        LOG.debug("Email or password are incorrect");
        throw new BadCredentialsException("Email or password are incorrect");
    }

    @Override
    public List<RoleModel> getRolesForUser(UserModel userModel) {

        notNull(userModel, "User cannot be null!");
        notBlank(userModel.getEmail(), "Email cannot be empty!");

        return getRoleRepository().findRolesByUserEmail(userModel.getEmail());
    }

    @Override
    public void saveUser(UserModel userModel) throws DuplicatedUserException {

        notNull(userModel, "User cannot be null!");
        notBlank(userModel.getEmail(), "Email cannot be blank!");

        UserModel user = getUserRepository().findUserByEmail(userModel.getEmail());
        if (user != null) {
            LOG.error(format("User with the following email [%s] already exists!", userModel.getEmail()));
            throw new DuplicatedUserException(format("User with the following email [%s] already exists!", userModel.getEmail()));
        }

        LOG.debug(format("Saving user with email [%s] to database", userModel.getEmail()));
        getUserRepository().save(userModel);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Resource
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    @Resource
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Resource
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
