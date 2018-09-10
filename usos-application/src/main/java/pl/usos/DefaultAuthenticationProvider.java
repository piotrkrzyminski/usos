package pl.usos;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.usos.facade.facades.user.UserFacade;

import javax.annotation.Resource;
import java.util.List;

/**
 * Default authentication provider for login.
 *
 * @author Piotr Krzyminski
 */
@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private UserFacade userFacade;

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {

        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final List<GrantedAuthority> authorities = userFacade.getAuthoritiesFromUser(email);

        return new UsernamePasswordAuthenticationToken(email, password, authorities);
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
}