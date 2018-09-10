package pl.usos.repository.user;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "role")
public class RoleModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Collection<UserModel> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<UserModel> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserModel> users) {
        this.users = users;
    }
}
