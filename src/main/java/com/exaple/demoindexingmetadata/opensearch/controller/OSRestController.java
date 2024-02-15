package com.exaple.demoindexingmetadata.opensearch.controller;

import com.exaple.demoindexingmetadata.opensearch.domain.IndexData;
import com.exaple.demoindexingmetadata.opensearch.response.IndexDataResponse;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Result;
import org.opensearch.client.opensearch._types.query_dsl.MatchPhrasePrefixQuery;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/indexing")
public class OSRestController {

    private final OpenSearchClient openSearchClient;
    @Value("${demoap.openserch.index}")
    private  String index = "demo-index";

    public OSRestController(OpenSearchClient openSearchClient) {
        this.openSearchClient = openSearchClient;
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Result> addSomeData(@PathVariable String documentId,
                                              @RequestBody IndexData data) throws IOException {
        System.out.println("Some data called");
        IndexData indexData = new IndexData(data.getFirstName(), data.getLastName(), data.getAnswer());
        IndexRequest<IndexData> indexRequest = new IndexRequest.Builder<IndexData>()
                .index(index).id(documentId)
                .document(indexData).build();
        IndexResponse indexResponse = openSearchClient.index(indexRequest);
        System.out.println(indexResponse.result().toString());
        System.out.println(indexResponse.id());
        return new ResponseEntity<>(indexResponse.result(), HttpStatus.OK );
    }

    @GetMapping("/searchphrase")
    public ResponseEntity<List<IndexDataResponse>> searchRequest(@RequestParam("field") String field,
                                                                 @RequestParam("q") String searchPhrase) throws IOException {

        System.out.println("/searchphrase endpoint");
        MatchPhrasePrefixQuery matchPhrasePrefixQuery = new MatchPhrasePrefixQuery.Builder()
                .field(field)
                .query(searchPhrase)
                .build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .query(q -> q.matchPhrasePrefix(matchPhrasePrefixQuery)).build();
        SearchResponse<IndexData> response = openSearchClient.search(searchRequest, IndexData.class);

        List<IndexDataResponse> responses = new ArrayList<>();
        System.out.println("Hits count for phrase: " + searchPhrase + " = " + response.hits().hits().size());

        for (int i = 0; i< response.hits().hits().size(); i++) {
            IndexDataResponse indexDataResponse = new IndexDataResponse(
                    response.hits().hits().get(i).id(),
                    response.hits().hits().get(i).source()
            );
            responses.add(indexDataResponse);
        }
        return new ResponseEntity<List<IndexDataResponse>>(responses, HttpStatus.OK);
    }
 }
