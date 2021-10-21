package de.ronnyfriedland.adr;

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
 * Re-index adrs
 *
 * @author ronnyfriedland
 */
@Mojo(name = "index")
public class IndexMojo extends AbstractMojo {

    @Parameter(property = "templateSourcePath", defaultValue = "${project.basedir}/src/main/resources/adr")
    private String templateSourcePath;

    @Parameter(property = "targetPath", defaultValue = "${project.build.directory}/adr")
    private String targetPath;

    @Parameter(property = "templateIndexFile", defaultValue = "index.md")
    private String templateIndexFile;


    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        try {
            Files.createDirectories(Path.of(targetPath));
        } catch (final IOException e) {
            throw new MojoExecutionException("Error creating target directory", e);
        }

        try {
            new IndexProcessor(templateSourcePath).createIndexTemplate(templateIndexFile, targetPath, templateIndexFile);
        } catch (final TemplateProcessorException e) {
            throw new MojoExecutionException("Error processing templates", e);
        }
    }
}
