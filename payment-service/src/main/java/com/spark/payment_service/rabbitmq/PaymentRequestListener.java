package com.spark.payment_service.rabbitmq;


import com.spark.payment_service.dto.PaymentRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PaymentRequestListener {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final RabbitTemplate rabbitTemplate;

    public PaymentRequestListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void handlePaymentRequest(PaymentRequest paymentRequest) throws Exception {
        if(paymentRequest.getStatus().equals("AVAILABLE")){
            System.out.println("Payment Request :(PAID/NOT_PAID) ::==>> Default PAID");
            paymentRequest.setStatus("PAID");
            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, paymentRequest);
        }else if(paymentRequest.getStatus().equals("NOT_AVAILABLE")){
            paymentRequest.setStatus("NOT_PAID");
            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, paymentRequest);
        }
    }
}
