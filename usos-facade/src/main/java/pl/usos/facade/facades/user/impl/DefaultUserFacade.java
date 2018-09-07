package pl.usos.facade.facades.user.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import pl.usos.facade.facades.user.UserFacade;
import pl.usos.repository.user.UserModel;
import pl.usos.services.user.UserService;

import javax.annotation.Resource;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * Default implementation of {@link UserFacade}
 *
 * @author Piotr Krzyminski
 */
@Component
public class DefaultUserFacade implements UserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserFacade.class);

    private UserService userService;

    @Override
    public void login(final String email, final String password) {

        if(StringUtils.isBlank(email) || StringUtils.isBlank(password))
            throw new BadCredentialsException("Bad credentials");

        LOG.debug(format("Performing user authentication with email [%s]", email));

        getUserService().authenticate(createUserInstance(email, password));
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
}
