package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.TaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import static pro.sky.telegrambot.util.TelegramBotUtil.*;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TaskRepository taskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TaskRepository taskRepository) {
        this.telegramBot = telegramBot;
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            String text = update.message().text();

            logger.info("Processing update: {}", update);
            if (!checkMessage(text)) {
                logger.warn("Message is not correct: {}", update);
                return;
            }
            Matcher matcher = DATE_VALIDATION_PATTERN.matcher(text);
            String chatId = update.message().chat().id().toString();
            SendMessage sendMessage = null;

            if (text.equals(START)) {
                sendMessage = new SendMessage(chatId, START_MESSAGE);
            } else if (matcher.matches()) {
                try {

                    sendMessage = new SendMessage(chatId, "Request is received");
                    taskRepository.save(new NotificationTask(
                                    chatId,
                                    matcher.group(2),
                                    LocalDateTime.parse(matcher.group(1), DATE_FORMATTER)
                            )
                    );

                } catch (DateTimeParseException e) {
                    logger.error("[{}]", e.getMessage());
                    sendMessage = new SendMessage(chatId, "Invalid date format");
                }

            }
            if (sendMessage != null) {
                telegramBot.execute(sendMessage);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }

    private boolean checkMessage(String text) {
        if (text.isBlank() || text.isEmpty()) {
            logger.warn("Message is empty");
            return false;
        } else return true;
    }

    public void execute (Collection<SendMessage> sendMessages) {
        sendMessages.forEach(telegramBot::execute);
    }
    public void execute(SendMessage sendMessages) {
        execute(List.of(sendMessages));
    }

}
