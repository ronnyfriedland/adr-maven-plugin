package de.ronnyfriedland.adr.template;

import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Template processing related operations.
 *
 * @author ronnyfriedland
 */
public class AdrProcessor extends TemplateProcessor {

    /**
     * Creates a new instance of {@link AdrProcessor}
     *
     * @param path       the base path for the templates
     * @param dateFormat the date format
     */
    public AdrProcessor(final String path, final String dateFormat) {
        super(path);
        cfg.setDateFormat(dateFormat);
    }

    /**
     * Process the adr template.
     *
     * @param templateFile the name of the template file
     * @param targetPath   the target path of the processed template
     * @param subject      the subject of the adr
     * @param status       the (initial) status of the adr
     * @param fileName     the target filename
     * @throws TemplateProcessorException error during template processing
     */
    public void processAdrTemplate(final String templateFile, final String targetPath, final String subject, final String status, final String fileName) throws TemplateProcessorException {
        try (FileWriter templateWriter = new FileWriter(new File(targetPath, fileName))) {
            Map<String, Object> templateParameters = new HashMap<>();
            templateParameters.put("subject", subject);
            templateParameters.put("status", status);
            templateParameters.put("creationDate", new Date());

            Template tpl1 = cfg.getTemplate(templateFile, StandardCharsets.UTF_8.name());
            tpl1.process(templateParameters, templateWriter);

        } catch (final IOException | TemplateException e) {
            throw new TemplateProcessorException("Error processing adr template", e);
        }
    }
}
