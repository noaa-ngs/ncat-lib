package gov.noaa.ngs.transform;

/**
 * Models exceptions that are specific to this package
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/10/2011

 */
public class CTException extends Exception {
    private static final long serialVersionUID = 2816324383242480018L;

    /**
     * Constructs a new Exception
     */
    public CTException() {
        super();
    }

    /**
     * Constructs a new Exception with the specified detail message
     * @param message String
     */
    public CTException(String message) {
        super(message);
    }
}
