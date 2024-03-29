package edu.nm.RabbitRedis.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class TaskListener {

    public static final String TASK_QUEUE = "task.queue";
    public static final String TASK_EXCHANGE = "task.exchange";

    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = TASK_QUEUE),
                    exchange = @Exchange(value = TASK_EXCHANGE)
            )
    )
    public void handleTask(String task) {
        ObjectMapper mapper = new ObjectMapper();
        Thread.sleep(1000);
//        log.info(String.format("Service \"%s\" end process task \"%s\" from service \"%s\"", "1", task.getId()))
        log.info(mapper.writeValueAsString(task));
    }
}
