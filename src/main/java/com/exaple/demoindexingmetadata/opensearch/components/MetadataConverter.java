package com.exaple.demoindexingmetadata.opensearch.components;

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
public class MetadataConverter implements Converter<String, Metadata> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataConverter.class);
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Metadata convert(String source) {
        try {
            return objectMapper.readValue(source, Metadata.class);
        } catch (IOException e) {
            LOGGER.error("There was an error during metadata conversion");
            throw new IOException("Metadata conversion error", e);
        }
    }
}
