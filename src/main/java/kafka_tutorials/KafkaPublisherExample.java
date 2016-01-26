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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaPublisherExample {
	String topicName = "test_topic";
	KafkaProducer<String, String> producer = null;

	// Loads the properties and calls the setup and start
	public void kickOff(String[] args) {
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
			startPublishing();
			producer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setup(Properties props) {
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());

		if (props.get("topic_name") != null) {
			topicName = (String) props.get("topic_name");
			System.out.println("Publishing to topic=" + topicName);
		}
		producer = new KafkaProducer<String, String>(props);
	}

	public void startPublishing() {
		System.out
				.println("Type message to send and press enter. Type \"exit\" to exit");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Console console = System.console();
		while (true) {
			try {
				// String message = console.readLine();
				System.out.print("Enter: ");
				String message = br.readLine();
				if ("exit".equalsIgnoreCase(message)
						|| "quit".equalsIgnoreCase(message)) {
					System.out.println("Good bye!!!");
					break;
				}
				System.out.println("Sending: " + message);
				RecordMetadata metadata = producer.send(
						new ProducerRecord<String, String>(topicName, message))
						.get();
				if (metadata == null) {
					System.out.println("Error sending message");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static public void main(String args[]) {
		KafkaPublisherExample publisher = new KafkaPublisherExample();
		publisher.kickOff(args);
	}
}
