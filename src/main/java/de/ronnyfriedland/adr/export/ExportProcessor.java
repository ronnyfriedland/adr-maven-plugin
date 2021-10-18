package de.ronnyfriedland.adr.export;

import de.ronnyfriedland.adr.export.exception.ExportProcessorException;
import org.apache.commons.io.FilenameUtils;
import org.markdown4j.Markdown4jProcessor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ExportProcessor {

    /**
     * Export the adr markdown to target format
     *
     * @param targetPath the path of markdown adr and base path of the exported data
     * @throws ExportProcessorException error during export
     */
    public void exportAdr(final String targetPath, final String type) throws ExportProcessorException {
        try (Stream<Path> pathStream = Files.find(Path.of(targetPath), 1, (v, b) -> true)
        ) {
            Set<Path> files = new HashSet<>();
            pathStream.filter(Files::isRegularFile).forEach(files::add);

            //TODO: only html is supported for now
            Markdown4jProcessor md = new Markdown4jProcessor();
            for (Path fileForExport : files) {

                String typedFileName = FilenameUtils.removeExtension(fileForExport.getFileName().toString()) + "." + type;
                try (FileWriter fw = new FileWriter(Path.of(targetPath, type, typedFileName).toFile())) {
                    String processed = md.process(fileForExport.toFile());

                    for (Path fileForReplacment : files) {
                        processed = processed.replaceAll(fileForReplacment.getFileName().toString(),
                                FilenameUtils.removeExtension(fileForReplacment.getFileName().toString()) + "." + type);
                    }

                    fw.write(processed);
                }
            }
        } catch (IOException e) {
            throw new ExportProcessorException("Error exporting data", e);
        }
    }
}
