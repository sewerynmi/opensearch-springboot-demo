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

## Payloads

Note: The attached Postman collection should give a general idea of how the requests
and payloads should look like.

#### Json Body

`{
    "firstName": "John",
    "lastName": "Doe",
    "answer": "I love cats and dogs."
}`

#### Metadata Json

`{
     "category":"MyCategory",
     "id": 2223,
     "indexData":"{  \"firstName\": \"John\", \"lastName\": \"Doe\", \"answer\": \"I love cats and dogs\" }"
}`

## Searching requests

#### matchPhrasePrefixSearch

Example 

`<controller_url>/search?field=answer&q=fish`

`<controller_url>/search?field=firstName&q=john`