package pl.usos.repository.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Integration test for {@link UserRepository}.
 *
 * @author Piotr Krzyminski
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private static final String EMAIL = "tommy@test.pl";
    private static final String PASSWORD = "qwerty";

    @Autowired
    private UserRepository userRepository;

    /**
     * Initialize
     */
    @Before
    public void setup() {
        createData();
    }

    /**
     * Find user by email address when he is exists in datasource.
     * This should return user object with the selected email.
     */
    @Test
    public void testFindUserByEmailSuccess() {
        UserModel result = userRepository.findUserByEmail(EMAIL);

        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
    }

    /**
     * Find user by email address when user with address not exists in datasource.
     * This should return null.
     */
    @Test
    public void testFindUserByEmailNotFound() {
        assertNull(userRepository.findUserByEmail("fake@test.pl"));
    }

    /**
     * Create test data.
     */
    private void createData() {
        UserModel user = new UserModel();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        userRepository.save(user);
    }
}