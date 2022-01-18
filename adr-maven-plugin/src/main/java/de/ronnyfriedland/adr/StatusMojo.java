package de.ronnyfriedland.adr;

import de.ronnyfriedland.adr.status.StatusProcessor;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Status of adrs
 *
 * @author ronnyfriedland
 */
@Mojo(name = "status")
public class StatusMojo extends AbstractMojo {

    @Parameter(property = "targetPath", defaultValue = "${project.build.directory}/adr")
    private String targetPath;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        try {
            new StatusProcessor().processStatus(targetPath);
        } catch (final TemplateProcessorException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Error processing adrs", e);
        }
    }
}
