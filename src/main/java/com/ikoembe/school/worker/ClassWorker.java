package com.ikoembe.school.worker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
//@Worker
@Service
public class ClassWorker {

    @RabbitListener(queues = "user.operation")
    public void onUserCreate(String user) throws InterruptedException {
        log.info("Hello {}", user);
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        log.info("Completed in : {} ms", System.currentTimeMillis() - start);
    }
}
