#!/bin/bash

kafka-topics --bootstrap-server localhost:29092 --create --topic "stock-trades-aggregates" --partitions 1 --if-not-exists
