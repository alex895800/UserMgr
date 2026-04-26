package it.alex89.usermgr.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMessageListener.class);

    @RabbitListener(queues = "user_add_queue")
    public void receiveMessage(String message) {
        logger.info("Received message: " + message);
    }
}
