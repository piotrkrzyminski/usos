package pl.usos.repository.user;

import pl.usos.ItemModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Basic user model containing information required for sign in.
 *
 * @author Piotr Krzyminski
 */
@Entity
@Table(name = "user")
public class UserModel extends ItemModel {

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(email, userModel.email) &&
                Objects.equals(password, userModel.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
