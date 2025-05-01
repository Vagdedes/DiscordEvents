package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.utils.AwarenessNotifications;
import ai.idealistic.bdw.utils.JavaURL;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class DiscordWebhook {

    public static final int MAX_WEBHOOK_URLS = 10;
    public static final int MAX_FIELDS = 25;
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
    private boolean enabled;
    @Getter
    private final Collection<String> webhookURLs;
    @Getter
    private final Collection<DiscordField> fields;
    private final ConfigurationBuilder config;

    public DiscordWebhook(
            @NotNull String pluginName,
            @NotNull Class<?> event
    ) {
        this.pluginName = pluginName;
        this.event = event;
        this.webhookURLs = new HashSet<>(1);
        this.fields = new ArrayList<>(1);
        this.config = new ConfigurationBuilder(this);
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
        if (fields.size() > MAX_FIELDS) {
            AwarenessNotifications.send(
                    this.invalidation = "Fields of event '"
                            + event.getName()
                            + "' are too many, must be less 25 or less"
            );
            return false;
        } else {
            int count = 0;

            for (DiscordField field : fields) {
                count++;

                if (field.getName().length() > 256) {
                    AwarenessNotifications.send(
                            this.invalidation = "Field '"
                                    + count
                                    + "' name of event '"
                                    + event.getName()
                                    + "' is too long, must be 256 characters or less"
                    );
                    return false;
                }
                if (field.getValue().length() > 1024) {
                    AwarenessNotifications.send(
                            this.invalidation = "Field '"
                                    + count
                                    + "' value of event '"
                                    + event.getName()
                                    + "' is too long, must be 1024 characters or less"
                    );
                    return false;
                }
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
                && description.length() > 4096) {
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
        } else if (title != null
                && title.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Title of event '"
                            + event.getName()
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (author != null
                && author.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Author name of event '"
                            + event.getName()
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (webhookURLs.size() > MAX_WEBHOOK_URLS) {
            AwarenessNotifications.send(
                    this.invalidation = "Webhook URLs of event '"
                            + event.getName()
                            + "' are too many, must be less than 10"
            );
            return false;
        } else {
            return true;
        }
    }

    public boolean send(Event event) {
        if (enabled
                && validate()) {
            // todo connection
            return true;
        } else {
            return false;
        }
    }

    public void reload() {
        this.enabled = this.config.getBoolean("options.enabled");

        this.avatarURL = this.config.getString("webhook.avatar_url");
        this.color = this.config.getString("webhook.color");
        this.author = this.config.getString("webhook.author");
        this.authorURL = this.config.getString("webhook.author_url");
        this.authorIconURL = this.config.getString("webhook.author_icon_url");
        this.title = this.config.getString("webhook.title");
        this.titleURL = this.config.getString("webhook.title_url");
        this.description = this.config.getString("webhook.description");
        this.footer = this.config.getString("webhook.footer");
        this.footerIconURL = this.config.getString("webhook.footer_icon_url");
        this.content = this.config.getString("webhook.content");

        for (int i = 1; i <= DiscordWebhook.MAX_WEBHOOK_URLS; i++) {
            String url = this.config.getString("webhook.url." + i);

            if (url != null && !url.isEmpty()) {
                this.webhookURLs.add(url);
            }
        }
        for (int i = 1; i <= DiscordWebhook.MAX_FIELDS; i++) {
            String name = this.config.getString("webhook.field." + i + ".name");
            String value = this.config.getString("webhook.field." + i + ".value");
            boolean inline = this.config.getBoolean("webhook.field." + i + ".inline");

            if (name != null
                    && !name.isEmpty()
                    && !value.isEmpty()) {
                this.fields.add(new DiscordField(name, value, inline));
            }
        }
        this.invalidation = null;
    }

    public String getEventName() {
        return event.getSimpleName();
    }

}
