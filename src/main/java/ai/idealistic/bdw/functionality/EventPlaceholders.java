package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.utils.ArrayReader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class EventPlaceholders {

    private static final String isNull = "(null)";
    private static final Set<Integer> noMethods = new HashSet<>(1);

    static final String
            nativePlaceholder = ":native}";
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
                    ":location:x}",
                    ":location:y}",
                    ":location:z}",
                    ":location:block-x}",
                    ":location:block-y}",
                    ":location:block-z}",
                    ":location:yaw}",
                    ":location:pitch}"
            },
            blockPlaceholders = {
                    ":block:biome}",
            },
            itemPlaceholders = {
                    ":item:amount}",
                    ":item:durability}",
                    ":item:type}",
                    ":item:has-meta}",
                    ":item:has-enchants}",
                    ":item:enchantment-count}",
                    ":item:has-display-name}",
                    ":item:has-lore}",
                    ":item:has-custom-model-data}",
                    ":item:has-localized-name}",
            },
            materialPlaceholders = {
                    ":material:type}",
                    ":material:is-block}",
                    ":material:is-edible}",
                    ":material:is-solid}",
                    ":material:is-transparent}",
                    ":material:is-flammable}",
                    ":material:is-item}",
                    ":material:is-air}",
                    ":material:is-liquid}",
                    ":material:is-interactable}",
                    ":material:is-occluding}",
            },
            entityPlaceholders = {
                    ":entity:type}",
                    ":entity:id}",
                    ":entity:uuid}",
                    ":entity:fall-distance}",
                    ":entity:fire-ticks}",
                    ":entity:is-valid}",
                    ":entity:is-inside-vehicle}",
                    ":entity:is-dead}",
                    ":entity:is-frozen}",
                    ":entity:name}",
                    ":entity:custom-name}",
                    ":entity:custom-name-visible}",
                    ":entity:is-silent}",
                    ":entity:is-glowing}",
                    ":entity:is-persistent}",
                    ":entity:is-visual-fire}",
                    ":entity:facing}",
                    ":entity:height}"
            },
            livingEntityPlaceholders = {
                    ":living-entity:health}",
                    ":living-entity:max-health}",
                    ":living-entity:is-swimming}",
                    ":living-entity:is-gliding}",
                    ":living-entity:is-sleeping}",
                    ":living-entity:has-ai}"
            },
            playerPlaceholders = {
                    ":player:is-sneaking}",
                    ":player:is-sprinting}",
                    ":player:is-flying}",
                    ":player:is-op}",
                    ":player:game-mode}"
            };

    static String getPlaceholder(String method, String placeholder) {
        return "{" + method + placeholder;
    }

    static String[] getPlaceholders(String method, String[] placeholders) {
        String[] result = new String[placeholders.length];

        for (int i = 0; i < placeholders.length; i++) {
            result[i] = getPlaceholder(method, placeholders[i]);
        }
        return result;
    }

    private static String prepare(
            String method,
            String prefix,
            String type
    ) {
        return "{" + method + ":" + prefix + ":" + type + "}";
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
        if (world == null) {
            for (String placeholder : worldPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
                ArrayReader.toString(world.getGameRules())
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
        } else {
            string = string.replace(
                    prepare(method, prefix, "has-raids"),
                    isNull
            );
        }
        object = hasWorldMethod(world, "getPVP");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "pvp-allowed"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "pvp-allowed"),
                    isNull
            );
        }
        object = hasWorldMethod(world, "hasSkyLight");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    isNull
            );
        }
        object = hasWorldMethod(world, "hasSkyLight");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "has-sky-light"),
                    isNull
            );
        }
        object = hasWorldMethod(world, "isHardcore");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-hardcore"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-hardcore"),
                    isNull
            );
        }
        object = hasWorldMethod(world, "getGameTime");

        if (object instanceof Long) {
            string = string.replace(
                    prepare(method, prefix, "game-time"),
                    new Timestamp((long) object).toString().substring(0, 19)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "game-time"),
                    isNull
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
                prepare(method, prefix, "x"),
                String.valueOf(location.getX())
        ).replace(
                prepare(method, prefix, "y"),
                String.valueOf(location.getY())
        ).replace(
                prepare(method, prefix, "z"),
                String.valueOf(location.getZ())
        ).replace(
                prepare(method, prefix, "block-x"),
                String.valueOf(location.getBlockX())
        ).replace(
                prepare(method, prefix, "block-y"),
                String.valueOf(location.getBlockY())
        ).replace(
                prepare(method, prefix, "block-z"),
                String.valueOf(location.getBlockZ())
        ).replace(
                prepare(method, prefix, "yaw"),
                String.valueOf(location.getYaw())
        ).replace(
                prepare(method, prefix, "pitch"),
                String.valueOf(location.getPitch())
        );

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
        if (block == null) {
            for (String placeholder : blockPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
        String prefix = "block";

        string = string.replace(
                prepare(method, prefix, "biome"),
                block.getBiome().name().toLowerCase().replace("_", "-")
        );

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

    private static Object hasItemMetaMethod(ItemMeta meta, String methodName) {
        int hash = (meta.getClass().hashCode() * 31) + methodName.hashCode();

        if (noMethods.contains(hash)) {
            return null;
        }
        try {
            return meta.getClass().getMethod(methodName).invoke(meta);
        } catch (Exception e) {
            noMethods.add(hash);
            return null;
        }
    }

    public static String getItem(
            ItemStack itemStack,
            String method,
            String string
    ) {
        if (itemStack == null) {
            for (String placeholder : itemPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
        String prefix = "item";

        string = string.replace(
                prepare(method, prefix, "amount"),
                String.valueOf(itemStack.getAmount())
        ).replace(
                prepare(method, prefix, "durability"),
                String.valueOf(itemStack.getDurability())
        ).replace(
                prepare(method, prefix, "type"),
                itemStack.getType().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "has-meta"),
                String.valueOf(itemStack.hasItemMeta())
        ).replace(
                prepare(method, prefix, "has-enchants"),
                String.valueOf(itemStack.hasItemMeta()
                        && itemStack.getItemMeta().hasEnchants())
        ).replace(
                prepare(method, prefix, "enchantment-count"),
                String.valueOf(itemStack.getEnchantments().size())
        ).replace(
                prepare(method, prefix, "has-display-name"),
                String.valueOf(
                        itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasDisplayName()
                )
        ).replace(
                prepare(method, prefix, "has-lore"),
                String.valueOf(
                        itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasLore()
                )
        );

        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            Object object = hasItemMetaMethod(meta, "hasCustomModelData");

            if (object instanceof Integer) {
                string = string.replace(
                        prepare(method, prefix, "has-custom-model-data"),
                        String.valueOf(object)
                );
            } else {
                string = string.replace(
                        prepare(method, prefix, "has-custom-model-data"),
                        isNull
                );
            }
            object = hasItemMetaMethod(meta, "hasLocalizedName");

            if (object instanceof String) {
                string = string.replace(
                        prepare(method, prefix, "has-localized-name"),
                        object.toString()
                );
            } else {
                string = string.replace(
                        prepare(method, prefix, "has-localized-name"),
                        isNull
                );
            }
        }

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
        if (material == null) {
            for (String placeholder : materialPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-air"),
                    isNull
            );
        }
        object = hasMaterialMethod(material, "isLiquid");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-item"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-item"),
                    isNull
            );
        }
        object = hasMaterialMethod(material, "isInteractable");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-interactable"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-interactable"),
                    isNull
            );
        }
        object = hasMaterialMethod(material, "isOccluding");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-occluding"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-occluding"),
                    isNull
            );
        }
        return string;
    }

    public static String getPlayer(
            Player player,
            String method,
            String string
    ) {
        if (player == null) {
            for (String placeholder : playerPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
        String prefix = "player";

        string = string.replace(
                prepare(method, prefix, "is-sneaking"),
                String.valueOf(player.isSneaking())
        ).replace(
                prepare(method, prefix, "is-sprinting"),
                String.valueOf(player.isSprinting())
        ).replace(
                prepare(method, prefix, "is-flying"),
                String.valueOf(player.isFlying())
        ).replace(
                prepare(method, prefix, "is-op"),
                String.valueOf(player.isOp())
        ).replace(
                prepare(method, prefix, "game-mode"),
                player.getGameMode().name().toLowerCase().replace("_", "-")
        );

        string = getLivingEntity(
                player,
                method,
                string
        );
        return string;
    }

    private static Object hasLivingEntityMethod(LivingEntity entity, String methodName) {
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

    public static String getLivingEntity(
            LivingEntity entity,
            String method,
            String string
    ) {
        if (entity == null) {
            for (String placeholder : livingEntityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
        Object object = hasLivingEntityMethod(entity, "isSleeping");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-sleeping"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-sleeping"),
                    isNull
            );
        }
        object = hasLivingEntityMethod(entity, "hasAI");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "has-ai"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "has-ai"),
                    isNull
            );
        }

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
        if (entity == null) {
            for (String placeholder : entityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
                prepare(method, prefix, "is-valid"),
                String.valueOf(entity.isValid())
        ).replace(
                prepare(method, prefix, "is-inside-vehicle"),
                String.valueOf(entity.isInsideVehicle())
        ).replace(
                prepare(method, prefix, "is-dead"),
                String.valueOf(entity.isDead())
        ).replace(
                prepare(method, prefix, "is-frozen"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isFrozen()
                )
        );
        Object object = hasEntityMethod(entity, "getName");

        if (object instanceof String) {
            string = string.replace(
                    prepare(method, prefix, "name"),
                    object.toString()
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "name"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "getCustomName");

        if (object instanceof String) {
            string = string.replace(
                    prepare(method, prefix, "custom-name"),
                    object.toString()
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "custom-name"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "isCustomNameVisible");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "custom-name-visible"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "custom-name-visible"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "isSilent");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-silent"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-silent"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "isGlowing");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-glowing"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-glowing"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "isPersistent");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-persistent"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-persistent"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "isVisualFire");

        if (object instanceof Boolean) {
            string = string.replace(
                    prepare(method, prefix, "is-visual-fire"),
                    String.valueOf(object == Boolean.TRUE)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "is-visual-fire"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "getFacing");

        if (object instanceof BlockFace) {
            string = string.replace(
                    prepare(method, prefix, "facing"),
                    ((BlockFace) object).name().toLowerCase().replace("_", "-")
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "facing"),
                    isNull
            );
        }
        object = hasEntityMethod(entity, "getHeight");

        if (object instanceof Double) {
            string = string.replace(
                    prepare(method, prefix, "height"),
                    String.valueOf(object)
            );
        } else {
            string = string.replace(
                    prepare(method, prefix, "height"),
                    isNull
            );
        }

        string = getLocation(
                entity.getLocation(),
                method,
                string,
                false
        );
        return string;
    }

    static boolean isNative(Object object) {
        return object instanceof Boolean
                || object instanceof Number
                || object instanceof String;
    }

    public static String getNative(
            Object object,
            String methodName,
            String string) {
        if (object == null) {
            string = string.replace(
                    "{" + methodName + nativePlaceholder,
                    isNull
            );
        } else {
            string = string.replace(
                    "{" + methodName + nativePlaceholder,
                    object.toString()
            );
        }
        return string;
    }

}
