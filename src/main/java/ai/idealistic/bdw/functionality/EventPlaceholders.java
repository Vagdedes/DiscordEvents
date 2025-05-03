package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.listeners.ChunksEvent;
import ai.idealistic.bdw.utils.ArrayReader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventPlaceholders {

    private static final String isNull = "(null)";

    static final String
            customPlaceholder = ":custom}";
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
                    ":location:pitch}",
                    ":location:chunk-x}",
                    ":location:chunk-z}",
            },
            blockPlaceholders = {
                    ":block:biome}",
                    ":block:is-empty}",
                    ":block:is-passable}",
                    ":block:is-liquid}",
                    ":block:is-block-powered}",
                    ":block:is-block-indirectly-powered}",
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
                    ":entity:height}",
                    ":entity:width}",
                    ":entity:has-gravity}",
                    ":entity:has-passenger}",
                    ":entity:ticks-lived}",
                    ":entity:max-fire-ticks}",
                    ":entity:portal-cooldown}",
                    ":entity:vehicle}",
            },
            livingEntityPlaceholders = {
                    ":living-entity:health}",
                    ":living-entity:max-health}",
                    ":living-entity:is-swimming}",
                    ":living-entity:is-gliding}",
                    ":living-entity:is-sleeping}",
                    ":living-entity:has-ai}",
                    ":living-entity:eye-height}",
                    ":living-entity:is-leashed}",
                    ":living-entity:can-pickup-items}",
                    ":living-entity:active-potion-effects}",
                    ":living-entity:remove-when-far-away}",
                    ":living-entity:last-damage}",
                    ":living-entity:maximum-no-damage-ticks}",
                    ":living-entity:no-damage-ticks}",
                    ":living-entity:arrows-in-body}",
                    ":living-entity:category}",
                    ":living-entity:is-riptiding}",
                    ":living-entity:is-climbing}",
                    ":living-entity:is-collidable}",
                    ":living-entity:can-breathe-underwater}",
            },
            playerPlaceholders = {
                    ":player:is-sneaking}",
                    ":player:is-sprinting}",
                    ":player:is-flying}",
                    ":player:is-op}",
                    ":player:game-mode}",
                    ":player:is-invisible}",
                    ":player:display-name}",
                    ":player:player-list-name}",
                    ":player:level}",
                    ":player:exp}",
                    ":player:is-player-time-relative}",
                    ":player:player-time}",
                    ":player:walk-speed}",
                    ":player:fly-speed}",
                    ":player:total-experience}",
                    ":player:client-view-distance}",
            },
            killerPlaceholders,
            vehiclePlaceholders;

    static {
        int size = playerPlaceholders.length
                + livingEntityPlaceholders.length
                + entityPlaceholders.length;
        List<String>
                placeholders = new ArrayList<>(size),
                add = new ArrayList<>(size);

        placeholders.addAll(Arrays.asList(playerPlaceholders));
        placeholders.addAll(Arrays.asList(livingEntityPlaceholders));
        placeholders.addAll(Arrays.asList(entityPlaceholders));

        for (String placeholder : placeholders) {
            add.add(
                    placeholder
                            .replace(":player:", ":killer:")
                            .replace(":living-entity:", ":killer:")
                            .replace(":entity:", ":killer:")
            );
        }
        killerPlaceholders = add.toArray(new String[0]);
        add.clear();

        for (String placeholder : entityPlaceholders) {
            add.add(
                    placeholder
                            .replace(":player:", ":vehicle:")
                            .replace(":living-entity:", ":vehicle:")
                            .replace(":entity:", ":vehicle:")
            );
        }
        vehiclePlaceholders = add.toArray(new String[0]);
    }

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

    private static String getWorld(
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
        ).replace(
                prepare(method, prefix, "has-raids"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && world.hasRaids()
                )
        ).replace(
                prepare(method, prefix, "has-sky-light"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && world.hasSkyLight()
                )
        ).replace(
                prepare(method, prefix, "pvp-allowed"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && world.getPVP()
                )
        ).replace(
                prepare(method, prefix, "is-hardcore"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && world.isHardcore()
                )
        ).replace(
                prepare(method, prefix, "game-time"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                ? world.getGameTime()
                                : -1
                )
        );
        return string;
    }

    private static String getLocation(
            Location location,
            String method,
            String string,
            boolean block
    ) {
        if (location == null) {
            for (String placeholder : locationPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : worldPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : blockPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : materialPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
        ).replace(
                prepare(method, prefix, "chunk-x"),
                String.valueOf(location.getBlockX() >> 4)
        ).replace(
                prepare(method, prefix, "chunk-z"),
                String.valueOf(location.getBlockZ() >> 4)
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

    private static String getBlock(
            Block block,
            String method,
            String string
    ) {
        if (block == null
                || !ChunksEvent.isLoaded(
                block.getWorld(),
                block.getX() >> 4,
                block.getZ() >> 4
        )) {
            for (String placeholder : blockPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : materialPlaceholders) {
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
        ).replace(
                prepare(method, prefix, "is-empty"),
                String.valueOf(block.isEmpty())
        ).replace(
                prepare(method, prefix, "is-liquid"),
                String.valueOf(block.isLiquid())
        ).replace(
                prepare(method, prefix, "is-block-powered"),
                String.valueOf(block.isBlockPowered())
        ).replace(
                prepare(method, prefix, "is-block-indirectly-powered"),
                String.valueOf(block.isBlockIndirectlyPowered())
        ).replace(
                prepare(method, prefix, "is-passable"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && block.isPassable()
                )
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

    private static String getItem(
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
            for (String placeholder : materialPlaceholders) {
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
        ).replace(
                prepare(method, prefix, "has-custom-model-data"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasLore()
                                && itemStack.getItemMeta().hasCustomModelData()
                )
        ).replace(
                prepare(method, prefix, "has-localized-name"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasLore()
                                && itemStack.getItemMeta().hasLocalizedName()
                )
        );
        string = getMaterial(
                itemStack.getType(),
                method,
                string
        );
        return string;
    }

    private static String getMaterial(
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
        ).replace(
                prepare(method, prefix, "is-item"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && material.isItem()
                )
        ).replace(
                prepare(method, prefix, "is-air"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && material.isAir()
                )
        ).replace(
                prepare(method, prefix, "is-interactable"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && material.isInteractable()
                )
        ).replace(
                prepare(method, prefix, "is-occluding"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && material.isOccluding()
                )
        );
        return string;
    }

    private static String getPlayer(
            Player player,
            String method,
            String string,
            String prefix,
            boolean recursive
    ) {
        if (player == null) {
            for (String placeholder : playerPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : livingEntityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : entityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : killerPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : vehiclePlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
                prepare(method, prefix, "can-fly"),
                String.valueOf(player.getAllowFlight())
        ).replace(
                prepare(method, prefix, "is-op"),
                String.valueOf(player.isOp())
        ).replace(
                prepare(method, prefix, "game-mode"),
                player.getGameMode().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "display-name"),
                player.getDisplayName()
        ).replace(
                prepare(method, prefix, "player-list-name"),
                player.getPlayerListName()
        ).replace(
                prepare(method, prefix, "level"),
                String.valueOf(player.getLevel())
        ).replace(
                prepare(method, prefix, "exp"),
                String.valueOf(player.getExp())
        ).replace(
                prepare(method, prefix, "is-player-time-relative"),
                String.valueOf(player.isPlayerTimeRelative())
        ).replace(
                prepare(method, prefix, "player-time"),
                String.valueOf(player.getPlayerTime()) // todo
        ).replace(
                prepare(method, prefix, "walk-speed"),
                String.valueOf(player.getWalkSpeed())
        ).replace(
                prepare(method, prefix, "fly-speed"),
                String.valueOf(player.getFlySpeed())
        ).replace(
                prepare(method, prefix, "total-experience"),
                String.valueOf(player.getTotalExperience())
        ).replace(
                prepare(method, prefix, "is-invisible"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && player.isInvisible()
                )
        ).replace(
                prepare(method, prefix, "is-invisible"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_18)
                                ? player.getClientViewDistance()
                                : -1
                )
        );

        string = getLivingEntity(
                player,
                method,
                string,
                "living-entity",
                recursive
        );
        return string;
    }

    private static String getLivingEntity(
            LivingEntity entity,
            String method,
            String string,
            String prefix,
            boolean recursive
    ) {
        if (entity == null) {
            for (String placeholder : livingEntityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : entityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : killerPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : vehiclePlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
        ).replace(
                prepare(method, prefix, "eye-height"),
                String.valueOf(entity.getEyeHeight())
        ).replace(
                prepare(method, prefix, "is-leashed"),
                String.valueOf(entity.isLeashed())
        ).replace(
                prepare(method, prefix, "can-pickup-items"),
                String.valueOf(entity.getCanPickupItems())
        ).replace(
                prepare(method, prefix, "active-potion-effects"),
                String.valueOf(entity.getActivePotionEffects().size())
        ).replace(
                prepare(method, prefix, "remove-when-far-away"),
                String.valueOf(entity.getRemoveWhenFarAway())
        ).replace(
                prepare(method, prefix, "last-damage"),
                String.valueOf(entity.getLastDamage())
        ).replace(
                prepare(method, prefix, "maximum-no-damage-ticks"),
                String.valueOf(entity.getMaximumNoDamageTicks())
        ).replace(
                prepare(method, prefix, "no-damage-ticks"),
                String.valueOf(entity.getNoDamageTicks())
        ).replace(
                prepare(method, prefix, "arrows-in-body"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                ? entity.getArrowsInBody()
                                : -1
                )
        ).replace(
                prepare(method, prefix, "category"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? entity.getCategory().name().toLowerCase().replace("_", "-")
                        : isNull
        ).replace(
                prepare(method, prefix, "can-breathe-underwater"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.canBreatheUnderwater()
                )
        ).replace(
                prepare(method, prefix, "is-riptiding"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && entity.isRiptiding()
                )
        ).replace(
                prepare(method, prefix, "is-climbing"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isClimbing()
                )
        ).replace(
                prepare(method, prefix, "is-collidable"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && entity.isCollidable()
                )
        ).replace(
                prepare(method, prefix, "has-ai"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && entity.hasAI()
                )
        ).replace(
                prepare(method, prefix, "is-sleeping"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && entity.isSleeping()
                )
        );
        Player killer = entity.getKiller();

        if (recursive) {
            string = getPlayer(
                    killer,
                    method,
                    string,
                    "killer",
                    false
            );
        } else {
            string = getPlayer(
                    null,
                    method,
                    string,
                    "killer",
                    false
            );
        }
        string = getEntity(
                entity,
                method,
                string,
                "entity",
                recursive
        );
        return string;
    }

    private static String getEntity(
            Entity entity,
            String method,
            String string,
            String prefix,
            boolean recursive
    ) {
        if (entity == null) {
            for (String placeholder : entityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            for (String placeholder : vehiclePlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        isNull
                );
            }
            return string;
        }
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
        ).replace(
                prepare(method, prefix, "ticks-lived"),
                String.valueOf(
                        entity.getTicksLived()
                )
        ).replace(
                prepare(method, prefix, "has-passenger"),
                String.valueOf(
                        entity.getPassenger() != null
                )
        ).replace(
                prepare(method, prefix, "max-fire-ticks"),
                String.valueOf(
                        entity.getMaxFireTicks()
                )
        ).replace(
                prepare(method, prefix, "name"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                        ? entity.getName()
                        : isNull
        ).replace(
                prepare(method, prefix, "custom-name"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                        ? (entity.getCustomName() == null
                        ? isNull
                        : entity.getCustomName())
                        : isNull
        ).replace(
                prepare(method, prefix, "custom-name-visible"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && entity.isCustomNameVisible()
                )
        ).replace(
                prepare(method, prefix, "has-gravity"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_10)
                                && entity.hasGravity()
                )
        ).replace(
                prepare(method, prefix, "is-silent"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                && entity.isSilent()
                )
        ).replace(
                prepare(method, prefix, "is-glowing"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                && entity.isGlowing()
                )
        ).replace(
                prepare(method, prefix, "is-persistent"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && entity.isPersistent()
                )
        ).replace(
                prepare(method, prefix, "is-visual-fire"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isVisualFire()
                )
        ).replace(
                prepare(method, prefix, "facing"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? entity.getFacing().name().toLowerCase().replace("_", "-")
                        : isNull
        ).replace(
                prepare(method, prefix, "height"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                ? entity.getHeight()
                                : isNull
                )
        ).replace(
                prepare(method, prefix, "width"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                ? entity.getWidth()
                                : isNull
                )
        ).replace(
                prepare(method, prefix, "pose"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? entity.getPose().name().toLowerCase().replace("_", "-")
                        : isNull
        ).replace(
                prepare(method, prefix, "spawn-category"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_18)
                        ? entity.getSpawnCategory().name().toLowerCase().replace("_", "-")
                        : isNull
        ).replace(
                prepare(method, prefix, "freeze-ticks"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                ? entity.getFreezeTicks()
                                : -1
                )
        ).replace(
                prepare(method, prefix, "max-freeze-ticks"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                ? entity.getMaxFreezeTicks()
                                : -1
                )
        ).replace(
                prepare(method, prefix, "portal-cooldown"),
                String.valueOf(
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                ? entity.getPortalCooldown()
                                : -1
                )
        );
        Entity vehicle = entity.getVehicle();

        if (recursive
                && vehicle != null) {
            if (vehicle instanceof Player) {
                string = getPlayer(
                        (Player) vehicle,
                        method,
                        string,
                        "vehicle",
                        false
                );
            } else if (vehicle instanceof LivingEntity) {
                string = getLivingEntity(
                        (LivingEntity) vehicle,
                        method,
                        string,
                        "vehicle",
                        false
                );
            } else {
                string = getEntity(
                        vehicle,
                        method,
                        string,
                        "vehicle",
                        false
                );
            }
        } else {
            string = getPlayer(
                    null,
                    method,
                    string,
                    "vehicle",
                    false
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

    private static String getCustom(
            Object object,
            String methodName,
            String string) {
        if (object == null) {
            string = string.replace(
                    "{" + methodName + customPlaceholder,
                    isNull
            );
        } else {
            string = string.replace(
                    "{" + methodName + customPlaceholder,
                    object.toString()
            );
        }
        return string;
    }

    public static String get(
            Object object,
            String methodName,
            String string
    ) {
        if (object instanceof World) {
            return getWorld(
                    (World) object,
                    methodName,
                    string
            );
        } else if (object instanceof Location) {
            return getLocation(
                    (Location) object,
                    methodName,
                    string,
                    true
            );
        } else if (object instanceof Block) {
            return getBlock(
                    (Block) object,
                    methodName,
                    string
            );
        } else if (object instanceof ItemStack) {
            return getItem(
                    (ItemStack) object,
                    methodName,
                    string
            );
        } else if (object instanceof Material) {
            return getMaterial(
                    (Material) object,
                    methodName,
                    string
            );
        } else if (object instanceof Entity) {
            if (object instanceof Player) {
                return getPlayer(
                        (Player) object,
                        methodName,
                        string,
                        "player",
                        true
                );
            } else if (object instanceof LivingEntity) {
                return getLivingEntity(
                        (LivingEntity) object,
                        methodName,
                        string,
                        "living-entity",
                        true
                );
            } else {
                return getEntity(
                        (Entity) object,
                        methodName,
                        string,
                        "entity",
                        true
                );
            }
        } else {
            return getCustom(object, methodName, string);
        }
    }

}
