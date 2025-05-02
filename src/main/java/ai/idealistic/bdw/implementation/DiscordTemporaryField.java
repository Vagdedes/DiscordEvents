package ai.idealistic.bdw.implementation;

import lombok.Getter;
import lombok.Setter;

public class DiscordTemporaryField {

    @Getter
    @Setter
    private String
            name,
            value;
    @Getter
    private final boolean inline;

    DiscordTemporaryField(DiscordField field) {
        this.name = field.getName();
        this.value = field.getValue();
        this.inline = field.isInline();
    }

}
