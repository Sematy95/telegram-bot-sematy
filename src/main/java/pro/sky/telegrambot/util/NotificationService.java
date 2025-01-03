package pro.sky.telegrambot.util;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final TelegramBotUpdatesListener listener;

    private final TaskRepository taskRepository;

    public NotificationService(TelegramBotUpdatesListener listener, TaskRepository taskRepository) {
        this.listener = listener;
        this.taskRepository = taskRepository;
    }

    public void sendNotificationByDate() {
        List<NotificationTask> tasks = taskRepository.findByNotificationDateBetween(
                LocalDateTime.now().minusSeconds(60),
                LocalDateTime.now());
        Set<SendMessage> messages = tasks.stream()
                .map(t -> new SendMessage(t.getChatId(), t.getMessage()))
                .collect(Collectors.toSet());
        listener.execute(messages);
    }

}

