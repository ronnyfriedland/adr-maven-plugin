package de.ronnyfriedland.adr.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Template processing related operations for index files.
 *
 * @author ronnyfriedland
 */
public class IndexProcessor extends TemplateProcessor {

    /**
     * The name of the index file
     */
    public static final String INDEX_FILENAME = "index.md";

    /**
     * Creates a new instance of {@link IndexProcessor}
     *
     * @param path the base path for the templates
     */
    public IndexProcessor(final String path) {
        super(path);
    }

    /**
     * Process the index template.
     *
     * @param templateFile the name of the template file
     * @param targetPath   the target path of the processed template
     * @throws TemplateProcessorException error during template processing
     */
    public void processIndexTemplate(final String templateFile, final String targetPath)
            throws TemplateProcessorException {
        try (FileWriter indexWriter = new FileWriter(new File(targetPath, INDEX_FILENAME))) {

            Map<String, Object> templateParameters = new HashMap<>();

            try (Stream<Path> pathStream = Files.find(Path.of(targetPath), 1,
                    (p, basicFileAttributes) -> p.getFileName().toString().endsWith(".md") && !p.getFileName()
                            .toString().equalsIgnoreCase(INDEX_FILENAME))) {
                templateParameters.put("adrs",
                        pathStream.filter(Files::isRegularFile).map(Path::getFileName).collect(Collectors.toSet()));
            }

            Template tpl2 = cfg.getTemplate(templateFile, StandardCharsets.UTF_8.name());
            tpl2.process(templateParameters, indexWriter);
        } catch (final IOException | TemplateException e) {
            throw new TemplateProcessorException("Error processing index template", e);
        }
    }
}
