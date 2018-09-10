package pl.usos.facade.converter.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.usos.facade.converter.Converter;
import pl.usos.repository.user.RoleModel;

/**
 * Converter from {@link RoleModel} to {@link GrantedAuthority}
 *
 * @author Piotr Krzyminski
 */
@Component
public class RoleConverter implements Converter<RoleModel, GrantedAuthority> {

    @Override
    public GrantedAuthority convert(RoleModel roleModel) {
        return new SimpleGrantedAuthority(roleModel.getName());
    }
}
