package de.ronnyfriedland.adr.export;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import de.ronnyfriedland.adr.export.enums.FormatType;
import de.ronnyfriedland.adr.export.exception.ExportProcessorException;
import org.apache.commons.io.FilenameUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Exports the adrs into the target format.
 *
 * @see FormatType
 *
 * @author ronnyfriedland
 */
public class ExportProcessor {

    /**
     * Export the adr markdown to target format
     *
     * @param targetPath the path of markdown adr and base path of the exported data
     * @throws ExportProcessorException error during export
     */
    public void exportAdr(final String targetPath, final String type) throws ExportProcessorException {
        try (Stream<Path> pathStream = Files.find(Path.of(targetPath), 1, (v, b) -> FilenameUtils.isExtension(v.getFileName().toString(), "md"))
        ) {
            Set<Path> files = new HashSet<>();
            pathStream.filter(Files::isRegularFile).forEach(files::add);

            //TODO: only html is supported for now
            switch(type) {
            default:
            case "html":
                exportHtml(targetPath, type, files);
                break;
            }
        } catch (IOException e) {
            throw new ExportProcessorException("Error exporting data", e);
        }
    }

    private void exportHtml(final String targetPath, final String type, final Set<Path> files) throws IOException {
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        for (Path fileForExport : files) {

            String typedFileName = FilenameUtils.removeExtension(fileForExport.getFileName().toString()) + "." + type;
            try (FileWriter fw = new FileWriter(Path.of(targetPath, type, typedFileName).toFile())) {

                List<String> lines = Files.readAllLines(Paths.get(fileForExport.toUri()), StandardCharsets.UTF_8);
                String processed = String.join(System.lineSeparator(), lines);

                for (Path fileForReplacment : files) {
                    processed = processed.replaceAll(fileForReplacment.getFileName().toString(),
                            FilenameUtils.removeExtension(fileForReplacment.getFileName().toString()) + "." + type);
                }
                processed = Stream.of(processed).map(parser::parse).map(renderer::render).collect(Collectors.joining());

                fw.write(processed);
            }
        }
    }

}
