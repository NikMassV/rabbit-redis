package edu.nm.RabbitRedis.rabbit;

import edu.nm.RabbitRedis.domain.Task;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class TaskSender {

    RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendTask(Task payload) {
        rabbitMessagingTemplate.convertAndSend(TaskListener.TASK_EXCHANGE, null, payload);
    }
}
