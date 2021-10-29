package de.ronnyfriedland.adr;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Integrationtest for {@link ExportAdrMojo}
 *
 * @author ronnyfriedland
 */
public class ExportAdrMojoTest {

    private File testDir;

    @BeforeEach
    public void setUp() throws IOException {
        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mojo-test-export" );
    }

    @Test
    public void test() throws Exception {
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "de.ronnyfriedland.maven:adr-maven-plugin:export" );

        verifier.assertFilePresent("html/adr.html");
        verifier.assertFilePresent("html/index.html");
    }
}
