package gr.codelearn.service;

import gr.codelearn.config.AMQPConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@AllArgsConstructor
@Slf4j
public class WalletService {
    private RabbitTemplate rabbitTemplate;

    public void walletrRequest(Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(AMQPConfiguration.exchangeName, AMQPConfiguration.routingKey, validate(payload));
        log.info("A payload has been sent to the queue.");
    }
}
