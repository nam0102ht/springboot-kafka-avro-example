package com.ntnn.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class CustomConsumerAwareListenerErrorHandler implements ConsumerAwareListenerErrorHandler {


    private final BinaryExceptionClassifier classifier = configureDefaultClassifier();

    private static BinaryExceptionClassifier configureDefaultClassifier() {
        Map<Class<? extends Throwable>, Boolean> classified = new HashMap<>();
        classified.put(DeserializationException.class, false);
        classified.put(MethodArgumentResolutionException.class, false);
        classified.put(NoSuchMethodException.class, false);
        classified.put(ClassCastException.class, false);
        classified.put(LogAndContinue.class, false);
        final BinaryExceptionClassifier binaryExceptionClassifier = new BinaryExceptionClassifier(classified, true);
        binaryExceptionClassifier.setTraverseCauses(true);
        return binaryExceptionClassifier;
    }


    /**
     * This method will handover the exception to the container's error handler if the exception is not classified.
     * If an exception is classified, which is default behaviour, then it will not handover the exception to the container's error handler &&
     * will set seek of current consumer thread to reprocess the same message again and again.
     *
     * @param message   message record resulting in exception.
     * @param exception exception thrown by the listener.
     * @param consumer  consumer thread listening for messages.
     * @return Anything returned by this method is ignored.
     */
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {

        final MessageHeaders headers = message.getHeaders();

        if (!this.classifier.classify(exception)) {

            log.error("Handing over message on partition = {}  and  offset = {} to be skipped for this exception. ",
                    headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                    headers.get(KafkaHeaders.OFFSET, Long.class), exception);
            throw exception;
        }


        //set seek to retrieve the same message again
        try {
            log.error("Setting seek for partition = {}  to  offset = {} for of this exception. This message will be re-processed.",
                    headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                    headers.get(KafkaHeaders.OFFSET, Long.class), exception);
            consumer.seek(new TopicPartition(headers.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                            headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class)),
                    headers.get(KafkaHeaders.OFFSET, Long.class));
        } catch (Exception ex) {
            log.error("Failed to seek partition = {}  to  offset = {}. This message will be skipped.",
                    headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                    headers.get(KafkaHeaders.OFFSET, Long.class), ex);
            throw exception; // cannot be replayed, needs to be skipped
        }

        return null;

    }
}