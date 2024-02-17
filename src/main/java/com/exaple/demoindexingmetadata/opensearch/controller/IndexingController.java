package com.exaple.demoindexingmetadata.opensearch.controller;

import com.exaple.demoindexingmetadata.opensearch.components.Metadata;
import com.exaple.demoindexingmetadata.opensearch.components.MetadataConverter;
import com.exaple.demoindexingmetadata.opensearch.domain.IndexDataModel;
import com.exaple.demoindexingmetadata.opensearch.response.IndexDataResponse;
import com.exaple.demoindexingmetadata.opensearch.service.IndexingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Result;
import org.opensearch.client.opensearch._types.query_dsl.MatchPhrasePrefixQuery;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/indexing")
@Validated
public class IndexingController {

    /* I am aware that file could be refactored and many lines could be moved
    to separate methods, but I wanted to keep it like that for the purpose of learning.
     */

    private final IndexingService indexingService;
    @Value("${demoap.openserch.index}")
    private  String index;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataConverter.class);

    public IndexingController(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @PostMapping("/{documentId}")
    public ResponseEntity<Result> addDataFromJsonBody(@PathVariable String documentId,
                                              @RequestBody IndexDataModel dataForIndex) throws IOException {
        IndexResponse indexResponse = indexingService.indexJsonBodyData(index, documentId, dataForIndex);

        LOGGER.info("Data to be indexed: {}", dataForIndex);
        LOGGER.info("Index Response Status: {}", indexResponse.result().toString());
        LOGGER.info("Indexed Entity ID: {}", indexResponse.id());

        return new ResponseEntity<>(indexResponse.result(), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity<Result> addDataFromMetadata(@RequestParam("metadata") Metadata metadata) throws IOException {
        IndexResponse indexResponse = indexingService.indexMetadataJson(index, metadata);

        LOGGER.info("Data to be indexed: {}", metadata);
        LOGGER.info("Index Response Status: {}", indexResponse.result().toString());
        LOGGER.info("Indexed Entity ID: {}", indexResponse.id());

        return new ResponseEntity<>(indexResponse.result(), HttpStatus.OK );
    }


    @GetMapping("/search")
    public ResponseEntity<List<IndexDataResponse>> matchPhrasePrefixSearch(@RequestParam("field") String field,
                                                                 @RequestParam("q") String searchPhrase) throws IOException {
        SearchResponse<IndexDataModel> response = indexingService.matchPhrasePrefixQuerySearch(index, field, searchPhrase);

        List<IndexDataResponse> responses = new ArrayList<>();

        LOGGER.info("Searching for a phrase: {}", searchPhrase);
        LOGGER.info("Hits count for phrase `{}` : {}", searchPhrase, response.hits().hits().size() );

        for (int i = 0; i< response.hits().hits().size(); i++) {
            IndexDataResponse indexDataResponse = new IndexDataResponse(
                    response.hits().hits().get(i).id(),
                    response.hits().hits().get(i).source()
            );
            responses.add(indexDataResponse);
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
 }
