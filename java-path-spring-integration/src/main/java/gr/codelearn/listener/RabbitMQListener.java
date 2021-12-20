package gr.codelearn.listener;

import gr.codelearn.gateway.PaymentsGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQListener {

    private static final String queueName = "payment.queue";
    private static final String walletQueue="wallet.queue";
    public static final String TYPE_WALLET="wallet";
    public static final String TYPE_PAYMENT="payment";

    private PaymentsGateway paymentsGateway;

    @RabbitListener(queues = queueName)
    public void consumePayment(Map<String, Object> payload) {
        log.info("A payment payload has been received.");
        payload.put("type", TYPE_PAYMENT);
        paymentsGateway.initiatePayment(payload);
    }

    @RabbitListener(queues = walletQueue) //project
    public void consumeWalletPayment(Map<String, Object> wpayload) {
        log.info("A Wallet payment payload has been received.");
        wpayload.put("type",TYPE_WALLET);
        paymentsGateway.initiateWalletPayment(wpayload);
    }
}