package com.exaple.demoindexingmetadata.opensearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.Transport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSConfig {

    @Value("${demoapp.openserch.domain}")
    private String host;

    @Value("${demoapp.openserch.user}")
    private String openSearchUser;

    @Value("${demoapp.openserch.password}")
    private String openSearchPassword;

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    RestClient restClient ;
    OpenSearchClient client = null;

    @Bean
    OpenSearchClient openSearchClient () {

        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(openSearchUser, openSearchPassword));

        HttpHost httpHost = new HttpHost(host, 4566, "http");
        restClient = RestClient.builder(httpHost)
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }

                }).build();

        Transport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new OpenSearchClient((OpenSearchTransport) transport);
        return  client;
    }

}
