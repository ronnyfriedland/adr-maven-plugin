package de.ronnyfriedland.adr.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.ronnyfriedland.adr.export.enums.FormatType;
import de.ronnyfriedland.adr.export.exception.ExportProcessorException;

/**
 * Test {@link ExportProcessor}
 *
 * @author ronnyfriedland
 */
public class ExportProcessorTest {

    private static final String baseDir = ExportProcessorTest.class.getSimpleName();

    private final ExportProcessor subject = new ExportProcessor();

    @BeforeEach
    public void setUp() {
        Arrays.stream(FormatType.values()).forEach(format -> {
            try {
                Files.createDirectories(Path.of("target", baseDir, format.name()));
            } catch (final IOException e) {
                Assertions.fail("Error creating directories", e);
            }
        });
    }

    @AfterEach
    public void tearDown() {
        Arrays.stream(FormatType.values()).forEach(format -> {
            try {
                Files.walk(Path.of("target", baseDir, format.name()))
                        .sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                Files.find(Path.of("target", baseDir), 1,
                        (v, b) -> FilenameUtils.wildcardMatch(v.getFileName().toString(),
                                baseDir + "*.md")).map(Path::toFile).forEach(File::delete);
            } catch (final IOException e) {
                Assertions.fail("Error deleting directories", e);
            }
        });
    }

    @Test
    public void testNoFilesSuccessAllFormats() throws IOException {
        Arrays.stream(FormatType.values()).forEach(format -> {
            try {
                subject.exportAdr("target/" + baseDir, format.name());
            } catch (final ExportProcessorException e) {
                Assertions.fail("No exception expected", e);
            }
            try {
                Assertions.assertEquals(0,
                        Files.list(Path.of("target", baseDir, FormatType.html.name())).count());
            } catch (final IOException e) {
                Assertions.fail("No exception expected", e);
            }
        });
    }

    @Test
    public void testSimpleSuccessHtml() throws IOException {
        Files.createFile(Path.of("target", baseDir, testFile()));


        Arrays.stream(FormatType.values()).forEach(format -> {
            try {
                subject.exportAdr("target/" + baseDir, FormatType.html.name());
            } catch (final ExportProcessorException e) {
                Assertions.fail("No exception expected", e);
            }
            try {
                Assertions.assertEquals(1,
                        Files.list(Path.of("target", baseDir, FormatType.html.name())).count());
            } catch (final IOException e) {
                Assertions.fail("No exception expected", e);
            }
        });
    }

    @Test
    public void testSimpleSuccessPdf() throws IOException {
        Files.createFile(Path.of("target", baseDir, testFile()));


        Arrays.stream(FormatType.values()).forEach(format -> {
            try {
                subject.exportAdr("target/" + baseDir, FormatType.pdf.name());
            } catch (final ExportProcessorException e) {
                Assertions.fail("No exception expected", e);
            }
            try {
                Assertions.assertEquals(1,
                        Files.list(Path.of("target", baseDir, FormatType.pdf.name())).count());
            } catch (final IOException e) {
                Assertions.fail("No exception expected", e);
            }
        });
    }

    @Test
    public void testSimpleSuccessDocx() throws IOException {
        Files.createFile(Path.of("target", baseDir, testFile()));


        Arrays.stream(FormatType.values()).forEach(format -> {
            try {
                subject.exportAdr("target/" + baseDir, FormatType.docx.name());
            } catch (final ExportProcessorException e) {
                Assertions.fail("No exception expected", e);
            }
            try {
                Assertions.assertEquals(1,
                        Files.list(Path.of("target", baseDir, FormatType.docx.name())).count());
            } catch (final IOException e) {
                Assertions.fail("No exception expected", e);
            }
        });
    }

    @Test
    public void testDirectoryNotFound() throws IOException {
        Files.createFile(Path.of("target", baseDir, testFile()));
        Assertions.assertThrows(ExportProcessorException.class,
                () -> subject.exportAdr("target/" + baseDir, "foo"));
    }

    private String testFile() {
        return baseDir + "_" + System.currentTimeMillis() + ".md";
    }
}
