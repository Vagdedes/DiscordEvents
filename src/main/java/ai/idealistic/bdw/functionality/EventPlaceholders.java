package ai.idealistic.bdw.functionality;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.Arrays;

public class EventPlaceholders {

    static final String[]
            worldPlaceholders = {
            ":world:name}",
            ":world:spawn-x}",
            ":world:spawn-y}",
            ":world:spawn-z}",
            ":world:difficulty}",
            ":world:hardcore}",
            ":world:environment}",
            ":world:seed}",
            ":world:time}",
            ":world:full-time}",
            ":world:game-time}",
            ":world:weather}",
            ":world:is-thundering}",
            ":world:has-storm}",
            ":world:allow-animals}",
            ":world:allow-monsters}",
            ":world:game-rules}",
            ":world:border-size}",
            ":world:has-raids}",
            ":world:pvp-allowed}",
            ":world:has-sky-light}"
    },
            locationPlaceholders = {

            },
            blockPlaceholders = {

            },
            itemPlaceholders = {

            },
            materialPlaceholders = {
            },
            entityPlaceholders = {
            },
            livingEntityPlaceholders = {
            },
            playerPlaceholders = {
            };

    static String[] getPlaceholders(String method, String[] placeholders) {
        String[] result = new String[placeholders.length];

        for (int i = 0; i < placeholders.length; i++) {
            result[i] = "{" + method + placeholders[i];
        }
        return result;
    }

    private static String prepare(
            String method,
            String prefix,
            String type
    ) {
        return "{" + method.toLowerCase() + ":" + prefix + ":" + type + "}";
    }

    private static Object hasWorldMethod(World world, String methodName) {
        try {
            return world.getClass().getMethod(methodName).invoke(world);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getWorld(
            World world,
            String method,
            String string
    ) {
        String prefix = "world";

        string = string.replace(
                prepare(method, prefix, "name"),
                world.getName()
        ).replace(
                prepare(method, prefix, "spawn-x"),
                String.valueOf(world.getSpawnLocation().getX())
        ).replace(
                prepare(method, prefix, "spawn-y"),
                String.valueOf(world.getSpawnLocation().getY())
        ).replace(
                prepare(method, prefix, "spawn-z"),
                String.valueOf(world.getSpawnLocation().getZ())
        ).replace(
                prepare(method, prefix, "difficulty"),
                world.getDifficulty().name().toLowerCase()
        ).replace(
                prepare(method, prefix, "hardcore"),
                world.isHardcore() ? "Hardcore" : "Not Hardcore"
        ).replace(
                prepare(method, prefix, "environment"),
                world.getEnvironment().name().toLowerCase()
        ).replace(
                prepare(method, prefix, "seed"),
                String.valueOf(world.getSeed())
        ).replace(
                prepare(method, prefix, "time"),
                new Timestamp(world.getTime()).toString().substring(0, 19)
        ).replace(
                prepare(method, prefix, "full-time"),
                new Timestamp(world.getFullTime()).toString().substring(0, 19)
        ).replace(
                prepare(method, prefix, "game-time"),
                new Timestamp(world.getGameTime()).toString().substring(0, 19)
        ).replace(
                prepare(method, prefix, "weather"),
                world.isThundering()
                        ? "Thundering Weather"
                        : world.hasStorm()
                        ? "Storm Weather"
                        : "Clear Weather"
        ).replace(
                prepare(method, prefix, "is-thundering"),
                world.isThundering()
                        ? "Thundering Weather"
                        : "Clear Weather"
        ).replace(
                prepare(method, prefix, "has-storm"),
                world.hasStorm()
                        ? "Storm Weather"
                        : "Clear Weather"
        ).replace(
                prepare(method, prefix, "allow-animals"),
                world.getAllowAnimals()
                        ? "Animals Allowed"
                        : "No Animals"
        ).replace(
                prepare(method, prefix, "allow-monsters"),
                world.getAllowMonsters()
                        ? "Monsters Allowed"
                        : "No Monsters"
        ).replace(
                prepare(method, prefix, "game-rules"),
                Arrays.toString(world.getGameRules())
        );

        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
            string = string.replace(
                    prepare(method, prefix, "border-size"),
                    Double.toString(world.getWorldBorder().getSize())
            );
        }

        Object object = hasWorldMethod(world, "hasRaids");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-raids"),
                    object == Boolean.TRUE
                            ? "Raids"
                            : "No Raids"
            );
        }
        object = hasWorldMethod(world, "getPVP");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "pvp-allowed"),
                    object == Boolean.TRUE
                            ? "PvP Allowed"
                            : "No PvP"
            );
        }
        object = hasWorldMethod(world, "hasSkyLight");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    object == Boolean.TRUE
                            ? "Sky Light"
                            : "No Sky Light"
            );
        }
        return string;
    }

    public static String getLocation(
            Location location,
            String method,
            String string,
            boolean block
    ) {
        String prefix = "location";

        string = string.replace(
                prepare(method, prefix, "world"),
                location.getWorld().getName()
        ).replace(
                prepare(method, prefix, "x"),
                String.valueOf(location.getX())
        ).replace(
                prepare(method, prefix, "y"),
                String.valueOf(location.getY())
        ).replace(
                prepare(method, prefix, "z"),
                String.valueOf(location.getZ())
        ).replace(
                prepare(method, prefix, "yaw"),
                String.valueOf(location.getYaw())
        ).replace(
                prepare(method, prefix, "pitch"),
                String.valueOf(location.getPitch())
        );
        // todo

        string = getWorld(
                location.getWorld(),
                method,
                string
        );
        if (block) {
            string = getBlock(
                    location.getBlock(),
                    method,
                    string
            );
        }
        return string;
    }

    public static String getBlock(
            Block block,
            String method,
            String string
    ) {
        String prefix = "block";

        string = string.replace(
                prepare(method, prefix, "biome"),
                block.getBiome().name().toLowerCase()
        );
        // todo

        string = getLocation(
                block.getLocation(),
                method,
                string,
                false
        );
        string = getMaterial(
                block.getType(),
                method,
                string
        );
        return string;
    }

    public static String getItem(
            ItemStack itemStack,
            String method,
            String string
    ) {
        String prefix = "item";

        string = string.replace(
                prepare(method, prefix, "amount"),
                String.valueOf(itemStack.getAmount())
        );
        // todo

        string = getMaterial(
                itemStack.getType(),
                method,
                string
        );
        return string;
    }

    public static String getMaterial(
            Material material,
            String method,
            String string
    ) {
        String prefix = "material";

        string = string.replace(
                prepare(method, prefix, "is-block"),
                material.isBlock()
                        ? "Block"
                        : "No Block"
        ).replace(
                prepare(method, prefix, "is-item"),
                material.isItem()
                        ? "Item"
                        : "No Item"
        );
        // todo

        return string;
    }

    public static String getPlayer(
            Player player,
            String method,
            String string
    ) {
        String prefix = "player";
        // todo

        string = getLivingEntity(
                player,
                method,
                string
        );
        return string;
    }

    public static String getLivingEntity(
            LivingEntity entity,
            String method,
            String string
    ) {
        String prefix = "living-entity";

        string = getEntity(
                entity,
                method,
                string
        );
        return string;
    }

    public static String getEntity(
            Entity entity,
            String method,
            String string
    ) {
        String prefix = "entity";
        // todo

        string = getLocation(
                entity.getLocation(),
                method,
                string,
                false
        );
        return string;
    }

}
