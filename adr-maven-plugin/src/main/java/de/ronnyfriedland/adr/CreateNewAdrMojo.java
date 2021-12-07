package de.ronnyfriedland.adr;

import de.ronnyfriedland.adr.template.enums.StatusType;
import de.ronnyfriedland.adr.template.AdrProcessor;
import de.ronnyfriedland.adr.template.IndexProcessor;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Create new adr mojo
 *
 * @author ronnyfriedland
 */
@Mojo(name = "create")
public class CreateNewAdrMojo extends AbstractMojo {

    @Parameter(property = "templateSourcePath", defaultValue = "${project.build.directory}/templates/adr")
    private String templateSourcePath;

    @Parameter(property = "templateIndexFile", defaultValue = "index-template.md")
    private String templateIndexFile;

    @Parameter(property = "templateAdrFile", defaultValue = "adr-template.md")
    private String templateAdrFile;

    @Parameter(property = "targetPath", defaultValue = "${project.build.directory}/adr")
    private String targetPath;

    @Parameter(property = "filenamePattern", defaultValue = "%03d_%s.md")
    private String filenamePattern;

    @Parameter(property = "dateFormat", defaultValue = "yyyy-MM-dd")
    private String dateFormat;

    @Parameter(property = "subject", defaultValue = "empty")
    private String subject;

    @Parameter(property = "status", defaultValue = "proposed")
    private StatusType status;

    @Parameter(property = "references")
    private String[] references;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        int count;
        if (Path.of(targetPath).toFile().exists()) {
            count = Path.of(targetPath).toFile().list().length - 1;
        } else {
            try {
                Files.createDirectories(Path.of(targetPath));
            } catch (final IOException e) {
                throw new MojoExecutionException("Error creating target directory", e);
            }
            count = 0;
        }

        try {
            new AdrProcessor(templateSourcePath, dateFormat).processAdrTemplate(templateAdrFile, targetPath, subject,
                    status.name(), String.join(",", references),
                    String.format(filenamePattern, count, subject.replaceAll("\\W", "_")));
            new IndexProcessor(templateSourcePath).processIndexTemplate(templateIndexFile, targetPath,
                    "index.md");
        } catch (final TemplateProcessorException e) {
            throw new MojoExecutionException("Error processing templates", e);
        }
    }
}
