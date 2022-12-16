package com.ntnn.config;

import com.ntnn.avro.Account;
import com.ntnn.avro.AccountId;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Autowired
    private KafKaProperties kafKaProperties;

    private Map<String, Object> configMap;

    @Bean
    public ProducerFactory<AccountId, Account> producerConfig() {
        configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafKaProperties.getBoostrapServers());
        configMap.put(ProducerConfig.ACKS_CONFIG, kafKaProperties.getProducer().getAcks());
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafKaProperties.getProducer().getKeySerializer());
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafKaProperties.getProducer().getValueSerializer());

        configMap.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafKaProperties.getSchemaRegistry().getUrl());
        configMap.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    public KafkaTemplate<AccountId, Account> kafkaTemplate() {
        return new KafkaTemplate<>(producerConfig());
    }
}
