package ai.idealistic.discordevents.implementation;

import lombok.Getter;

public class DiscordField {

    @Getter
    private final String
            name,
            value;
    @Getter
    private final boolean inline;

    DiscordField(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

}
