package pl.usos.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link UserRepository}
 */
public interface UserRepository extends JpaRepository<UserModel, Long> {

    /**
     * Gets user by unique email address.
     *
     * @param email user's unique email address.
     * @return user with the specified email or null when user not exists.
     */
    UserModel findUserByEmail(final String email);
}
