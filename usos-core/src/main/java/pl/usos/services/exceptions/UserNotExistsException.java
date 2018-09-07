package pl.usos.services.exceptions;

/**
 * Exception thrown when searching for user in datasource ends with empty result.
 *
 * @author Piotr Krzyminski
 */
public class UserNotExistsException extends Exception {

    public UserNotExistsException() {
        super();
    }

    public UserNotExistsException(String message) {
        super(message);
    }
}
