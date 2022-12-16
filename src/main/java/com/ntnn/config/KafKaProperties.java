package com.ntnn.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix =  "infrastructure.kafka")
public class KafKaProperties {
    private String boostrapServers;
    private String securityProtocol;
    private SchemaRegistry schemaRegistry = new SchemaRegistry();
    private Async async = new Async();
    private Consumer consumer = new Consumer();
    private Producer producer = new Producer();
    private  Listener listener = new Listener();
    private Ssl ssl = new Ssl();

    @Getter
    @Setter
    static class SchemaRegistry {
        private String url;
        private int cacheSize;
        private String subjectNameStrategy;
        private String keySubjectNameStrategy;
        private String valueSubjectNameStrategy;
    }

    @Getter
    @Setter
    static class Async {
        private int poolSize;
        private int maxPoolSize;
        private int queueCapacity;
        private int keepAliceSeconds;
        private boolean allowThreadTimeout;
    }

    @Getter
    @Setter
    static class Consumer {
        private String topic;
        private Integer maxPollRecords;
        private boolean enableAutoCommit;
        private String groupId;
        private String keyDeserializer;
        private String valueDeserializer;
        private String autoOffsetReset;
        private String keyDelegateClass;
        private String valueDelegateClass;
    }

    @Getter
    @Setter
    static class Listener {
        private String ackMode;
        private int concurrency;
        private long idleEventInterval;
    }

    @Getter
    @Setter
    static class Producer {
        private String topic;
        private String acks;
        private String keySerializer;
        private String valueSerializer;
    }

    @Getter
    @Setter
    static class Ssl {
        private boolean enable;
        private String keystoreLocation;
        private String keystorePassword;
        private String truststoreLocation;
        private String truststorePassword;
    }
}
