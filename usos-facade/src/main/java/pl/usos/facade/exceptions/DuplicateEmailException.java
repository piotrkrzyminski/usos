package pl.usos.facade.exceptions;

/**
 * Exception thrown when trying to register user with duplicated email.
 *
 * @author Piotr Krzyminski
 */
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException() {
        super();
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
