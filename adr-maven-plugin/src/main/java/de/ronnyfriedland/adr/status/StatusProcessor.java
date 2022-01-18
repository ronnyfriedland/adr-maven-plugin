package de.ronnyfriedland.adr.status;

import de.ronnyfriedland.adr.template.enums.StatusType;
import de.ronnyfriedland.adr.template.exception.TemplateProcessorException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Status of existing adr files.
 *
 * @author ronnyfriedland
 */
public class StatusProcessor {

    /**
     * The name of the index file
     */
    public static final String INDEX_FILENAME = "index.md";

    private static final Pattern statusPattern = Pattern.compile(".*[sS]tatus.*(proposed|rejected|accepted|deprecated).*");
    private static final Pattern linkPattern = Pattern.compile("\\(([^)]+)\\)*");

    /**
     * Process the adr status.
     *
     * @param targetPath the target path of the processed template
     * @throws TemplateProcessorException error during template processing
     */
    public Map<StatusType, BigInteger> processStatus(String targetPath) throws TemplateProcessorException {
        Map<StatusType, BigInteger> status = new EnumMap<>(StatusType.class);

        try (Stream<Path> pathStream = Files.find(Path.of(targetPath), 1,
                (p, basicFileAttributes) -> p.getFileName().toString().endsWith(".md") && !p.getFileName()
                        .toString().equalsIgnoreCase(INDEX_FILENAME))) {

            Set<Path> files = pathStream.filter(Files::isRegularFile).collect(Collectors.toSet());
            for (Path file : files) {
                String content = Files.readString(file, StandardCharsets.UTF_8);

                Matcher matcher = statusPattern.matcher(content);

                if (matcher.find()) {
                    StatusType statusType = StatusType.valueOf(matcher.group(1));
                    status.put(statusType, Optional.ofNullable(status.get(statusType)).orElse(BigInteger.ZERO).add(BigInteger.ONE));
                }
            }

        } catch (final IOException e) {
            throw new TemplateProcessorException("Error processing status", e);
        }
        return status;
    }

    public long processBrokenLinks(String targetPath) throws TemplateProcessorException {
        try {
            String content = Files.readString(Path.of(targetPath, INDEX_FILENAME), StandardCharsets.UTF_8);
            Matcher matcher = linkPattern.matcher(content);
            return matcher.results().filter(r -> !Path.of(targetPath, r.group(1)).toFile().exists()).count();
        } catch (final IOException e) {
            throw new TemplateProcessorException("Error get broken link", e);
        }
    }

}
