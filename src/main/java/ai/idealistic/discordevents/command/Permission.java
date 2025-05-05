package ai.idealistic.discordevents.command;

import ai.idealistic.discordevents.Register;
import lombok.Getter;

public enum Permission {
    TOGGLE, RELOAD, OPTION, ADMIN;

    @Getter
    private final String key;

    Permission() {
        key = Register.command + "." + this.name().toLowerCase();
    }

}
