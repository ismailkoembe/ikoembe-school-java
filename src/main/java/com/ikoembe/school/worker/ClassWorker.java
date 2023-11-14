package com.ikoembe.school.worker;

import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import rabbitmq.UserCreatedMessage;

@Slf4j
//@Worker
@Service
public class ClassWorker {

    @RabbitListener(queues = "user.operation")
    public void onUserCreate(String user) {
        log.info ("Hello {}", user);
    }
}
