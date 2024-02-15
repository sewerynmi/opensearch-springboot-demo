package com.exaple.demoindexingmetadata.opensearch.components;

import com.exaple.demoindexingmetadata.opensearch.domain.FileMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class FileMetadataConverter implements Converter<String, FileMetadata> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMetadataConverter.class);
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public FileMetadata convert(String source) {
        try {
            return objectMapper.readValue(source, FileMetadata.class);
        } catch (IOException e) {
            LOGGER.error("Error code {}: Error during Metadata conversion {}");
            throw new IOException("Error during Metadata conversion", e);
        }
    }
}
