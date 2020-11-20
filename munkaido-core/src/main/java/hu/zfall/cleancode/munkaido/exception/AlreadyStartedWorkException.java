package hu.zfall.cleancode.munkaido.exception;

public class AlreadyStartedWorkException extends MunkaidoException {

    /**  */
    private static final long serialVersionUID = 3678196930518523080L;

    public AlreadyStartedWorkException(String msg) {
        super(msg);
    }
}
