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

    private final static Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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
            if (!checkMessage(update)) {
                logger.warn("Message is not correct: {}", update);
                return;
            }

            Matcher matcher = DATE_VALIDATION_PATTERN.matcher(text);
            String chatId = getChatId(update);
            SendMessage sendMessage = null;

            if (text.equals(START)) {
                sendMessage = new SendMessage(chatId, START_MESSAGE);
            } else if (!matcher.matches()) {
                sendMessage = new SendMessage(chatId, "Request form is incorrect");
            } else if (matcher.matches()) {
                try {
                    sendMessage = new SendMessage(chatId, ANSWER);
                    taskRepository.save(new NotificationTask(
                                    chatId,
                                    matcher.group(3),
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

    private boolean checkMessage(Update update) {
        return update.message() != null && !update.message().text().isBlank();
    }

    public void execute(Collection<SendMessage> sendMessages) {
        sendMessages.forEach(telegramBot::execute);
    }

    public void execute(SendMessage sendMessages) {
        execute(List.of(sendMessages));
    }

    private String getChatId(Update update) {
        if (update.message().chat() == null) {
            throw new IllegalArgumentException("chatId is not exist");
        }
        return String.valueOf(update.message().chat().id());
    }

}
