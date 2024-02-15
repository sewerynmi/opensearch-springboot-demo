package com.exaple.demoindexingmetadata.opensearch.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {


    @JsonProperty("category")
    private String category;

    @JsonProperty("id")
    private int id;

    @JsonProperty("indexData")
    private String indexData;

    @Override
    public String toString() {
        return "FileMetadata{" +
                "category='" + category + '\'' +
                ", id=" + id +
                ", indexData='" + indexData + '\'' +
                '}';
    }
}
