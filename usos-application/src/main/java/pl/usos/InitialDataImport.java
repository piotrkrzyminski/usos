package pl.usos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.usos.repository.user.UserModel;
import pl.usos.repository.user.UserRepository;

import javax.annotation.Resource;

/**
 * This class is used to save to database example data when application is launched.
 *
 * @author Piotr Krzyminski
 */
@Component
public class InitialDataImport implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(InitialDataImport.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOG.debug("Creating initial data");

        UserModel user = new UserModel();
        user.setEmail("timmy@test.com");
        user.setPassword(getPasswordEncoder().encode("qwerty"));

        getUserRepository().save(user);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Resource
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Resource
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
