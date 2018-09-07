package pl.usos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.usos.facade.facades.user.UserFacade;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * Default authetication provider for login.
 *
 * @author Piotr Krzyminski
 */
@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationProvider.class);

    private UserFacade userFacade;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {

        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();

        getUserFacade().login(email, password);

        return new UsernamePasswordAuthenticationToken(email, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }



    public UserFacade getUserFacade() {
        return userFacade;
    }

    @Resource
    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Resource
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}