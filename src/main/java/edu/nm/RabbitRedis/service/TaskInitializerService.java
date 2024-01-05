package edu.nm.RabbitRedis.service;

import edu.nm.RabbitRedis.config.RedisLock;
import edu.nm.RabbitRedis.domain.Task;
import edu.nm.RabbitRedis.rabbit.TaskSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class TaskInitializerService {

    private static final long ONE_MINUTE_IN_MILLIS = 1000 * 60;
    private static final String TASKS_KEY = "horizontal:scaling:generate:tasks";
    private static final String SERVER_ID = UUID.randomUUID().toString();

    TaskSender taskSender;
    RedisLock redisLock;

    @Scheduled(fixedDelay = 1000L) //use cron
    public void generateTasks() {
        if (redisLock.acquireLock(ONE_MINUTE_IN_MILLIS, TASKS_KEY)) {
            log.info(Strings.repeat("-", 100));
            log.info(String.format("Server \"%s\" start generate tasks", SERVER_ID));
            for (int i = 0; i < 5; i++) {
                taskSender.sendTask(
                        Task.builder()
                                .id(UUID.randomUUID().toString())
                                .fromServer(SERVER_ID)
                                .build()
                );
            }
            redisLock.releaseLock(TASKS_KEY);
        }
    }
}
