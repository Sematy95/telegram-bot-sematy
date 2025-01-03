package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.TaskRepository;

import javax.annotation.PostConstruct;
import java.util.List;

import static pro.sky.telegrambot.util.TelegramBotUtil.START;
import static pro.sky.telegrambot.util.TelegramBotUtil.START_MESSAGE;

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

            Long chatId = update.message().chat().id();
            SendMessage sendMessage = null;

            if (text.equals(START)) {
                sendMessage = new SendMessage(chatId, START_MESSAGE);
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

}
