package de.ronnyfriedland.adr.template.exception;

/**
 * Indicates exception concerning template processing
 *
 * @author ronnyfriedland
 */
public class TemplateProcessorException extends Exception {

    /**
     * Creates a new instance of {@link TemplateProcessorException}
     *
     * @param message the exception message
     * @param cause   the exception cause
     */
    public TemplateProcessorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
