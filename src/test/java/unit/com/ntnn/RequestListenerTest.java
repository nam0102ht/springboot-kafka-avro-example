package unit.com.ntnn;

import com.ntnn.application.RequestListener;
import com.ntnn.avro.Account;
import com.ntnn.avro.AccountId;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RequestListenerTest {

    @InjectMocks
    RequestListener requestListener;

    @Test
    public void test() {
        AccountId accountId = new AccountId();
        accountId.setAccountId("accountId");
        Account account = new Account();
        account.setAccountId("accountId");
        Assert.assertEquals("1", "1");
        ConsumerRecord<AccountId, Account> record = new ConsumerRecord<>("account", 1, 0l, accountId, account);
        Assertions.assertDoesNotThrow(() -> {
            requestListener.onMessage(record);
        });
    }
}
