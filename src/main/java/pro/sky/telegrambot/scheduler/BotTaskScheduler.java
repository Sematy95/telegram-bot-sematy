package pro.sky.telegrambot.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.util.NotificationService;

@Component
public class BotTaskScheduler {

    private final NotificationService notificationService;

    public BotTaskScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void execute() {
        notificationService.sendNotificationByDate();
    }
}
