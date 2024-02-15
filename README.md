# OpenSearch SpringBoot API Demo

### Motivation
The purpose of this project is to create simple API to interact with the OpenSearch domain and index.

## How to run project
- run `docker compose up -d` to create Localstack Docker container with OpenSearch service
- run spring boot app

## Postman collection

Attached in the file `OPENSEARCH SpringBoot API Demo.postman_collection.json`

## Metadata payload
`
{
"category":"MyCategory",
"id": 211,
"indexData":"{  \"firstName\": \"Keith\", \"lastName\": \"McCallister\", \"answer\": \"I have a puppy\" }"
}
`