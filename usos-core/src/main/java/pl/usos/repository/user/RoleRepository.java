package pl.usos.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    @Query("select r from RoleModel as r join r.users u where u.email = :email")
    List<RoleModel> findRolesByUserEmail(@Param(value = "email") final String email);
}
