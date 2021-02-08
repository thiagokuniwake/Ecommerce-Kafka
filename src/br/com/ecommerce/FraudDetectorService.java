package br.com.ecommerce;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class FraudDetectorService {

	public static void main(String[] args) {
		var consumer = new KafkaConsumer<String, String>(properties());
		
		var topics = new ArrayList<String>();
		topics.add("ECOMMERCE_NEW_ORDER");
		consumer.subscribe(topics);
		
		while (true) {
			var records = consumer.poll(Duration.ofMillis(100));
			if (!records.isEmpty()) {
				System.out.println("Encontrei " + records.count() + " registros");

				for (var record : records) {
					System.out.println("----------------------------------------");
					System.out.println("Processing new order, checking for fraud");
					System.out.println(record.key());
					System.out.println(record.value());
					System.out.println(record.partition());
					System.out.println(record.offset());

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Order processed!!");
				}
			}
		}
	}

	private static Properties properties() {
		var prop = new Properties();
		prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
		return prop;
	}

}
