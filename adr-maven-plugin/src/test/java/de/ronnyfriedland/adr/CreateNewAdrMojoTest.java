package de.ronnyfriedland.adr;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Integrationtest for {@link CreateNewAdrMojo}
 *
 * @author ronnyfriedland
 */
public class CreateNewAdrMojoTest {

    private File testDir;

    @BeforeEach
    public void setUp() throws IOException {
        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mojo-test-create" );
    }

    @Test
    public void test() throws Exception {
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setSystemProperty("subject", "test1");
        verifier.executeGoal( "de.ronnyfriedland.maven:adr-maven-plugin:create" );

        verifier.assertFilePresent("target/adr/000_test1.md");
        verifier.assertFilePresent("target/adr/index.md");

        List<String> indexLines = verifier.loadLines("target/adr/index.md", StandardCharsets.UTF_8.name());
        Assertions.assertEquals(1, indexLines.size());
        Assertions.assertEquals("000_test1.md", indexLines.get(0));

        List<String> adrLines = verifier.loadLines("target/adr/000_test1.md", StandardCharsets.UTF_8.name());
        Assertions.assertEquals(3, adrLines.size());
        Assertions.assertEquals("subject: test1", adrLines.get(0));
        Assertions.assertEquals("status: proposed", adrLines.get(1));
        Assertions.assertEquals("date: " + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()), adrLines.get(2));
    }
}
