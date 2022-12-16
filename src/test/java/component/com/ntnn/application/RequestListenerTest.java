package component.com.ntnn.application;


import com.ntnn.avro.Account;
import com.ntnn.avro.AccountId;
import com.ntnn.avro.Metadata;
import component.com.ntnn.ComponentTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;

@ComponentTest
public class RequestListenerTest {
    @Autowired
    private KafkaTemplate<AccountId, Account> kafkaTemplate;

    @Test
    public void happyPath() throws Exception {
        AccountId accountId = new AccountId();
        accountId.setAccountId("123456");
        accountId.setAccountType("ADMIN");
        accountId.setProductName("EBANKING");

        Account account = new Account();
        account.setAccountId("123456");
        account.setAccountType("ADMIN");
        account.setAccountOwnership("MEGA");
        account.setProductName("EBANKING");
        account.setAccountStatus("checking");

        Metadata metadata = new Metadata();
        metadata.setAppId("PL123");
        metadata.setCorrelationId(UUID.randomUUID().toString());
        metadata.setEventVersion("1.0");
        metadata.setSource("FE ACCOUNT");
        metadata.setSchemaUrl("account");
        metadata.setTime(System.currentTimeMillis());
        metadata.setSubject("CHECK PROFILES");
        metadata.setType("CHECK");
        metadata.setId(UUID.randomUUID().toString());
        account.setMetadata(metadata);
        System.out.println("Send message: " + account.toString());
        ListenableFuture<SendResult<AccountId, Account>> callback = kafkaTemplate.send("account", accountId, account);
        kafkaTemplate.flush();
        SendResult<AccountId, Account> result = callback.get();
        System.out.println(" status='success', partition='" + result.getRecordMetadata().partition() + "', offset='" + result.getRecordMetadata().offset() + "'");
    }
}
