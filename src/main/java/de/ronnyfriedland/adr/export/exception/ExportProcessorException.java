package de.ronnyfriedland.adr.export.exception;

/**
 * Indicates exception concerning export
 *
 * @author ronnyfriedland
 */
public class ExportProcessorException extends Exception {

    /**
     * Creates a new instance of {@link ExportProcessorException}
     *
     * @param message the exception message
     * @param cause   the exception cause
     */
    public ExportProcessorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
