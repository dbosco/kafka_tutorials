#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Usage: $0 <input.properties> <user.keytab>"
    echo "Example: $0 input.properties jane.keytab"
    exit 1
fi

set -x
java -classpath .:target/"*" -Djava.security.auth.login.config=$2 kafka_tutorials.KafkaConsumerKerberosExample $*
