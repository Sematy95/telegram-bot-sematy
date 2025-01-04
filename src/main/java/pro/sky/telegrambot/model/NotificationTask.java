package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatId;
    private String message;
    private LocalDateTime creationDate;
    private LocalDateTime notificationDate;

    public NotificationTask() {
    }

    public NotificationTask(String chatId, String message, LocalDateTime notificationDate) {
        this.chatId = chatId;
        this.message = message;
        this.notificationDate = notificationDate;
        creationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(creationDate, that.creationDate) && Objects.equals(notificationDate, that.notificationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, message, creationDate, notificationDate);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId='" + chatId + '\'' +
                ", message='" + message + '\'' +
                ", creationDate=" + creationDate +
                ", notificationDate=" + notificationDate +
                '}';
    }
}
