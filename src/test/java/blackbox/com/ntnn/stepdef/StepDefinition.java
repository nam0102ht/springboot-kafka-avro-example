package blackbox.com.ntnn.stepdef;

import com.ntnn.avro.Account;
import com.ntnn.avro.AccountId;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class StepDefinition {

    private KafkaProducer<AccountId, Account> kafkaProducer;


    @BeforeAll
    public void init() {
        final var props = new Properties();
        props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "java-producer");
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());

        this.kafkaProducer = new KafkaProducer<>(props);
    }

    @Given("^push any message to queue input \"([^\"]*)\"$")
    public void given(String message) throws Exception {

        String mess = Files.readString(Path.of("data/message"));
        AccountId accountId = new AccountId();

    }

    @When("Feature: producer to push to queue\n")
    public void when() {}





}
