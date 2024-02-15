package com.exaple.demoindexingmetadata.opensearch.response;

import com.exaple.demoindexingmetadata.opensearch.domain.IndexData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class IndexDataResponse {

    private String id;
    private IndexData indexData;

}
