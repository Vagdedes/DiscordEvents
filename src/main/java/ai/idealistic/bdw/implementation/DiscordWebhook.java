package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.utils.AwarenessNotifications;
import ai.idealistic.bdw.utils.JavaURL;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DiscordWebhook {

    private static final int TIME_OUT_SECONDS = 3;
    private static final String CONNECTION_METHOD = "POST";

    @Getter
    private final Class<?> event;
    @Getter
    private final String
            webhookURL,
            avatarURL,
            color,
            authorName,
            authorURL,
            authorIconURL,
            titleName,
            titleURL,
            description,
            footerName,
            footerIconURL,
            content;
    @Getter
    private final Map<String, String> fields;
    private final ConfigurationBuilder config;

    public DiscordWebhook(
            @NotNull Class<?> event,
            @NotNull String webhookURL,
            @NotNull String avatarURL,
            @NotNull String color,
            @NotNull String authorName,
            @NotNull String authorURL,
            @NotNull String authorIconURL,
            @NotNull String titleName,
            @NotNull String titleURL,
            @NotNull String description,
            @NotNull String footerName,
            @NotNull String footerIconURL,
            @NotNull Map<String, String> fields,
            @NotNull String content
    ) {
        this.event = event;
        this.webhookURL = webhookURL;
        this.avatarURL = avatarURL;
        this.color = color;
        this.authorName = authorName;
        this.authorURL = authorURL;
        this.authorIconURL = authorIconURL;
        this.titleName = titleName;
        this.titleURL = titleURL;
        this.description = description;
        this.footerName = footerName;
        this.footerIconURL = footerIconURL;
        this.fields = fields;
        this.content = content;
        this.config = new ConfigurationBuilder(this);
    }

    public boolean validate() {
        if (webhookURL == null
                || webhookURL.isEmpty()) {
            AwarenessNotifications.send(
                    "Webhook URL of event '"
                            + event.getName()
                            + "' is empty"
            );
            return false;
        } else if (!JavaURL.isValid(webhookURL, true)) {
            AwarenessNotifications.send(
                    "Webhook URL '"
                            + webhookURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(avatarURL, true)) {
            AwarenessNotifications.send(
                    "Avatar URL '"
                            + avatarURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorURL, true)) {
            AwarenessNotifications.send(
                    "Author URL '"
                            + authorURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorIconURL, true)) {
            AwarenessNotifications.send(
                    "Author icon URL '"
                            + authorIconURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(titleURL, true)) {
            AwarenessNotifications.send(
                    "Title URL '"
                            + titleURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(footerIconURL, true)) {
            AwarenessNotifications.send(
                    "Footer icon URL '"
                            + footerIconURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (color != null
                && color.length() != 6) {
            AwarenessNotifications.send(
                    "Color '"
                            + color
                            + "' of event '"
                            + event.getName()
                            + "' is invalid, must be 6 characters long"
            );
            return false;
        } else if (content != null
                && content.length() > 2000) {
            AwarenessNotifications.send(
                    "Content of event '"
                            + event.getName()
                            + "' is too long, must be 2000 characters or less"
            );
            return false;
        } else if (description != null
                && description.length() > 2048) {
            AwarenessNotifications.send(
                    "Description of event '"
                            + event.getName()
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (footerName != null
                && footerName.length() > 2048) {
            AwarenessNotifications.send(
                    "Footer name of event '"
                            + event.getName()
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (fields != null
                && fields.size() > 25) {
            AwarenessNotifications.send(
                    "Fields of event '"
                            + event.getName()
                            + "' are too many, must be less 25 or less"
            );
            return false;
        } else {
            return true;
        }
    }

    public boolean send(Event event) {
        if (validate()) {
            // todo connection
            return true;
        } else {
            return false;
        }
    }

    public void reload() {

    }

}
