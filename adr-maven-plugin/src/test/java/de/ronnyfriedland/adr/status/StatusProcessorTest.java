package de.ronnyfriedland.adr.status;

import de.ronnyfriedland.adr.template.enums.StatusType;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * Test {@link StatusProcessor}
 *
 * @author Ronny.Friedland@t-systems.com
 */

public class StatusProcessorTest {

    private static final String baseDir = StatusProcessorTest.class.getSimpleName();

    private final StatusProcessor subject = new StatusProcessor();

    @BeforeAll
    public static void setUp() {
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
    public void testBrokenLinks() throws Exception {
        Files.createFile(Path.of("target", baseDir, "index.md"));

        Assertions.assertEquals(0, subject.processBrokenLinks(Path.of("target", baseDir).toString()));

        IOUtils.write("foo(bar)", new FileOutputStream(Path.of("target", baseDir, "index.md").toFile()), StandardCharsets.UTF_8);

        Assertions.assertEquals(1, subject.processBrokenLinks(Path.of("target", baseDir).toString()));
    }

    @ParameterizedTest
    @EnumSource(StatusType.class)
    public void testStatus(final StatusType statusType) throws Exception {
        Path created = Files.createFile(Path.of("target", baseDir, statusType + ".md"));

        IOUtils.write("status: " + statusType, new FileOutputStream(created.toFile()), StandardCharsets.UTF_8);

        Assertions.assertTrue(subject.processStatus(Path.of("target", baseDir).toString()).containsKey(statusType));
    }
}
