package hu.zfall.cleancode.munkaido.exception;

public class MunkaidoException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = -7056101877844520526L;

    public MunkaidoException() {
    }

    public MunkaidoException(String message) {
        super(message);
    }

    public MunkaidoException(Throwable t) {
        super(t);
    }

    public MunkaidoException(String message, Throwable t) {
        super(message, t);
    }

}
