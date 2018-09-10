package pl.usos.services.user.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.usos.repository.user.RoleModel;
import pl.usos.repository.user.RoleRepository;
import pl.usos.repository.user.UserModel;
import pl.usos.repository.user.UserRepository;
import pl.usos.services.exceptions.DuplicatedUserException;
import pl.usos.services.exceptions.UserNotExistsException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
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

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private DefaultUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserModel user;

    @Mock
    private UserModel badUser;

    @Mock
    private RoleModel adminRole;

    @Before
    public void setup() {
        userService = new DefaultUserService();
        user = mock(UserModel.class);
        badUser = mock(UserModel.class);
        adminRole = mock(RoleModel.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);

        userService.setUserRepository(userRepository);
        userService.setRoleRepository(roleRepository);
        userService.setPasswordEncoder(passwordEncoder);
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
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

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

    /**
     * Get roles from user by email address.
     * This should return list of roles attached to user.
     */
    @Test
    public void testGetRolesForUserSuccess() {

        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(adminRole.getName()).thenReturn(ADMIN_ROLE);
        when(adminRole.getUsers()).thenReturn(Collections.singletonList(user));
        when(user.getRoles()).thenReturn(Collections.singletonList(adminRole));

        when(roleRepository.findRolesByUserEmail(EMAIL)).thenReturn(Collections.singletonList(adminRole));

        List<RoleModel> roles = userService.getRolesForUser(user);

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(ADMIN_ROLE, roles.get(0).getName());
    }

    /**
     * Get roles from user when result will be empty.
     * Should return empty list of roles.
     */
    @Test
    public void testGetRolesForUserNotFound() {

        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(adminRole.getName()).thenReturn(ADMIN_ROLE);
        when(adminRole.getUsers()).thenReturn(Collections.singletonList(user));
        when(user.getRoles()).thenReturn(Collections.singletonList(adminRole));

        when(roleRepository.findRolesByUserEmail(EMAIL)).thenReturn(Collections.emptyList());

        List<RoleModel> roles = userService.getRolesForUser(user);

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    /**
     * Try to save new user to database when email is not duplicated.
     * New user should be added.
     */
    @Test
    public void testSaveUserSuccess() throws DuplicatedUserException {

        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null); // user not found

        userService.saveUser(user);
    }

    /**
     * Try to save new user to database when email is duplicated.
     * This should throw exception {@link DuplicatedUserException}.
     */
    @Test(expected = DuplicatedUserException.class)
    public void testSaveUserDuplicated() throws DuplicatedUserException {

        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user); // user not found

        userService.saveUser(user);
    }

    /**
     * Perform saving user when parameter object is null.
     * This should throw exception {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void testSaveUserWhenNull() throws DuplicatedUserException {
        userService.saveUser(null);
    }

    /**
     * Perform saving user when parameter email of user is empty.
     * This should throw exception {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveUserWhenEmailEmpty() throws DuplicatedUserException {
        when(user.getEmail()).thenReturn("  ");
        userService.saveUser(user);
    }

}