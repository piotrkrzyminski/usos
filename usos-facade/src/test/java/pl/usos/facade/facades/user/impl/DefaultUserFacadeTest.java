package pl.usos.facade.facades.user.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import pl.usos.repository.user.UserModel;
import pl.usos.services.user.UserService;
import pl.usos.services.user.impl.DefaultUserService;

/**
 * Tests for {@link DefaultUserFacade}.
 *
 * @author Piotr Krzyminski
 */
@RunWith(SpringRunner.class)
public class DefaultUserFacadeTest {

    private static final String EMAIL = "tommy@test.pl";
    private static final String PASSWORD = "qwerty";

    private static final String BAD_EMAIL = "thief@test.pl";
    private static final String BAD_PASSWORD = "qwe123";

    private DefaultUserFacade userFacade;

    @Mock
    private UserService userService;

    @Mock
    private UserModel user;

    @Mock
    private UserModel badUser;

    @Before
    public void setup() {
        userFacade = new DefaultUserFacade();

        userService = Mockito.mock(DefaultUserService.class);
        user = Mockito.mock(UserModel.class);
        badUser = Mockito.mock(UserModel.class);

        userFacade.setUserService(userService);
    }

    /**
     * Test form email and password anthentication when user with the specified email and password exists.
     * Should finish with success.
     */
    @Test
    public void testAuthenticationSuccess() {

        Mockito.when(user.getEmail()).thenReturn(EMAIL);
        Mockito.when(user.getPassword()).thenReturn(PASSWORD);
        Mockito.when(userService.authenticate(user)).thenReturn(user);

        userFacade.login(EMAIL, PASSWORD);
    }

    /**
     * Test authentication from data from login form when password is incorrect.
     * Should throw exception.
     */
    @Test(expected = BadCredentialsException.class)
    public void testAuthenticationBadPassword() {

        Mockito.when(badUser.getEmail()).thenReturn(EMAIL);
        Mockito.when(badUser.getPassword()).thenReturn(BAD_PASSWORD);

        Mockito.when(userService.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);

        userFacade.login(EMAIL, BAD_PASSWORD);
    }

    /**
     * Test authentication from data from login form when email is incorrect.
     * Should throw exception.
     */
    @Test(expected = BadCredentialsException.class)
    public void testAuthenticationBadEmail() {

        Mockito.when(badUser.getEmail()).thenReturn(BAD_EMAIL);
        Mockito.when(badUser.getPassword()).thenReturn(PASSWORD);

        Mockito.when(userService.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);

        userFacade.login(BAD_EMAIL, PASSWORD);
    }
}