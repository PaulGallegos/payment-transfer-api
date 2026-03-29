package com.paulgallegos.payment.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    @Value("${kafka.topics.payment-initiated}")
    private String paymentInitiateTopic;
    @Value("${kafka.topics.payment-completed}")
    private String paymentCompleteTopic;

    public void publishPaymentInitiated(PaymentEvent paymentEvent){
        log.info("Publishing payment initiated event for paymentId: {}", paymentEvent.getPaymentId());
        kafkaTemplate.send(paymentInitiateTopic, paymentEvent.getPaymentId().toString(), paymentEvent);
    }

}
