package com.paulgallegos.account.kafka;

import com.paulgallegos.account.domain.Account;
import com.paulgallegos.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final AccountRepository accountRepository;

    @Transactional
    @KafkaListener(topics = "${kafka.topics.payment-initiated}", groupId = "account-service-group")
    public void handlePaymentInitiated(PaymentEvent event) {
        log.info("Received payment event: {}", event.getPaymentId());
        log.info("Sender account ID from event: {}", event.getSenderAccountId());

        accountRepository.findByUserId(event.getSenderAccountId()).ifPresent(
                account -> {
                    log.info("Found account: {}", account.getAccountNumber());
                    account.setBalance(account.getBalance().subtract(event.getAmount()));
                    accountRepository.save(account);
                    log.info("Balance updated for account: {}", account.getAccountNumber());
                });

    }


}
