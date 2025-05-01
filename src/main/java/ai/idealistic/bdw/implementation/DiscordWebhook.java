package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.utils.AwarenessNotifications;
import ai.idealistic.bdw.utils.JavaURL;
import ai.idealistic.bdw.utils.RequestUtils;
import ai.idealistic.bdw.utils.WebhookThread;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class DiscordWebhook {

    public static final int MAX_WEBHOOK_URLS = 10;
    public static final int MAX_FIELDS = 25;
    private static final int TIME_OUT_MILLISECONDS = 3_000;
    private static final String CONNECTION_METHOD = "POST";

    @Getter
    private final String pluginName;
    private final Class<?> event;
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
    private boolean enabled;
    private final Collection<String> webhookURLs;
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

    private boolean validate() {
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
                                + getEventName()
                                + "' is empty"
                );
                return false;
            }
            if (!JavaURL.isValid(url, true)) {
                AwarenessNotifications.send(
                        this.invalidation = "Webhook URL '"
                                + url
                                + "' of event '"
                                + getEventName()
                                + "' is invalid"
                );
                return false;
            }
        }
        if (fields.size() > MAX_FIELDS) {
            AwarenessNotifications.send(
                    this.invalidation = "Fields of event '"
                            + getEventName()
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
                                    + getEventName()
                                    + "' is too long, must be 256 characters or less"
                    );
                    return false;
                }
                if (field.getValue().length() > 1024) {
                    AwarenessNotifications.send(
                            this.invalidation = "Field '"
                                    + count
                                    + "' value of event '"
                                    + getEventName()
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
                            + getEventName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Author URL '"
                            + authorURL
                            + "' of event '"
                            + getEventName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorIconURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Author icon URL '"
                            + authorIconURL
                            + "' of event '"
                            + getEventName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(titleURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Title URL '"
                            + titleURL
                            + "' of event '"
                            + getEventName()
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(footerIconURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer icon URL '"
                            + footerIconURL
                            + "' of event '"
                            + getEventName()
                            + "' is invalid"
            );
            return false;
        } else if (color != null
                && color.length() != 6) {
            AwarenessNotifications.send(
                    this.invalidation = "Color '"
                            + color
                            + "' of event '"
                            + getEventName()
                            + "' is invalid, must be 6 characters long"
            );
            return false;
        } else if (content != null
                && content.length() > 2000) {
            AwarenessNotifications.send(
                    this.invalidation = "Content of event '"
                            + getEventName()
                            + "' is too long, must be 2000 characters or less"
            );
            return false;
        } else if (description != null
                && description.length() > 4096) {
            AwarenessNotifications.send(
                    this.invalidation = "Description of event '"
                            + getEventName()
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (footer != null
                && footer.length() > 2048) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer name of event '"
                            + getEventName()
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (title != null
                && title.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Title of event '"
                            + getEventName()
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (author != null
                && author.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Author name of event '"
                            + getEventName()
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (webhookURLs.size() > MAX_WEBHOOK_URLS) {
            AwarenessNotifications.send(
                    this.invalidation = "Webhook URLs of event '"
                            + getEventName()
                            + "' are too many, must be less than 10"
            );
            return false;
        } else {
            return true;
        }
    }

    public void send(Event event) {
        WebhookThread.run(() -> {
            if (enabled
                    && validate()) {
                for (String url : webhookURLs) {
                    try {
                        StringBuilder fields = new StringBuilder();
                        fields.append("[");
                        int count = 0;

                        for (DiscordField field : this.fields) {
                            count++;
                            fields.append("{\"name\":\"")
                                    .append(field.getName())
                                    .append("\",\"value\":\"")
                                    .append(field.getValue())
                                    .append("\",\"inline\":")
                                    .append(field.isInline())
                                    .append("}");

                            if (count < this.fields.size()) {
                                fields.append(",");
                            }
                        }
                        fields.append("]");

                        String[] strings = RequestUtils.get(
                                "https://www.vagdedes.com/minecraft/cloud/?"
                                        + "identification=BuiltByBit|%%__USER__%%|%%__NONCE__%%"
                                        + "&action=add"
                                        + "&data=advancedDiscordWebhook"
                                        + "&version=" + Register.plugin.getDescription().getVersion()
                                        + "&webhook_url=" + URLEncoder.encode(url, "UTF-8")
                                        + "&avatar_url=" + URLEncoder.encode(avatarURL, "UTF-8")
                                        + "&color=" + URLEncoder.encode(color, "UTF-8")
                                        + "&author=" + URLEncoder.encode(author, "UTF-8")
                                        + "&author_url=" + URLEncoder.encode(authorURL, "UTF-8")
                                        + "&author_icon_url=" + URLEncoder.encode(authorIconURL, "UTF-8")
                                        + "&title=" + URLEncoder.encode(title, "UTF-8")
                                        + "&title_url=" + URLEncoder.encode(titleURL, "UTF-8")
                                        + "&description=" + URLEncoder.encode(description, "UTF-8")
                                        + "&footer=" + URLEncoder.encode(footer, "UTF-8")
                                        + "&footer_icon_url=" + URLEncoder.encode(footerIconURL, "UTF-8")
                                        + "&content=" + URLEncoder.encode(content, "UTF-8")
                                        + "&fields=" + URLEncoder.encode(fields.toString(), "UTF-8"),
                                CONNECTION_METHOD,
                                TIME_OUT_MILLISECONDS
                        );

                        if (strings.length > 0) {
                            if (!strings[0].equals("true")) {
                                AwarenessNotifications.send(strings[0]);
                            }
                        } else {
                            AwarenessNotifications.send("...");
                        }
                    } catch (Exception ex) {
                        AwarenessNotifications.send(ex.getMessage());
                    }
                }
            }
        });
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
        this.webhookURLs.clear();

        for (int i = 1; i <= DiscordWebhook.MAX_WEBHOOK_URLS; i++) {
            String url = this.config.getString("webhook.url." + i);

            if (url != null
                    && !url.isEmpty()) {
                this.webhookURLs.add(url);
            }
        }
        this.fields.clear();

        for (int i = 1; i <= DiscordWebhook.MAX_FIELDS; i++) {
            String name = this.config.getString("webhook.field." + i + ".name");
            String value = this.config.getString("webhook.field." + i + ".value");
            boolean inline = this.config.getBoolean("webhook.field." + i + ".inline");

            if (name != null
                    && !name.isEmpty()
                    && value != null
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
