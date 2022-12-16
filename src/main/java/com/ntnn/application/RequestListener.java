package com.ntnn.application;

import com.ntnn.avro.Account;
import com.ntnn.avro.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestListener {

    @KafkaListener(topics = "${infrastructure.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory", clientIdPrefix = "subscriber", groupId = "consumer-g1", errorHandler = "customConsumerAwareListenerErrorHandler")
    public void onMessage(ConsumerRecord<AccountId, Account> record) {
        AccountId key = record.key();
        log.info("key: {}", key.toString());
        Account value = record.value();
        log.info("value: {}", value.toString());
    }

}
