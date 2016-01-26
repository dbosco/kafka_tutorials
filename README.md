# kafka_tutorials
Sample code for testing Kafka

## Creating eclipse project
```
mvn eclipse:eclipse
```
### Go to your Eclipse IDE and import exisiting project

## Compilation
```
mvn clean compile package
```

## Running without Kerberos
### Go to the server where you have installed Kafka and create the topic
```
./bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic test101 --partitions 1 --replication-factor 1
```
### First test using Kafka console/CLI utilities
#### From one window/terminal start the publisher
```
bin/kafka-console-producer.sh --broker-list `hostname -f`:6667 --topic test101
```
#### From another window/terminal start the consumer
```
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test101
```
### Make a copy of input.properties and update
```
bootstrap.servers=<your broker service host>:<your broker service port>
```
### Running producer/publisher
```
./run_publisher.sh <input.properties>
```

### Running consumer
```
./run_Consumer.sh <input.properties>
```

## Running with Kerberos
For you running in Kerberos environment, you need to create a principal for the client application. For the principal create a keytab

### To create the test topic, go to the Kafka broker server
```
sudo su - kafka
kinit kafka
bin/kafka-topics.sh --zookeeper `hostname -f`:2181 --create --topic test101 --partitions 1 --replication-factor 1
```
### First test using Kafka console/CLI utilities
#### From one window/terminal start the publisher
```
kinit <user principal>
bin/kafka-console-producer.sh --broker-list `hostname -f`:6667 --topic test101 --security-protocol PLAINTEXTSASL
```
#### From another window/terminal start the consumer
```
kinit <user principal> (if different user)
bin/kafka-console-consumer.sh --zookeeper `hostname -f`:2181 --topic test101 --security-protocol PLAINTEXTSASL --from-beginning 
--consumer.config /tmp/consumer.properties --delete-consumer-offsets
```

### Setting up Kerberos client for Kafka
#### Copy the client user keytab to the client machine
#### Make a copy of kafka_client_jaas.conf and update the keytab location and principal name
```
KafkaClient {
      	com.sun.security.auth.module.Krb5LoginModule required
        useKeyTab=true
        storeKey=true
        keyTab="jane.keytab"
        principal="jane@EXAMPLE.COM";
};
```
### Running Publisher/Producer
```
./run_publisher_kerberos.sh input.properties kafka_client_jaas.conf
```
### Running consumer
```
./run_consumer_kerberos.sh input.properties kafka_client_jaas.conf 
```
