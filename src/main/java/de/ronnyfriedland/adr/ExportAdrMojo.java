package de.ronnyfriedland.adr;

import de.ronnyfriedland.adr.enums.ExportType;
import de.ronnyfriedland.adr.export.ExportProcessor;
import de.ronnyfriedland.adr.export.exception.ExportProcessorException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Export adr mojo
 *
 * @author ronnyfriedland
 */
@Mojo(name = "export")
public class ExportAdrMojo extends AbstractMojo {

    @Parameter(property = "exportType", defaultValue = "html")
    private ExportType exportType;

    @Parameter(property = "targetPath", defaultValue = "${project.build.directory}/adr")
    private String targetPath;

    @Parameter(property = "templateIndexFile", defaultValue = "index.md")
    private String templateIndexFile;


    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        try {
            Files.createDirectories(Path.of(targetPath, exportType.name()));
        } catch (final IOException e) {
            throw new MojoExecutionException("Error creating target directory", e);
        }

        try {
            new ExportProcessor().exportAdr(targetPath, exportType.name());
        } catch (final ExportProcessorException e) {
            throw new MojoExecutionException("Error exporting adr", e);
        }

    }
}