package ai.idealistic.bdw.functionality;

import org.bukkit.World;

import java.sql.Timestamp;
import java.util.Arrays;

public class EventPlaceholders {

    private static String prepare(
            String prefix, String subfix, String type
    ) {
        return "{" + prefix + ":" + (!subfix.isEmpty() ? subfix + "-" + type : type) + "}";
    }

    private static Object hasWorldMethod(World world, String methodName) {
        try {
            return world.getClass().getMethod(methodName).invoke(world);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getWorld(World world, String prefix, String subfix, String string) {
        string = string.replace(
                prepare(prefix, subfix, "name"),
                world.getName()
        ).replace(
                prepare(prefix, subfix, "spawn-x"),
                String.valueOf(world.getSpawnLocation().getX())
        ).replace(
                prepare(prefix, subfix, "spawn-y"),
                String.valueOf(world.getSpawnLocation().getY())
        ).replace(
                prepare(prefix, subfix, "spawn-z"),
                String.valueOf(world.getSpawnLocation().getZ())
        ).replace(
                prepare(prefix, subfix, "difficulty"),
                world.getDifficulty().name().toLowerCase()
        ).replace(
                prepare(prefix, subfix, "hardcore"),
                world.isHardcore() ? "Hardcore" : "Not Hardcore"
        ).replace(
                prepare(prefix, subfix, "environment"),
                world.getEnvironment().name().toLowerCase()
        ).replace(
                prepare(prefix, subfix, "seed"),
                String.valueOf(world.getSeed())
        ).replace(
                prepare(prefix, subfix, "time"),
                new Timestamp(world.getTime()).toString().substring(0, 19)
        ).replace(
                prepare(prefix, subfix, "full-time"),
                new Timestamp(world.getFullTime()).toString().substring(0, 19)
        ).replace(
                prepare(prefix, subfix, "game-time"),
                new Timestamp(world.getGameTime()).toString().substring(0, 19)
        ).replace(
                prepare(prefix, subfix, "weather"),
                world.isThundering()
                        ? "Thundering Weather"
                        : world.hasStorm()
                        ? "Storm Weather"
                        : "Clear Weather"
        ).replace(
                prepare(prefix, subfix, "is-thundering"),
                world.isThundering()
                        ? "Thundering Weather"
                        : "Clear Weather"
        ).replace(
                prepare(prefix, subfix, "has-storm"),
                world.hasStorm()
                        ? "Storm Weather"
                        : "Clear Weather"
        ).replace(
                prepare(prefix, subfix, "allow-animals"),
                world.getAllowAnimals()
                        ? "Animals Allowed"
                        : "No Animals"
        ).replace(
                prepare(prefix, subfix, "allow-monsters"),
                world.getAllowMonsters()
                        ? "Monsters Allowed"
                        : "No Monsters"
        ).replace(
                prepare(prefix, subfix, "game-rules"),
                Arrays.toString(world.getGameRules())
        );

        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
            string = string.replace(
                    prepare(prefix, subfix, "border-size"),
                    Double.toString(world.getWorldBorder().getSize())
            );
        }

        Object object = hasWorldMethod(world, "hasRaids");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(prefix, subfix, "has-raids"),
                    object == Boolean.TRUE
                            ? "Raids"
                            : "No Raids"
            );
        }
        object = hasWorldMethod(world, "getPVP");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(prefix, subfix, "pvp-allowed"),
                    object == Boolean.TRUE
                            ? "PvP Allowed"
                            : "No PvP"
            );
        }
        object = hasWorldMethod(world, "hasSkyLight");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(prefix, subfix, "has-sky-light"),
                    object == Boolean.TRUE
                            ? "Sky Light"
                            : "No Sky Light"
            );
        }
        return string;
    }

}
