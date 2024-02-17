package com.exaple.demoindexingmetadata.opensearch.service;

import com.exaple.demoindexingmetadata.opensearch.components.Metadata;
import com.exaple.demoindexingmetadata.opensearch.domain.IndexDataModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.MatchPhrasePrefixQuery;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IndexingService {

    private final OpenSearchClient openSearchClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IndexingService(OpenSearchClient openSearchClient) {
        this.openSearchClient = openSearchClient;
    }

    public IndexResponse indexJsonBodyData(String index, String documentId, IndexDataModel dataForIndex) throws IOException {
        IndexDataModel indexDataModel = new IndexDataModel(dataForIndex.getFirstName(),
                dataForIndex.getLastName(), dataForIndex.getAnswer());

        IndexRequest<IndexDataModel> indexRequest = new IndexRequest.Builder<IndexDataModel>()
                .index(index).id(documentId)
                .document(indexDataModel).build();
        return openSearchClient.index(indexRequest);
    }

    public IndexResponse indexMetadataJson(String index, Metadata metadata) throws IOException {
        IndexDataModel indexDataModel = objectMapper.readValue(metadata.getIndexData(), IndexDataModel.class);

        IndexRequest<IndexDataModel> indexRequest = new IndexRequest.Builder<IndexDataModel>()
                .index(index).id(String.valueOf(metadata.getId()))
                .document(indexDataModel).build();
        return openSearchClient.index(indexRequest);
    }

    public SearchResponse<IndexDataModel> matchPhrasePrefixQuerySearch(String index, String field, String searchPhrase) throws IOException {
        MatchPhrasePrefixQuery matchPhrasePrefixQuery = new MatchPhrasePrefixQuery.Builder()
                .field(field)
                .query(searchPhrase)
                .build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .query(q -> q.matchPhrasePrefix(matchPhrasePrefixQuery)).build();

        return openSearchClient.search(searchRequest, IndexDataModel.class);
    }
}
