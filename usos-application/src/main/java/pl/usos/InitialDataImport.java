package pl.usos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.usos.repository.user.RoleModel;
import pl.usos.repository.user.RoleRepository;
import pl.usos.repository.user.UserModel;
import pl.usos.repository.user.UserRepository;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * This class is used to save to database example data when application is launched.
 *
 * @author Piotr Krzyminski
 */
@Component
public class InitialDataImport implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(InitialDataImport.class);

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private static final String USER_ROLE = "ROLE_USER";

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOG.debug("Creating initial data");

        createUsers();
    }

    private void createUsers() {
        UserModel admin = new UserModel();
        admin.setEmail("admin@test.com");
        admin.setPassword(getPasswordEncoder().encode("qwerty"));

        RoleModel adminRole = new RoleModel();
        adminRole.setName(ADMIN_ROLE);
        adminRole.setUsers(Arrays.asList(admin));

        admin.setRoles(Arrays.asList(adminRole));

        getRoleRepository().save(adminRole);
        getUserRepository().save(admin);

        UserModel user = new UserModel();
        user.setEmail("user@test.com");
        user.setPassword(getPasswordEncoder().encode("qwerty"));

        RoleModel userRole = new RoleModel();
        userRole.setName(USER_ROLE);
        userRole.setUsers(Arrays.asList(user));

        user.setRoles(Arrays.asList(userRole));

        getRoleRepository().save(userRole);
        getUserRepository().save(user);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Resource
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    @Resource
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Resource
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
