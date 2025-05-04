package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.functionality.EventPlaceholders;
import ai.idealistic.bdw.functionality.EventReader;
import ai.idealistic.bdw.utils.*;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private String
            trueText,
            falseText,
            nullText,
            notImplementedText;
    @Getter
    private boolean enabled;
    private boolean
            escapeMarkdownBeforeMethodCall,
            escapeMarkdownAfterMethodCall;
    private double cooldown;
    private long expiration;
    private final Map<String, Boolean> methodCallBlacklist;
    private final Map<String, Boolean> webhookURLs;
    private final Map<Integer, DiscordField> fields;
    private final ConfigurationBuilder config;
    private Map<String, Method> methods;

    public DiscordWebhook(
            @NotNull String pluginName,
            @NotNull Class<?> event
    ) {
        this.pluginName = pluginName;
        this.event = event;
        this.webhookURLs = new ConcurrentHashMap<>(0);
        this.fields = new ConcurrentHashMap<>(0);
        this.config = new ConfigurationBuilder(this);
        this.methodCallBlacklist = new ConcurrentHashMap<>(0);
        this.methods = null;
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
            if (this.invalidation.isEmpty()) {
                return true;
            } else {
                AwarenessNotifications.send(this.invalidation);
                return false;
            }
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
        for (String url : webhookURLs.keySet()) {
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
        int sum = author.length()
                + title.length()
                + description.length()
                + footer.length();

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

            if (this.fields.isEmpty()) {
                if (sum > 6000) {
                    AwarenessNotifications.send(
                            this.invalidation = "Fields of the event '"
                                    + getEventName()
                                    + " (" + pluginName + ")"
                                    + "' are too long, must be 6000 characters or less"
                    );
                    return false;
                }
            } else {
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
                    sum += field.getName().length() + field.getValue().length();

                    if (sum > 6000) {
                        AwarenessNotifications.send(
                                this.invalidation = "Fields of the event '"
                                        + getEventName()
                                        + " (" + pluginName + ")"
                                        + "' are too long, must be 6000 characters or less"
                        );
                        return false;
                    }
                }
            }
        }
        if (color != null
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
        } else if (description.length() > 4096) {
            AwarenessNotifications.send(
                    this.invalidation = "Description of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (footer.length() > 2048) {
            AwarenessNotifications.send(
                    this.invalidation = "Footer name of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 2048 characters or less"
            );
            return false;
        } else if (title.length() > 256) {
            AwarenessNotifications.send(
                    this.invalidation = "Title of the event '"
                            + getEventName()
                            + " (" + pluginName + ")"
                            + "' is too long, must be 256 characters or less"
            );
            return false;
        } else if (author.length() > 256) {
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
            this.invalidation = "";
            return true;
        }
    }

    private void loadMethods() {
        Map<String, Method> methods = EventReader.getValidMethods(event);
        this.methods = methods;

        if (!methods.isEmpty()) {
            Iterator<Map.Entry<String, Method>> iterator = methods.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Method> entry = iterator.next();
                String methodName = entry.getKey();
                String[] placeholders = EventReader.getPlaceholders(
                        methodName,
                        entry.getValue().getReturnType()
                );

                if (placeholders.length == 0) {
                    config.setOption(
                            "information.placeholders." + methodName,
                            null
                    );
                    iterator.remove();
                } else {
                    config.setOption(
                            "information.placeholders." + methodName,
                            ArrayReader.toString(placeholders)
                    );
                }
            }
        }
        Set<String> keys = this.config.getKeys("information.placeholders", false);

        if (!keys.isEmpty()) {
            for (String key : keys) {
                if (!methods.containsKey(key)) {
                    config.setOption(
                            "information.placeholders." + key,
                            null
                    );
                }
            }
        }
    }

    public void send(Event event) {
        if (enabled) {
            WebhookThread.run(() -> {
                if (!validate()) {
                    return;
                }
                if (cooldown > 0.0) {
                    if (expiration > System.currentTimeMillis()) {
                        return;
                    }
                    expiration = System.currentTimeMillis() + Math.round(cooldown * 1_000L);
                }
                List<DiscordTemporaryField> fieldsArray = new ArrayList<>(this.fields.size());

                if (!this.fields.isEmpty()) {
                    for (DiscordField field : this.fields.values()) {
                        DiscordTemporaryField tmpField = new DiscordTemporaryField(field);
                        fieldsArray.add(tmpField);

                        if (this.escapeMarkdownBeforeMethodCall) {
                            tmpField.setName(
                                    DiscordUtils.escapeMarkdown(tmpField.getName())
                            );
                            tmpField.setValue(
                                    DiscordUtils.escapeMarkdown(tmpField.getValue())
                            );
                        }
                    }
                }
                String avatarURL = this.avatarURL;
                String color = this.color;
                String author = this.author;
                String authorURL = this.authorURL;
                String authorIconURL = this.authorIconURL;
                String title = this.title;
                String titleURL = this.titleURL;
                String description = this.description;
                String footer = this.footer;
                String footerIconURL = this.footerIconURL;
                String content = this.content;

                if (this.escapeMarkdownBeforeMethodCall) {
                    avatarURL = DiscordUtils.escapeMarkdown(avatarURL);
                    color = DiscordUtils.escapeMarkdown(color);
                    author = DiscordUtils.escapeMarkdown(author);
                    authorURL = DiscordUtils.escapeMarkdown(authorURL);
                    authorIconURL = DiscordUtils.escapeMarkdown(authorIconURL);
                    title = DiscordUtils.escapeMarkdown(title);
                    titleURL = DiscordUtils.escapeMarkdown(titleURL);
                    description = DiscordUtils.escapeMarkdown(description);
                    footer = DiscordUtils.escapeMarkdown(footer);
                    footerIconURL = DiscordUtils.escapeMarkdown(footerIconURL);
                    content = DiscordUtils.escapeMarkdown(content);
                }

                if (!this.methods.isEmpty()) {
                    for (Map.Entry<String, Method> entry : this.methods.entrySet()) {
                        String methodName = entry.getKey();
                        Method method = entry.getValue();
                        Object result;

                        if (methodCallBlacklist.containsKey(methodName)) {
                            result = null;
                        } else {
                            try {
                                result = method.invoke(event);
                            } catch (Exception ignored) {
                                result = null;
                            }
                        }

                        if (!fieldsArray.isEmpty()) {
                            for (DiscordTemporaryField field : fieldsArray) {
                                field.setName(
                                        EventPlaceholders.get(
                                                this,
                                                result,
                                                methodName,
                                                method,
                                                field.getName()
                                        )
                                );
                                field.setValue(
                                        EventPlaceholders.get(
                                                this,
                                                result,
                                                methodName,
                                                method,
                                                field.getValue()
                                        )
                                );
                            }
                        }
                        avatarURL = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                avatarURL
                        );
                        color = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                color
                        );
                        author = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                author
                        );
                        authorURL = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                authorURL
                        );
                        authorIconURL = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                authorIconURL
                        );
                        title = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                title
                        );
                        titleURL = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                titleURL
                        );
                        description = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                description
                        );
                        footer = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                footer
                        );
                        footerIconURL = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                footerIconURL
                        );
                        content = EventPlaceholders.get(
                                this,
                                result,
                                methodName,
                                method,
                                content
                        );
                    }
                }
                JSONArray fields = new JSONArray();

                if (!fieldsArray.isEmpty()) {
                    for (DiscordTemporaryField field : fieldsArray) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", field.getName());
                        jsonObject.put("value", field.getValue());
                        jsonObject.put("inline", field.isInline());
                        fields.put(jsonObject);
                    }
                }
                for (String url : webhookURLs.keySet()) {
                    try {
                        String[] strings = RequestUtils.get(
                                "https://www.vagdedes.com/minecraft/cloud/"
                                        + "?identification=" + IDs.platform() + "|%%__USER__%%|%%__NONCE__%%"
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
                            if (strings.length != 1
                                    || !strings[0].equals("true")) {
                                AwarenessNotifications.send(
                                        "The webserver returned an error response for the event '"
                                                + getEventName()
                                                + " (" + pluginName + ")': "
                                                + ArrayReader.toString(strings)
                                );
                            }
                        } else {
                            AwarenessNotifications.send(
                                    "The webserver returned an empty response for the event '"
                                            + getEventName()
                                            + " (" + pluginName + ")'"
                            );
                        }
                    } catch (Exception ex) {
                        AwarenessNotifications.send(
                                "The code run into an error response for the event '"
                                        + getEventName()
                                        + " (" + pluginName + ")': "
                                        + ex.getMessage());
                    }
                }
            });
        }
    }

    public void reload() {
        this.loadMethods();

        this.config.clear();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.config.setOption("information.last_reload", date);
        this.enabled = this.config.getBoolean("options.enabled");
        this.escapeMarkdownBeforeMethodCall = this.config.getBoolean("options.escape_markdown_before_method_call");
        this.escapeMarkdownAfterMethodCall = this.config.getBoolean("options.escape_markdown_after_method_call");
        this.cooldown = Math.max(this.config.getDouble("options.cooldown"), 0.0);
        this.methodCallBlacklist.clear();
        String[] split = this.config.getString("options.method_call_blacklist").split(
                ",",
                100
        );

        for (String method : split) {
            method = method.trim();

            if (!method.isEmpty()) {
                methodCallBlacklist.put(method, true);
            }
        }

        this.trueText = this.config.getString("text.true");
        this.falseText = this.config.getString("text.false");
        this.nullText = this.config.getString("text.null");
        this.notImplementedText = this.config.getString("text.not_implemented");

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
                this.webhookURLs.put(url, true);
            }
        }
        for (int i = 1; i <= DiscordWebhook.MAX_FIELDS; i++) {
            String name = this.config.getString("webhook.fields." + i + ".name");

            if (name != null
                    && !name.isEmpty()) {
                String value = this.config.getString("webhook.fields." + i + ".value");

                if (value != null
                        && !value.isEmpty()) {
                    boolean inline = this.config.getBoolean("webhook.fields." + i + ".inline");
                    this.fields.put(i, new DiscordField(name, value, inline));
                } else {
                    this.fields.remove(i);
                }
            } else {
                this.fields.remove(i);
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

    public String escapeMarkdownAfterMethodCall(Object object) {
        if (escapeMarkdownAfterMethodCall) {
            return DiscordUtils.escapeMarkdown(object.toString());
        } else {
            return object.toString();
        }
    }

}
