package pl.usos.facade.facades.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserFacade {

    /**
     * Perform user authentication.
     *
     * @param email    user's name.
     * @param password user's password.
     */
    void login(final String email, final String password);

    /**
     * Get list of authorities attached to user.
     *
     * @param email email of user which authorities will be searching for.
     * @return list of {@link GrantedAuthority} objects.
     */
    List<GrantedAuthority> getAuthoritiesFromUser(final String email);
}
