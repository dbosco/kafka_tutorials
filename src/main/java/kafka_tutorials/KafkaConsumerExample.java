/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package kafka_tutorials;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaConsumerExample {

	String topicName = "test_topic";

	KafkaConsumer<String, String> kafkaConsumer = null;

	public void kickOff(String args[]) {
		if (args.length == 0) {
			System.out.println("ERROR: Pass properties file as parameter");
			System.exit(1);
		}

		String inputPropertiesFile = args[0];
		Properties props = new Properties();
		try {
			System.out
					.println("Loading properties file " + inputPropertiesFile);
			props.load(new FileReader(new File(inputPropertiesFile)));

			setup(props);
			startConsumer();
			kafkaConsumer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setup(Properties props) {
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());

		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");

		if (props.get("topic_name") != null) {
			topicName = (String) props.get("topic_name");
			System.out.println("Consuming from topic=" + topicName);
		}
		kafkaConsumer = new KafkaConsumer<String, String>(props);
		kafkaConsumer.subscribe(Arrays.asList(topicName));
	}

	public void startConsumer() {
		System.out.println("Listening on topic=" + topicName
				+ ", kafkaConsumer=" + kafkaConsumer);
		while (true) {
			ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				String key = record.key();
				String value = record.value();

				System.out.println((key != null ? "key=" + key + ", " : "")
						+ "value=" + value);
			}
		}

	}

	static public void main(String args[]) {
		KafkaConsumerExample consumer = new KafkaConsumerExample();
		consumer.kickOff(args);
	}

}
