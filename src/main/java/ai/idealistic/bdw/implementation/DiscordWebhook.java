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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    @Getter
    private boolean enabled;
    private double cooldown;
    private long expiration;
    private final Map<Integer, String> webhookURLs;
    private final Map<Integer, DiscordField> fields;
    private final ConfigurationBuilder config;

    public DiscordWebhook(
            @NotNull String pluginName,
            @NotNull Class<?> event
    ) {
        this.pluginName = pluginName;
        this.event = event;
        this.webhookURLs = new ConcurrentHashMap<>(1);
        this.fields = new ConcurrentHashMap<>(1);
        this.config = new ConfigurationBuilder(this);
        this.avatarURL = "";
        this.color = "";
        this.author = "";
        this.authorURL = "";
        this.authorIconURL = "";
        this.title = "";
        this.titleURL = "";
        this.description = "";
        this.footer = "";
        this.footerIconURL = "";
        this.content = "";
        this.invalidation = null;
        this.enabled = false;
        this.cooldown = 0.0;
    }

    private boolean validate() {
        if (this.invalidation != null) {
            AwarenessNotifications.send(this.invalidation);
            return false;
        }
        if (webhookURLs.isEmpty()) {
            AwarenessNotifications.send(
                    this.invalidation = "Webhook URLs of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' are empty"
            );
            return false;
        }
        for (String url : webhookURLs.values()) {
            if (url == null || url.isEmpty()) {
                AwarenessNotifications.send(
                        this.invalidation = "Webhook URL of the event '"
                                + getEventName()
                                + " (" + pluginName + ")"
                                + "' is empty"
                );
                return false;
            }
            if (!JavaURL.isValid(url, true)) {
                AwarenessNotifications.send(
                        this.invalidation = "Webhook URL '"
                                + url
                                + "' of the event '"
                                + getEventName()
                                + " (" + pluginName + ")"
                                + "' is invalid"
                );
                return false;
            }
        }
        if (fields.size() > MAX_FIELDS) {
            AwarenessNotifications.send(
                    this.invalidation = "Fields of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' are too many, must be less 25 or less"
            );
            return false;
        } else {
            int count = 0;

            for (DiscordField field : fields.values()) {
                count++;

                if (field.getName().length() > 256) {
                    AwarenessNotifications.send(
                            this.invalidation = "Field '"
                                    + count
                                    + "' name of the event '"
                                    + getEventName()
                                    + " (" + pluginName + ")"
                                    + "' is too long, must be 256 characters or less"
                    );
                    return false;
                }
                if (field.getValue().length() > 1024) {
                    AwarenessNotifications.send(
                            this.invalidation = "Field '"
                                    + count
                                    + "' value of the event '"
                                    + getEventName()
                                    + " (" + pluginName + ")"
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
                            + "' of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Author URL '"
                            + authorURL
                            + "' of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(authorIconURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Author icon URL '"
                            + authorIconURL
                            + "' of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(titleURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Title URL '"
                            + titleURL
                            + "' of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is invalid"
            );
            return false;
        } else if (!JavaURL.isValid(footerIconURL, true)) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer icon URL '"
                            + footerIconURL
                            + "' of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is invalid"
            );
            return false;
        } else if (color != null
                && color.length() != 6) {
            AwarenessNotifications.send(
                    this.invalidation = "Color '"
                            + color
                            + "' of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is invalid, must be 6 characters long"
            );
            return false;
        } else if (content != null
                && content.length() > 2000) {
            AwarenessNotifications.send(
                    this.invalidation = "Content of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 2000 characters or less"
            );
            return false;
        } else if (description != null
                && description.length() > 4096) {
            AwarenessNotifications.send(
                    this.invalidation = "Description of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (footer != null
                && footer.length() > 2048) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer name of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (title != null
                && title.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Title of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (author != null
                && author.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Author name of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (webhookURLs.size() > MAX_WEBHOOK_URLS) {
            AwarenessNotifications.send(
                    this.invalidation = "Webhook URLs of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
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
                if (cooldown > 0.0) {
                    if (expiration > System.currentTimeMillis()) {
                        return;
                    }
                    expiration = System.currentTimeMillis() + Math.round(cooldown * 1_000L);
                }
                for (String url : webhookURLs.values()) {
                    try {
                        StringBuilder fields = new StringBuilder();
                        fields.append("[");
                        int count = 0;

                        for (DiscordField field : this.fields.values()) {
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
                            if (strings[0].equals("true")) {
                                AwarenessNotifications.send(strings[0]);
                            }
                        } else {
                            AwarenessNotifications.send(
                                    "The webserver returned an empty response for the event '"
                                            + getEventName()
                                            + "' of the plugin '"
                                            + pluginName
                                            + "'"
                            );
                        }
                    } catch (Exception ex) {
                        AwarenessNotifications.send(ex.getMessage());
                    }
                }
            }
        });
    }

    public void reload() {
        this.config.clear();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.config.setOption("information.last_reload", date);
        this.enabled = this.config.getBoolean("options.enabled");
        this.cooldown = Math.max(this.config.getDouble("options.cooldown"), 0.0);

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

            if (url != null
                    && !url.isEmpty()
                    && !this.webhookURLs.containsValue(url)) {
                this.webhookURLs.put(i, url);
            } else {
                this.webhookURLs.remove(i);
            }
        }
        for (int i = 1; i <= DiscordWebhook.MAX_FIELDS; i++) {
            String name = this.config.getString("webhook.field." + i + ".name");

            if (name != null
                    && !name.isEmpty()) {
                String value = this.config.getString("webhook.field." + i + ".value");

                if (value != null
                        && !value.isEmpty()) {
                    boolean inline = this.config.getBoolean("webhook.field." + i + ".inline");
                    this.fields.put(i, new DiscordField(name, value, inline));
                } else {
                    this.fields.remove(i);
                }
            } else {
                this.fields.remove(i - 1);
            }
        }
        this.invalidation = null;
    }

    public String getEventName() {
        return event.getSimpleName();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.config.setOption("options.enabled", enabled);
    }

    public boolean setOptionIfExists(String optionName, String optionValue) {
        if (this.config.setOptionIfExists(optionName, optionValue)) {
            this.reload();
            return true;
        } else {
            return false;
        }
    }

}
