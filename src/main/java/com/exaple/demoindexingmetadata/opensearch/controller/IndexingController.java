package com.exaple.demoindexingmetadata.opensearch.controller;

import com.exaple.demoindexingmetadata.opensearch.components.Metadata;
import com.exaple.demoindexingmetadata.opensearch.components.MetadataConverter;
import com.exaple.demoindexingmetadata.opensearch.domain.IndexDataModel;
import com.exaple.demoindexingmetadata.opensearch.response.IndexDataResponse;
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

    private final OpenSearchClient openSearchClient;

    @Value("${demoap.openserch.index}")
    private  String index;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataConverter.class);

    public IndexingController(OpenSearchClient openSearchClient) {
        this.openSearchClient = openSearchClient;
    }


    /**
     * This method allows to index data passed a json body
     * Example json body:
     * {
     *     "firstName": "John",
     *     "lastName": "Doe",
     *     "answer": "I love cats and dogs."
     * }
     */
    @PostMapping("/{documentId}")
    public ResponseEntity<Result> addSomeData(@PathVariable String documentId,
                                              @RequestBody IndexDataModel dataForIndex) throws IOException {

        IndexDataModel indexDataModel = new IndexDataModel(dataForIndex.getFirstName(),
                dataForIndex.getLastName(), dataForIndex.getAnswer());

        IndexRequest<IndexDataModel> indexRequest = new IndexRequest.Builder<IndexDataModel>()
                .index(index).id(documentId)
                .document(indexDataModel).build();
        IndexResponse indexResponse = openSearchClient.index(indexRequest);

        LOGGER.info("Data to be indexed: {}", dataForIndex);
        LOGGER.info("Index Response Status: {}", indexResponse.result().toString());
        LOGGER.info("Indexed Entity ID: {}", indexResponse.id());

        return new ResponseEntity<>(indexResponse.result(), HttpStatus.OK );
    }

    /**
     * This method allows to index data passed as a form-data json value
     * Example json for `metadata` field:
     * {
     *   "category":"MyCategory",
     *   "id": 2223,
     *   "indexData":"{  \"firstName\": \"John\", \"lastName\": \"Doe\", \"answer\": \"I love cats and dogs\" }"
     * }
     */
    @PostMapping
    public ResponseEntity<Result> addDataFromMetadata(@RequestParam("metadata") Metadata metadata) throws IOException {

        IndexDataModel indexDataModel = objectMapper.readValue(metadata.getIndexData(), IndexDataModel.class);

        IndexRequest<IndexDataModel> indexRequest = new IndexRequest.Builder<IndexDataModel>()
                .index(index).id(String.valueOf(metadata.getId()))
                .document(indexDataModel).build();
        IndexResponse indexResponse = openSearchClient.index(indexRequest);

        LOGGER.info("Data to be indexed: {}", metadata);
        LOGGER.info("Index Response Status: {}", indexResponse.result().toString());
        LOGGER.info("Indexed Entity ID: {}", indexResponse.id());

        return new ResponseEntity<>(indexResponse.result(), HttpStatus.OK );
    }

    /**
     * This endpoint allows to search index for a given value [`q`] in a given field.
     * Example URI part:
     *      /search?field=answer&q=fish
     *      /search?field=firstName&q=john
     */
    @GetMapping("/search")
    public ResponseEntity<List<IndexDataResponse>> searchRequest(@RequestParam("field") String field,
                                                                 @RequestParam("q") String searchPhrase) throws IOException {

        MatchPhrasePrefixQuery matchPhrasePrefixQuery = new MatchPhrasePrefixQuery.Builder()
                .field(field)
                .query(searchPhrase)
                .build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .query(q -> q.matchPhrasePrefix(matchPhrasePrefixQuery)).build();

        SearchResponse<IndexDataModel> response = openSearchClient.search(searchRequest, IndexDataModel.class);

        List<IndexDataResponse> responses = new ArrayList<>();

        LOGGER.info("Searching for a phrase/word: {}", searchPhrase);
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
