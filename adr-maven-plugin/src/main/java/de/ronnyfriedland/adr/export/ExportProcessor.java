package de.ronnyfriedland.adr.export;

import de.ronnyfriedland.adr.export.enums.FormatType;
import de.ronnyfriedland.adr.export.exception.ExportProcessorException;
import de.ronnyfriedland.adr.export.formats.ExportDocxProcessor;
import de.ronnyfriedland.adr.export.formats.ExportHtmlProcessor;
import de.ronnyfriedland.adr.export.formats.ExportPdfProcessor;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Exports the adrs into the target format.
 *
 * @author ronnyfriedland
 * @see FormatType
 */
public class ExportProcessor {

    /**
     * Export the adr markdown to target format
     *
     * @param targetPath the path of markdown adr and base path of the exported data
     * @throws ExportProcessorException error during export
     */
    public void exportAdr(final String targetPath, final String type) throws ExportProcessorException {
        try (Stream<Path> pathStream = Files.find(Path.of(targetPath), 1,
                (v, b) -> FilenameUtils.isExtension(v.getFileName().toString(), "md"))
        ) {
            Set<Path> files = new HashSet<>();
            pathStream.filter(Files::isRegularFile).forEach(files::add);

            switch (type) {
                case "docx":
                    ExportDocxProcessor.exportDocx(targetPath, files);
                    break;
                case "html":
                    ExportHtmlProcessor.exportHtml(targetPath, files);
                    break;
                case "pdf":
                    ExportHtmlProcessor.exportHtml(targetPath, files);
                    ExportPdfProcessor.exportPdf(targetPath, files);
                    break;
                default:
                    throw new ExportProcessorException("Unknown type provided: " + type);
            }
        } catch (IOException e) {
            throw new ExportProcessorException("Error exporting data", e);
        }
    }

}
