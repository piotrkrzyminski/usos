package pl.usos.repository.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    private static final String EMAIL = "admin@test.com";

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private UserRepository userRepository;

    @Before
    public void setup() {
        createData();
    }

    /**
     * Get all roles attached to user by its unique email address.
     * This should return list of all roles attached to user.
     */
    @Test
    public void testGetAllRolesFromUser() {
        List<RoleModel> roles = roleRepository.findRolesByUserEmail(EMAIL);

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(ADMIN_ROLE, roles.get(0).getName());
    }

    /**
     * Get all roles attached to user by its unique email address when user not exists.
     * This should return empty list of roles.
     */
    @Test
    public void testGetAllRolesFromUserWhenNotExists() {
        List<RoleModel> roles = roleRepository.findRolesByUserEmail("qwe@frgr.pl");

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    private void createData() {
        UserModel admin = new UserModel();
        RoleModel adminRole = new RoleModel();

        adminRole.setName(ADMIN_ROLE);
        adminRole.setUsers(Arrays.asList(admin));

        admin.setRoles(Arrays.asList(adminRole));
        admin.setEmail(EMAIL);
        admin.setPassword("qwerty");

        userRepository.save(admin);
        roleRepository.save(adminRole);
    }
}