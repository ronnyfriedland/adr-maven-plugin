package de.ronnyfriedland.adr.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.ronnyfriedland.adr.template.enums.StatusType;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;

/**
 * Test {@link AdrProcessor}
 *
 * @author Ronny.Friedland@t-systems.com
 */
public class AdrProcessorTest {

    private static final String baseDir = AdrProcessorTest.class.getSimpleName();

    private final AdrProcessor subject = new AdrProcessor("target/templates/adr", "YYYY-mm-dd");

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

        Arrays.stream(StatusType.values()).forEach(status -> {
            try {
                subject.processAdrTemplate("adr-template.md", "target/" + baseDir, "test", status.name(),
                        "", "adr-test.md");
            } catch (final TemplateProcessorException e) {
                Assertions.fail("No exception expected", e);
            }
        });
    }

    @Test
    public void testNoTemplateFileFound() throws Exception {
        Files.createFile(Path.of("target", baseDir, testFile()));

        Assertions.assertThrows(TemplateProcessorException.class,
                () -> subject.processAdrTemplate("foo.md", "target/" + baseDir, "test",
                        StatusType.proposed.name(), "", "adr-test.md"));
    }

    private String testFile() {
        return baseDir + "_" + System.currentTimeMillis() + ".md";
    }
}
