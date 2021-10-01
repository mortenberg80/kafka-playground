# kafka-playground

Playground project exploring different kafka clients++

## Prerequisites

We need a running kafka cluster to connect the clients to.

[kafka-dev-sandbox](https://gitlab.com/kpmeen/kafka-dev-sandbox) is an easy-to-use setup for this.

### Kafka-connect

Some of the applications (kafka-streams) require generated data. We use kafka-connect-datagen for this.

Start kafka-dev-sandbox with kafka-connect installed:

- check out kafka-dev-sandbox
- navigate to kafka-dev-sandbox/plain
- start the cluster with the following command:

`./init.start -c -kc`

[kafka-connect-codegen](https://github.com/confluentinc/kafka-connect-datagen.git) is a kafka-connect-connector 
generating random test-data.

After the kafka-cluster is stared with kafka-connect:

- check out kafka-connect-codegen
- navigate to the checked-out folder
- start the kafka-connect-datagen-job by executing the following HTTP-call:

`curl -XPOST -H "Content-Type: application/json" -d @config/connector_stock_trades.config http://localhost:8083/connectors`

**Check what kafka-connect jobs are running**:

`curl http://localhost:8083/connectors`

**Check status of kafka-connect-codegen stocktrades job**:

`curl http://localhost:8083/connectors/datagen-stocktrades`

**Stop the kafka-connect-codegen job**:

`curl -XDELETE http://localhost:8083/connectors/datagen-stocktrades`

## plaindemoproducer

Simple producer producing messages to a topic.

## plaindemoconsumer

Simple producer consuming messages from a topic.

## plaindemostreams

Simple kafka-streams application exploring streams functionality, working on data from kafka-connect-codegen.
