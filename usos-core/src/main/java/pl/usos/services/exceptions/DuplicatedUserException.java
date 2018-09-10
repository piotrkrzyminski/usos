package pl.usos.services.exceptions;

/**
 * Exception thrown when trying to save user that email already exists in datasource.
 *
 * @author Piotr Krzyminski
 */
public class DuplicatedUserException extends Exception {

    public DuplicatedUserException() {
        super();
    }

    public DuplicatedUserException(String message) {
        super(message);
    }
}
