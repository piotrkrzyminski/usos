package pl.usos.services.user.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;
import pl.usos.repository.user.UserModel;
import pl.usos.repository.user.UserRepository;
import pl.usos.services.exceptions.UserNotExistsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultUserService}.
 *
 * @author Piotr Krzyminski
 */
@RunWith(SpringRunner.class)
public class DefaultUserServiceTest {

    private static final String EMAIL = "tommy@test.pl";
    private static final String PASSWORD = "qwerty";
    private static final String EMPTY_EMAIL = "  ";

    private DefaultUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserModel user;

    @Mock
    private UserModel badUser;

    @Before
    public void setup() {
        userService = new DefaultUserService();
        user = mock(UserModel.class);
        badUser = mock(UserModel.class);
        userRepository = mock(UserRepository.class);

        userService.setUserRepository(userRepository);
    }

    /**
     * Get user by email address when user with this address exists in datasource.
     * This should return user object with the specified email.
     */
    @Test
    public void testGetUserForEmailWhenExists() throws UserNotExistsException {

        when(user.getEmail()).thenReturn(EMAIL);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(user);

        UserModel result = userService.getUserForEmail(EMAIL);
        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
    }

    /**
     * Get user by email address when parameter is empty.
     * This should throw exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUserForEmailWhenParameterIsEmpty() throws UserNotExistsException {
        userService.getUserForEmail(EMPTY_EMAIL);
    }

    /**
     * Get user by email address when parameter is null.
     * This should throw exception.
     */
    @Test(expected = NullPointerException.class)
    public void testGetUserForEmailWhenIsNull() throws UserNotExistsException {
        userService.getUserForEmail(null);
    }

    /**
     * Authenticate user when data is properly filled.
     * Service should return user object.
     */
    @Test
    public void testAuthenticateSuccess() throws AuthenticationException {

        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(user);

        UserModel result = userService.authenticate(user);

        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
    }

    /**
     * Try to authenticate user when password is incorrect.
     * This should throw exception.
     */
    @Test(expected = BadCredentialsException.class)
    public void testAuthenticateWrongPassword() throws AuthenticationException {

        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(user);

        when(badUser.getEmail()).thenReturn(EMAIL);
        when(badUser.getPassword()).thenReturn("badPassword");

        userService.authenticate(badUser);
    }

}