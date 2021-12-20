package gr.codelearn.service;

import com.google.common.base.Strings;
import gr.codelearn.config.AMQPConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class WalletService {

    private RabbitTemplate rabbitTemplate;

    public void walletRequest(Map<String, Object> wpayload) {
        rabbitTemplate.convertAndSend(AMQPConfiguration.exchangeName, AMQPConfiguration.walletRoutingKey, validate(wpayload));
        log.info("A wallet payload has been sent to the queue.");
    }
    private Map<String, Object> validate(Map<String, Object> wpayload) {
        // replacing attributes with "" instead of null because we do are not required to check if string is null in all the following services
        //cid (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("cid"))) {
            wpayload.put("cid", "");
        }
        //creditorName (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("creditorName"))) {
            wpayload.put("creditorName", "");
        }
        //creditorAccount (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("creditorIBAN"))) {
            wpayload.put("creditorIBAN", "");
        }
        //debtorName (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("debtorName"))) {
            wpayload.put("debtorName", "");
        }
        //debtorAccount (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("debtorIBAN"))) {
            wpayload.put("debtorIBAN", "");
        }
        //paymentAmount (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("paymentAmount"))) {
            wpayload.put("paymentAmount", 0);
        }
        //valueDate (string)
        if (Strings.isNullOrEmpty((String) wpayload.get("valueDate"))) {
            wpayload.put("valueDate", new Date());
        }
        //paymentCurrency (string)
        if(Strings.isNullOrEmpty((String) wpayload.get("paymentCurrency"))) {
            wpayload.put("paymentCurrency", "");
        }
        return wpayload;
    }
}
