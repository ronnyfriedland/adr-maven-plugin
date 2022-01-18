package de.ronnyfriedland.adr;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Integrationtest for {@link IndexMojo}
 *
 * @author ronnyfriedland
 */
public class StatusMojoTest {

    private File testDir;

    @BeforeEach
    public void setUp() throws IOException {
        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mojo-test-status" );
    }

    @Test
    public void test() throws Exception {
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "de.ronnyfriedland.maven:adr-maven-plugin:status" );
    }
}
