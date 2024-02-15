#!/bin/sh

awslocal opensearch create-domain \
    --region ${DEFAULT_REGION} \
    --domain-name ${OPENSEARCH_DOMAIN}

# Wait for the domain to become available and create index
echo "Waiting for the domain to become available..."
while true; do
    status=$(awslocal opensearch describe-domain --region ${DEFAULT_REGION} --domain-name ${OPENSEARCH_DOMAIN} --query 'DomainStatus.Processing' --output text)
    if [ $status != "False" ]; then
        sleep 3
    else
        echo "\nOpenSearch domain ${OPENSEARCH_DOMAIN} is available now."
        curl -X PUT ${OPENSEARCH_DOMAIN}.${DEFAULT_REGION}.opensearch.localhost.localstack.cloud:4566/${OPENSEARCH_INDEX}
        echo "\nIndex created: ${OPENSEARCH_INDEX}"
        break
    fi
done