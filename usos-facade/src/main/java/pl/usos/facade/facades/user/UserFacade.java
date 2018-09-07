package pl.usos.facade.facades.user;

public interface UserFacade {

    /**
     * Perform user authentication.
     *
     * @param email    user's name.
     * @param password user's password.
     */
    void login(final String email, final String password);
}
