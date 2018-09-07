package pl.usos;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Basic item containing only unique identifier value.
 * All models should extend this class.
 *
 * @author Piotr Krzyminski
 */
@MappedSuperclass
public class ItemModel {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
