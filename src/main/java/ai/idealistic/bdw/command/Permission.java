package ai.idealistic.bdw.command;

import ai.idealistic.bdw.Register;
import lombok.Getter;

public enum Permission {
    TOGGLE, RELOAD, ADMIN;

    @Getter
    private final String key;

    Permission() {
        key = Register.command + "." + this.name().toLowerCase();
    }

}
