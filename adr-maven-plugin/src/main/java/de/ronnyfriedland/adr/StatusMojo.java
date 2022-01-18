package de.ronnyfriedland.adr;

import de.ronnyfriedland.adr.status.StatusProcessor;
import de.ronnyfriedland.adr.template.enums.StatusType;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.math.BigInteger;
import java.util.Map;

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
            StatusProcessor statusProcessor = new StatusProcessor();

            Map<StatusType, BigInteger> status = statusProcessor.processStatus(targetPath);
            long brokenLinks = statusProcessor.processBrokenLinks(targetPath);

            getLog().info("Status of available ADRs:");
            getLog().info("- total: " + status.values().stream().reduce(BigInteger.ZERO, BigInteger::add));
            status.entrySet().stream().map(k -> "- " + k.getKey() + ": " + k.getValue()).forEach(v -> getLog().info(v));
            getLog().info("- broken links: " + brokenLinks);

        } catch (final TemplateProcessorException e) {
            throw new MojoExecutionException("Error processing adrs", e);
        }
    }
}
