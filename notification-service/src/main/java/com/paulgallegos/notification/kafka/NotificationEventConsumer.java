package com.paulgallegos.notification.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationEventConsumer {

    @KafkaListener(topics = "${kafka.topics.payment-initiated}", groupId = "notification-service-group")
    public void handlePaymentInitiated(PaymentEvent event) {

        log.info("Sending payment initiated notification for payment: {}", event.getPaymentId());

    }

    @KafkaListener(topics = "${kafka.topics.payment-completed}", groupId = "notification-service-group")
    public void handlePaymentCompleted(PaymentEvent event) {

        log.info("Sending payment completed notification for payment: {}", event.getPaymentId());

    }
}
