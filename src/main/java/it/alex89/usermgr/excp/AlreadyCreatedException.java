package it.alex89.usermgr.excp;

public class AlreadyCreatedException extends Exception {
    public AlreadyCreatedException() {
        super();
    }

    public AlreadyCreatedException(String message) {
        super(message);
    }

    public AlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyCreatedException(Throwable cause) {
        super(cause);
    }

    protected AlreadyCreatedException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
