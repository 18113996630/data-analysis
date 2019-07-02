package com.hrong.db2kafka.kafka;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Int;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author hrong
 * @ClassName Producer
 * @Description
 * @Date 2019/6/30 13:51
 **/
@Slf4j
@Component
public class Producer {
	@Value(value = "${spring.kafka.topic}")
	private String topic;

	@Value(value = "${spring.kafka.key}")
	private String key;

	@Value(value = "${spring.kafka.message.waite}")
	private String waite;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Value(value = "${spring.kafka.producer.send-async}:false")
	private String isAsync;

	private KafkaProducer<String, String> producer;

	private Producer() {
		log.info("开始加载kafka配置，并实例化kafka-producer");
		Properties properties = new Properties();
		try {
			@Cleanup InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("kafka.properties");
			properties.load(inputStream);
			producer = new KafkaProducer<>(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int sendMessage(String message) {
		int count = 0;
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
		if (Boolean.valueOf(isAsync)) {
			log.info("开始异步发送消息");
			Future<RecordMetadata> result = producer.send(record, (metadata, exception) -> {
				if (metadata != null && exception == null) {
					long end = System.currentTimeMillis();
					log.info("发送消息成功，消息分区：{}，当前offset为：{}", metadata.partition(), metadata.offset());
				}
				log.error("metadata is null,{}", exception != null ? exception.getMessage() : "");
			});
			count = result.isDone() ? 1 : 0;
		} else {
			log.info("开始同步发送消息");
			Future<RecordMetadata> send = producer.send(record);
			try {
				RecordMetadata metadata = send.get();
				log.info("发送消息成功，消息分区：{}，当前offset为：{}", metadata.partition(), metadata.offset());
				count = 1;
			} catch (InterruptedException | ExecutionException e) {
				log.error("出现异常：{}-{}", e.getClass(), e.getMessage());
				e.printStackTrace();
			}
		}
		if (Integer.valueOf(waite) != 0) {
			try {
				Thread.sleep(Integer.valueOf(waite));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
}
