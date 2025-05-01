package ai.idealistic.bdw.functionality.placeholders;

import org.bukkit.entity.Entity;

public class EntityPlaceholders {

    public static String entityType(Entity entity) {
        return entity.getType().name().toLowerCase();
    }

    public static String entityName(Entity entity) {
        return entity.getCustomName() != null
                ? entity.getCustomName()
                : entity.getType().name().toLowerCase();
    }

    // todo

}
