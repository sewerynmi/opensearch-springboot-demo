package com.exaple.demoindexingmetadata.opensearch.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {


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
