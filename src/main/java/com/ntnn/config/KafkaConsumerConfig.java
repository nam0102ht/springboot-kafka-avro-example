package com.ntnn.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Autowired
    private KafKaProperties kafKaProperties;

    private Map<String, Object> configMap;

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafKaProperties.getBoostrapServers());
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafKaProperties.getConsumer().getKeyDeserializer());
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafKaProperties.getConsumer().getValueDeserializer());
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafKaProperties.getConsumer().getAutoOffsetReset());
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafKaProperties.getConsumer().isEnableAutoCommit());
        configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafKaProperties.getConsumer().getMaxPollRecords());

        configMap.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafKaProperties.getSchemaRegistry().getUrl());
        configMap.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(configMap);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ConsumerFactory<Object, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(containerFactory, consumerFactory);
        containerFactory.getContainerProperties().setIdleEventInterval(kafKaProperties.getListener().getIdleEventInterval());
        return containerFactory;
    }
}
