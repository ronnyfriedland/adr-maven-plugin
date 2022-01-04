package de.ronnyfriedland.adr.export.formats;

import com.vladsch.flexmark.docx.converter.DocxRenderer;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.superscript.SuperscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Docx related export processor
 *
 * @author ronnyfriedland
 */
public class ExportDocxProcessor {

    /**
     * Creates a new instance of {@link ExportDocxProcessor}
     */
    private ExportDocxProcessor() {
        // empty
    }

    /**
     * Export as docx
     *
     * @param targetPath the target path
     * @param files      the files
     * @throws IOException error writing export
     */
    public static void exportDocx(final String targetPath, final Set<Path> files) throws IOException {
        MutableDataSet options = new MutableDataSet()
                .set(Parser.EXTENSIONS, Arrays.asList(
                        DefinitionExtension.create(),
                        FootnoteExtension.create(),
                        StrikethroughSubscriptExtension.create(),
                        InsExtension.create(),
                        SuperscriptExtension.create(),
                        TablesExtension.create(),
                        TocExtension.create(),
                        SimTocExtension.create(),
                        WikiLinkExtension.create()
                ))
                .set(DocxRenderer.SUPPRESS_HTML, true)
                .set(DocxRenderer.DOC_RELATIVE_URL, "file:///")
                .set(DocxRenderer.DOC_ROOT_URL, "file:///");

        Parser parser = Parser.builder(options).build();
        DocxRenderer renderer = DocxRenderer.builder(options).build();

        for (Path fileForExport : files) {
            String typedFileName = FilenameUtils.removeExtension(fileForExport.getFileName().toString()) + "." + FormatType.docx.name();
            try (FileWriter fw = new FileWriter(Path.of(targetPath, FormatType.docx.name(), typedFileName).toFile())) {

                List<String> lines = Files.readAllLines(Paths.get(fileForExport.toUri()), StandardCharsets.UTF_8);
                String processed = String.join(System.lineSeparator(), lines);

                for (Path fileForReplacment : files) {
                    processed = processed.replaceAll(fileForReplacment.getFileName().toString(),
                            FilenameUtils.removeExtension(fileForReplacment.getFileName().toString()) + "." + FormatType.docx.name());
                }

                processed = Stream.of(processed).map(parser::parse).map(renderer::render).collect(Collectors.joining());

                fw.write(processed);
            }
        }
    }
}