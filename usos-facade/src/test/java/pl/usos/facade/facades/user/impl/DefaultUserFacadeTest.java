package pl.usos.facade.facades.user.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import pl.usos.facade.converter.user.GrantedAuthorityConverter;
import pl.usos.facade.data.user.RegisterData;
import pl.usos.facade.exceptions.DuplicateEmailException;
import pl.usos.repository.user.RoleModel;
import pl.usos.repository.user.UserModel;
import pl.usos.services.exceptions.DuplicatedUserException;
import pl.usos.services.exceptions.UserNotExistsException;
import pl.usos.services.user.UserService;
import pl.usos.services.user.impl.DefaultUserService;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private DefaultUserFacade userFacade;

    @Mock
    private UserService userService;

    @Mock
    private GrantedAuthorityConverter grantedAuthorityConverter;

    @Mock
    private UserModel user;

    @Mock
    private UserModel badUser;

    @Mock
    private RoleModel adminRole;

    @Mock
    private RegisterData registerData;

    @Before
    public void setup() {
        userFacade = new DefaultUserFacade();

        userService = Mockito.mock(DefaultUserService.class);
        grantedAuthorityConverter = Mockito.mock(GrantedAuthorityConverter.class);

        user = Mockito.mock(UserModel.class);
        badUser = Mockito.mock(UserModel.class);
        adminRole = Mockito.mock(RoleModel.class);

        registerData = Mockito.mock(RegisterData.class);

        userFacade.setUserService(userService);
        userFacade.setGrantedAuthorityConverter(grantedAuthorityConverter);
    }

    /**
     * Test form email and password authentication when user with the specified email and password exists.
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

    /**
     * Test get all authorities from user.
     * Should return list of {@link org.springframework.security.core.GrantedAuthority} objects.
     */
    @Test
    public void testGetAuthoritiesSuccess() throws UserNotExistsException {
        Mockito.when(adminRole.getName()).thenReturn(ROLE_ADMIN);
        Mockito.when(user.getEmail()).thenReturn(EMAIL);
        Mockito.when(user.getPassword()).thenReturn(PASSWORD);

        Mockito.when(adminRole.getUsers()).thenReturn(Collections.singletonList(user));
        Mockito.when(user.getRoles()).thenReturn(Collections.singletonList(adminRole));

        GrantedAuthority authority = new SimpleGrantedAuthority(ROLE_ADMIN);

        Mockito.when(userService.getUserForEmail(EMAIL)).thenReturn(user);
        Mockito.when(userService.getRolesForUser(user)).thenReturn(Collections.singletonList(adminRole));
        Mockito.when(grantedAuthorityConverter.convertAll(Mockito.any())).thenReturn(Collections.singletonList(authority));

        List<GrantedAuthority> grantedAuthorities = userFacade.getAuthoritiesFromUser(EMAIL);

        assertNotNull(grantedAuthorities);
        assertEquals(1, grantedAuthorities.size());
    }

    /**
     * Get authorities from user when email parameter is empty.
     * Should throw exception {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAuthoritiesEmailEmpty() {
        userFacade.getAuthoritiesFromUser("  ");
    }

    /**
     * Test registration of new user when all data is properly assigned.
     * This should register new user to database.
     */
    @Test
    public void testRegisterNewUser() throws DuplicateEmailException, DuplicatedUserException {

        Mockito.when(registerData.getEmail()).thenReturn(EMAIL);
        Mockito.when(registerData.getPassword()).thenReturn(PASSWORD);

        userFacade.register(registerData);

        Mockito.verify(userService, Mockito.times(1)).saveUser(Mockito.any());
    }

    /**
     * Register new user when user's email is already assigned to other user.
     * This should throw exception {@link DuplicateEmailException}.
     */
    @Test(expected = DuplicateEmailException.class)
    public void testRegisterNewUserDuplicate() throws DuplicateEmailException, DuplicatedUserException {

        Mockito.when(registerData.getEmail()).thenReturn(EMAIL);
        Mockito.when(registerData.getPassword()).thenReturn(PASSWORD);

        Mockito.doThrow(new DuplicatedUserException()).when(userService).saveUser(Mockito.any()); // duplicated user found.

        userFacade.register(registerData);
    }

    /**
     * Try to register new user when passed email is empty.
     * Should throw exception {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWhenEmailIsEmpty() throws DuplicateEmailException {

        Mockito.when(registerData.getEmail()).thenReturn("   ");
        Mockito.when(registerData.getPassword()).thenReturn(PASSWORD);

        userFacade.register(registerData);
    }

    /**
     * Try to register new user when passed password is empty.
     * Should throw exception {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWhenPasswordIsEmpty() throws DuplicateEmailException {

        Mockito.when(registerData.getEmail()).thenReturn(EMAIL);
        Mockito.when(registerData.getPassword()).thenReturn("  ");

        userFacade.register(registerData);
    }

    /**
     * Try to register new user when register data is null.
     * Should throw exception {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void testRegisterDataNull() throws DuplicateEmailException {
        userFacade.register(null);
    }
}