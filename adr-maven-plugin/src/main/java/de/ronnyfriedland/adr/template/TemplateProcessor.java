package de.ronnyfriedland.adr.template;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * Abstract base for template processing related operations.
 *
 * @author ronnyfriedland
 */
public abstract class TemplateProcessor {

    /**
     * Template processing configuration object
     */
    protected final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

    /**
     * Creates a new instance of {@link TemplateProcessor}
     *
     * @param path       the base path for the templates
     */
    protected TemplateProcessor(final String path) {
        try {
            cfg.setTemplateLoader(new FileTemplateLoader(new File(path)));
        } catch (IOException e) {
            throw new IllegalStateException("Error initializing template engine", e);
        }
    }

}
