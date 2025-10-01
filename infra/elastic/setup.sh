#!/bin/bash

echo "Waiting for Elasticsearch availability";
until curl -s --cacert config/certs/ca/ca.crt https://elasticsearch01:9200 | grep -q "missing authentication credentials"; do
  echo "Elasticsearch not ready yet, waiting...";
  sleep 5;
done;

echo "Setting kibana_system password";
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
  RESPONSE=$(curl -s -X POST --cacert config/certs/ca/ca.crt \
    -u "elastic:${ELASTIC_PASSWORD}" \
    -H "Content-Type: application/json" \
    https://elasticsearch01:9200/_security/user/kibana_system/_password \
    -d "{\"password\":\"${KIBANA_PASSWORD}\"}")

  echo "Password set response: $RESPONSE"

  # Check if response is empty JSON object (success)
  if echo "$RESPONSE" | grep -q "{}"; then
    echo "Successfully set kibana_system password";
    break;
  fi

  RETRY_COUNT=$((RETRY_COUNT + 1))
  echo "Retry $RETRY_COUNT/$MAX_RETRIES - waiting 10 seconds...";
  sleep 10;
done;

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
  echo "ERROR: Failed to set kibana_system password after $MAX_RETRIES attempts";
  exit 1;
fi

# Create a marker file to signal setup completion
echo "Creating setup completion marker";
touch /tmp/.setup_complete

echo "All done!";