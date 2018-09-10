package pl.usos.facade.facades.user.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.usos.facade.converter.user.GrantedAuthorityConverter;
import pl.usos.facade.data.user.RegisterData;
import pl.usos.facade.exceptions.DuplicateEmailException;
import pl.usos.facade.facades.user.UserFacade;
import pl.usos.repository.user.UserModel;
import pl.usos.services.exceptions.DuplicatedUserException;
import pl.usos.services.exceptions.UserNotExistsException;
import pl.usos.services.user.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Default implementation of {@link UserFacade}
 *
 * @author Piotr Krzyminski
 */
@Component
public class DefaultUserFacade implements UserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserFacade.class);

    private UserService userService;

    private GrantedAuthorityConverter grantedAuthorityConverter;

    @Override
    public void login(final String email, final String password) {

        if (StringUtils.isBlank(email) || StringUtils.isBlank(password))
            throw new BadCredentialsException("Bad credentials");

        LOG.debug(format("Performing user authentication with email [%s]", email));

        getUserService().authenticate(createUserInstance(email, password));
    }

    @Override
    public List<GrantedAuthority> getAuthoritiesFromUser(final String email) {

        notBlank(email, "Email cannot be empty!");

        List<GrantedAuthority> authorities = new ArrayList<>();
        try {
            UserModel user = userService.getUserForEmail(email);
            authorities = getGrantedAuthorityConverter().convertAll(userService.getRolesForUser(user));
        } catch (UserNotExistsException ignored) {
        }

        return authorities;
    }

    @Override
    public void register(RegisterData registerData) throws DuplicateEmailException, IllegalArgumentException {

        notNull(registerData, "Register data cannot be null!");
        notBlank(registerData.getEmail(), "Email cannot be empty!");
        notBlank(registerData.getPassword(), "Password cannot be empty!");

        UserModel newUser = new UserModel();
        newUser.setEmail(registerData.getEmail());
        newUser.setPassword(registerData.getPassword());

        try {
            userService.saveUser(newUser);
        } catch (DuplicatedUserException e) {
            throw new DuplicateEmailException(format("User with email [%s] already exists", registerData.getEmail()));
        }
    }

    private UserModel createUserInstance(final String email, final String password) {
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public UserService getUserService() {
        return userService;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public GrantedAuthorityConverter getGrantedAuthorityConverter() {
        return grantedAuthorityConverter;
    }

    @Resource
    public void setGrantedAuthorityConverter(GrantedAuthorityConverter grantedAuthorityConverter) {
        this.grantedAuthorityConverter = grantedAuthorityConverter;
    }
}
