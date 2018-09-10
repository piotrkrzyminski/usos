package pl.usos.facade.facades.user;

import org.springframework.security.core.GrantedAuthority;
import pl.usos.facade.data.user.RegisterData;
import pl.usos.facade.exceptions.DuplicateEmailException;

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

    /**
     * Register new user to system.
     *
     * @param registerData data to save.
     * @throws DuplicateEmailException  if user email is not unique.
     * @throws IllegalArgumentException if required data is missing.
     */
    void register(final RegisterData registerData) throws DuplicateEmailException, IllegalArgumentException;
}
