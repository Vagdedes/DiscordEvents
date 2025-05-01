package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.utils.AwarenessNotifications;
import ai.idealistic.bdw.utils.JavaURL;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DiscordWebhook {

    private static final int TIME_OUT_SECONDS = 3;
    private static final String CONNECTION_METHOD = "POST";

    @Getter
    private final String pluginName;
    @Getter
    private final Class<?> event;
    @Getter
    private String
            avatarURL,
            color,
            author,
            authorURL,
            authorIconURL,
            title,
            titleURL,
            description,
            footer,
            footerIconURL,
            content,
            invalidation;
    @Getter
    private final Collection<String> webhookURLs;
    @Getter
    private final Map<String, String> fields;
    private final ConfigurationBuilder config;

    public DiscordWebhook(
            @NotNull String pluginName,
            @NotNull Class<?> event
    ) {
        this.pluginName = pluginName;
        this.event = event;
        this.webhookURLs = new HashSet<>(1);
        this.fields = new HashMap<>(1);
        this.config = new ConfigurationBuilder(this);
        this.invalidation = null;
        this.reload();
    }

    public boolean validate() {
        if (webhookURLs.isEmpty()) {
            return false;
        }
        if (this.invalidation != null) {
            AwarenessNotifications.send(this.invalidation);
            return false;
        }
        for (String url : webhookURLs) {
            if (url == null || url.isEmpty()) {
                AwarenessNotifications.send(
                        this.invalidation = "Webhook URL of event '"
                                + event.getName()
                                + "' is empty"
                );
                return false;
            }
            if (!JavaURL.isValid(url, true)) {
                AwarenessNotifications.send(
                        this.invalidation = "Webhook URL '"
                                + url
                                + "' of event '"
                                + event.getName()
                                + "' is invalid"
                );
                return false;
            }
        }
        if (!JavaURL.isValid(avatarURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Avatar URL '"
                            + avatarURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Author URL '"
                            + authorURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorIconURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Author icon URL '"
                            + authorIconURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(titleURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Title URL '"
                            + titleURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(footerIconURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer icon URL '"
                            + footerIconURL
                            + "' of event '"
                            + event.getName()
                            + "' is invalid"
            );
            return false;
        } else if (color != null
                && color.length() != 6) {
            AwarenessNotifications.send(
                    this.invalidation = "Color '"
                            + color
                            + "' of event '"
                            + event.getName()
                            + "' is invalid, must be 6 characters long"
            );
            return false;
        } else if (content != null
                && content.length() > 2000) {
            AwarenessNotifications.send(
                    this.invalidation = "Content of event '"
                            + event.getName()
                            + "' is too long, must be 2000 characters or less"
            );
            return false;
        } else if (description != null
                && description.length() > 2048) {
            AwarenessNotifications.send(
                    this.invalidation = "Description of event '"
                            + event.getName()
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (footer != null
                && footer.length() > 2048) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer name of event '"
                            + event.getName()
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (fields.size() > 25) {
            AwarenessNotifications.send(
                    this.invalidation = "Fields of event '"
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
        this.avatarURL = this.config.getString("avatar_url");
        this.color = this.config.getString("color");
        this.author = this.config.getString("author");
        this.authorURL = this.config.getString("author_url");
        this.authorIconURL = this.config.getString("author_icon_url");
        this.title = this.config.getString("title");
        this.titleURL = this.config.getString("title_url");
        this.description = this.config.getString("description");
        this.footer = this.config.getString("footer");
        this.footerIconURL = this.config.getString("footer_icon_url");
        this.content = this.config.getString("content");

        // todo webhook urls
        // todo fields
    }

}
