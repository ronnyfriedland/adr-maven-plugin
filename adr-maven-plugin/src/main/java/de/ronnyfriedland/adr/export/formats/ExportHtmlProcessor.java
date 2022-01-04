package de.ronnyfriedland.adr.export.formats;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import de.ronnyfriedland.adr.export.enums.FormatType;
import org.apache.commons.io.FilenameUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Html related export processor
 *
 * @author ronnyfriedland
 */
public class ExportHtmlProcessor {

    /**
     * Creates a new instance of {@link ExportHtmlProcessor}
     */
    private ExportHtmlProcessor() {
        // empty
    }

    /**
     * Export as html
     *
     * @param targetPath the target path
     * @param files      the files
     * @throws IOException error writing export
     */
    public static void exportHtml(final String targetPath, final Set<Path> files) throws IOException {
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        for (Path fileForExport : files) {
            String typedFileName = FilenameUtils.removeExtension(fileForExport.getFileName().toString()) + "." + FormatType.html.name();
            try (FileWriter fw = new FileWriter(Path.of(targetPath, FormatType.html.name(), typedFileName).toFile())) {

                List<String> lines = Files.readAllLines(Paths.get(fileForExport.toUri()), StandardCharsets.UTF_8);
                String processed = String.join(System.lineSeparator(), lines);

                for (Path fileForReplacment : files) {
                    processed = processed.replaceAll(fileForReplacment.getFileName().toString(),
                            FilenameUtils.removeExtension(fileForReplacment.getFileName().toString()) + "." + FormatType.html.name());
                }
                processed = Stream.of(processed).map(parser::parse).map(renderer::render).collect(Collectors.joining());

                fw.write(processed);
            }
        }
    }
}