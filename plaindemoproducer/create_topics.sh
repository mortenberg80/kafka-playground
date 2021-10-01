#!/bin/bash

kafka-topics --bootstrap-server localhost:29092 --create --topic "no.tretoen.kafka.test.events" --partitions 3 --if-not-exists
