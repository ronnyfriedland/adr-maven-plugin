package de.ronnyfriedland.adr.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;

/**
 * Test {@link IndexProcessor}
 *
 * @author Ronny.Friedland@t-systems.com
 */
public class IndexProcessorTest {

    private static final String baseDir = AdrProcessorTest.class.getSimpleName();

    private final IndexProcessor subject = new IndexProcessor("target/templates/adr");

    @BeforeEach
    public void setUp() {
        try {
            Files.createDirectory(Path.of("target", baseDir));
        } catch (final IOException e) {
            Assertions.fail("Error creating directories", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            Files.walk(Path.of("target", baseDir)).sorted(Comparator.reverseOrder())
                    .map(Path::toFile).forEach(File::delete);
        } catch (final IOException e) {
            Assertions.fail("Error deleting directories", e);
        }
    }

    @Test
    public void testSuccess() throws Exception {
        Files.createFile(Path.of("target", baseDir, testFile()));

        try {
            subject.processIndexTemplate("index-template.md", "target/" + baseDir);
        } catch (final TemplateProcessorException e) {
            Assertions.fail("No exception expected", e);
        }
    }

    @Test
    public void testNoTemplateFileFound() throws Exception {
        Files.createFile(Path.of("target", baseDir, testFile()));

        Assertions.assertThrows(TemplateProcessorException.class,
                () -> subject.processIndexTemplate("foo.md", "target/" + baseDir));
    }

    private String testFile() {
        return baseDir + "_" + System.currentTimeMillis() + ".md";
    }
}
