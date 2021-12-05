package com.microservices.fr.tech.mq.producers;

import com.microservices.fr.tech.dto.PaymentRequest;
import com.microservices.fr.tech.dto.QueueMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Haytham DAHRI
 * Payment MQ Sender
 */
@Component
@Log4j2
public class PaymentSender {

    @Value("${com.tech.queues.create_payment}")
    private String paymentQueue;

    private final RabbitTemplate rabbitTemplate;

    public PaymentSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentPayload(PaymentRequest paymentRequest, String businessKey) {
        log.info("Start PaymentSender.sendPaymentPayload(PaymentRequest paymentRequest, String businessKey)");
        QueueMessage<Object> queueMessage = QueueMessage.builder()
                .content(paymentRequest)
                .businessKey(businessKey)
                .build();
        log.info("QueueMessage: {}", queueMessage);
        this.rabbitTemplate.convertAndSend(paymentQueue, queueMessage);
        log.info("End PaymentSender.sendPaymentPayload(PaymentRequest paymentRequest, String businessKey)");
    }

}
