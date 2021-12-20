package gr.codelearn.flow;

import gr.codelearn.service.PostingService;
import gr.codelearn.service.ReportingService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

import static gr.codelearn.listener.RabbitMQListener.TYPE_PAYMENT;

@Configuration
@EnableIntegration
@AllArgsConstructor
public class PostingFlow {

    private PostingService postingService;
    private ReportingService reportingService;

    @Bean
    public MessageChannel postingChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow postingInternalFlow() {
        return IntegrationFlows
                .from(postingChannel())
                .<Map<Boolean, Object>, Boolean>route(m -> TYPE_PAYMENT.equals(m.get("type")), msg -> msg
                        .subFlowMapping(true, paymentFlow -> paymentFlow.transform(postingService::makeTransaction))
                        .subFlowMapping(false, walletFlow -> walletFlow.transform(postingService::makeWalletTransaction))
                        .defaultOutputToParentFlow())
                .handle(message -> reportingService.executeReports(message))
                .get();
    }
}
