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
import java.util.HashSet;
import java.util.Set;

public class EventPlaceholders {

    private static final Set<Integer> noMethods = new HashSet<>(1);

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
            ":world:has-sky-light}",
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
        int hash = (world.getClass().hashCode() * 31) + methodName.hashCode();

        if (noMethods.contains(hash)) {
            return null;
        }
        try {
            return world.getClass().getMethod(methodName).invoke(world);
        } catch (Exception e) {
            noMethods.add(hash);
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
                world.getDifficulty().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "environment"),
                world.getEnvironment().name().toLowerCase().replace("_", "-")
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
                prepare(method, prefix, "weather"),
                world.isThundering()
                        ? "Thundering Weather"
                        : world.hasStorm()
                        ? "Storm Weather"
                        : "Clear Weather"
        ).replace(
                prepare(method, prefix, "is-thundering"),
                String.valueOf(world.isThundering())
        ).replace(
                prepare(method, prefix, "has-storm"),
                String.valueOf(world.hasStorm())
        ).replace(
                prepare(method, prefix, "allow-animals"),
                String.valueOf(world.getAllowAnimals())
        ).replace(
                prepare(method, prefix, "allow-monsters"),
                String.valueOf(world.getAllowMonsters())
        ).replace(
                prepare(method, prefix, "game-rules"),
                Arrays.toString(world.getGameRules())
        ).replace(
                prepare(method, prefix, "border-size"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                ? world.getWorldBorder().getSize()
                                : -1
                )
        );

        Object object = hasWorldMethod(world, "hasRaids");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-raids"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasWorldMethod(world, "getPVP");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "pvp-allowed"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasWorldMethod(world, "hasSkyLight");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasWorldMethod(world, "hasSkyLight");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasWorldMethod(world, "isHardcore");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-hardcore"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasWorldMethod(world, "getGameTime");

        if (object instanceof Long) {
            string = string.replace(
                    prepare(method, prefix, "game-time"),
                    new Timestamp((long) object).toString().substring(0, 19)
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
                block.getBiome().name().toLowerCase().replace("_", "-")
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

    private static Object hasMaterialMethod(Material material, String methodName) {
        int hash = (material.getClass().hashCode() * 31) + methodName.hashCode();

        if (noMethods.contains(hash)) {
            return null;
        }
        try {
            return material.getClass().getMethod(methodName).invoke(material);
        } catch (Exception e) {
            noMethods.add(hash);
            return null;
        }
    }

    public static String getMaterial(
            Material material,
            String method,
            String string
    ) {
        String prefix = "material";

        string = string.replace(
                prepare(method, prefix, "type"),
                material.name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "is-block"),
                String.valueOf(material.isBlock())
        ).replace(
                prepare(method, prefix, "is-edible"),
                String.valueOf(material.isEdible())
        ).replace(
                prepare(method, prefix, "is-solid"),
                String.valueOf(material.isSolid())
        ).replace(
                prepare(method, prefix, "is-transparent"),
                String.valueOf(material.isTransparent())
        ).replace(
                prepare(method, prefix, "is-flammable"),
                String.valueOf(material.isFlammable())
        );
        Object object = hasMaterialMethod(material, "isItem");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-item"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasMaterialMethod(material, "isAir");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-air"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        object = hasMaterialMethod(material, "isLiquid");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-item"),
                    String.valueOf(object == Boolean.TRUE)
            );
        }
        return string;
    }

    public static String getPlayer(
            Player player,
            String method,
            String string
    ) {
        String prefix = "player";

        string = string.replace(
                prepare(method, prefix, "is-sneaking"),
                String.valueOf(player.isSneaking())
        ).replace(
                prepare(method, prefix, "is-sprinting"),
                String.valueOf(player.isSprinting())
        );

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

        string = string.replace(
                prepare(method, prefix, "health"),
                String.valueOf(entity.getHealth())
        ).replace(
                prepare(method, prefix, "max-health"),
                String.valueOf(entity.getMaxHealth())
        ).replace(
                prepare(method, prefix, "is-swimming"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && entity.isSwimming()
                )
        ).replace(
                prepare(method, prefix, "is-gliding"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                && entity.isGliding()
                )
        );

        string = getEntity(
                entity,
                method,
                string
        );
        return string;
    }

    private static Object hasEntityMethod(Entity entity, String methodName) {
        int hash = (entity.getClass().hashCode() * 31) + methodName.hashCode();

        if (noMethods.contains(hash)) {
            return null;
        }
        try {
            return entity.getClass().getMethod(methodName).invoke(entity);
        } catch (Exception e) {
            noMethods.add(hash);
            return null;
        }
    }

    public static String getEntity(
            Entity entity,
            String method,
            String string
    ) {
        String prefix = "entity";

        string = string.replace(
                prepare(method, prefix, "type"),
                entity.getType().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "id"),
                String.valueOf(entity.getEntityId())
        ).replace(
                prepare(method, prefix, "uuid"),
                entity.getUniqueId().toString()
        ).replace(
                prepare(method, prefix, "fall-distance"),
                String.valueOf(entity.getFallDistance())
        ).replace(
                prepare(method, prefix, "fire-ticks"),
                String.valueOf(entity.getFireTicks())
        ).replace(
                prepare(method, prefix, "name"),
                entity.getName()
        ).replace(
                prepare(method, prefix, "custom-name"),
                entity.getCustomName() == null
                        ? "(null)"
                        : entity.getCustomName()
        ).replace(
                prepare(method, prefix, "facing"),
                entity.getFacing().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "is-silent"),
                String.valueOf(entity.isSilent())
        ).replace(
                prepare(method, prefix, "is-glowing"),
                String.valueOf(entity.isGlowing())
        ).replace(
                prepare(method, prefix, "is-persistent"),
                String.valueOf(entity.isPersistent())
        ).replace(
                prepare(method, prefix, "is-valid"),
                String.valueOf(entity.isValid())
        ).replace(
                prepare(method, prefix, "is-visual-fire"),
                String.valueOf(entity.isVisualFire())
        ).replace(
                prepare(method, prefix, "is-inside-vehicle"),
                String.valueOf(entity.isInsideVehicle())
        ).replace(
                prepare(method, prefix, "is-dead"),
                String.valueOf(entity.isDead())
        ).replace(
                prepare(method, prefix, "height"),
                String.valueOf(entity.getHeight())
        ).replace(
                prepare(method, prefix, "is-frozen"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isFrozen()
                )
        );

        string = getLocation(
                entity.getLocation(),
                method,
                string,
                false
        );
        return string;
    }

}
