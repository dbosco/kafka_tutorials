#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <input.properties>"
    echo "Example: $0 input.properties"
    exit 1
fi

set -x
java -classpath .:target/"*" kafka_tutorials.KafkaPublisher $*
