package de.ronnyfriedland.adr.export.formats;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import de.ronnyfriedland.adr.export.enums.FormatType;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;

/**
 * Pdf related export processor
 *
 * @author ronnyfriedland
 */
public class ExportPdfProcessor {

    /**
     * Creates a new instance of {@link ExportPdfProcessor}
     */
    private ExportPdfProcessor() {
        // empty
    }

    /**
     * Export as pdf
     *
     * @param targetPath the target path
     * @param files      the files
     * @throws IOException error writing export
     */
    public static void exportPdf(final String targetPath, final Set<Path> files) throws IOException {
        for (Path fileForExport : files) {
            String htmlFileName = FilenameUtils.removeExtension(fileForExport.getFileName().toString()) + "." + FormatType.html.name();
            String pdfFileName = FilenameUtils.removeExtension(fileForExport.getFileName().toString()) + "." + FormatType.pdf.name();
            try (FileInputStream fis = new FileInputStream(Path.of(targetPath, FormatType.html.name(), htmlFileName).toFile());
                 FileOutputStream fos = new FileOutputStream(Path.of(targetPath, FormatType.pdf.name(), pdfFileName).toFile())) {

                String processed = IOUtils.toString(fis, StandardCharsets.UTF_8);
                for (Path fileForReplacment : files) {
                    processed = processed.replaceAll(
                            FilenameUtils.removeExtension(fileForReplacment.getFileName().toString()) + "." + FormatType.html.name(),
                            FilenameUtils.removeExtension(fileForReplacment.getFileName().toString()) + "." + FormatType.pdf.name());
                }
                PdfConverterExtension.exportToPdf(fos, processed, null, BaseRendererBuilder.TextDirection.LTR);
            }
        }
    }
}