package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.implementation.DiscordWebhook;
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

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventPlaceholders {

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
                    ":player:name}",
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
                    ":player:big-head-image-url}",
                    ":player:medium-head-image-url}",
                    ":player:small-head-image-url}",
                    ":player:big-head-image-url-nohelm}",
                    ":player:medium-head-image-url-nohelm}",
                    ":player:small-head-image-url-nohelm}",
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

    private static String bool(DiscordWebhook webhook, boolean b) {
        return b
                ? webhook.getTrueText()
                : webhook.getFalseText();
    }

    private static String prepare(
            String method,
            String prefix,
            String type
    ) {
        return "{" + method + ":" + prefix + ":" + type + "}";
    }

    private static String getWorld(
            DiscordWebhook webhook,
            World world,
            String method,
            String string
    ) {
        if (world == null) {
            for (String placeholder : worldPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
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
                webhook.escapeMarkdownAfterMethodCall(world.getSpawnLocation().getX())
        ).replace(
                prepare(method, prefix, "spawn-y"),
                webhook.escapeMarkdownAfterMethodCall(world.getSpawnLocation().getY())
        ).replace(
                prepare(method, prefix, "spawn-z"),
                webhook.escapeMarkdownAfterMethodCall(world.getSpawnLocation().getZ())
        ).replace(
                prepare(method, prefix, "difficulty"),
                world.getDifficulty().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "environment"),
                world.getEnvironment().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "seed"),
                webhook.escapeMarkdownAfterMethodCall(world.getSeed())
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
                bool(webhook, world.isThundering())
        ).replace(
                prepare(method, prefix, "has-storm"),
                bool(webhook, world.hasStorm())
        ).replace(
                prepare(method, prefix, "allow-animals"),
                bool(webhook, world.getAllowAnimals())
        ).replace(
                prepare(method, prefix, "allow-monsters"),
                bool(webhook, world.getAllowMonsters())
        ).replace(
                prepare(method, prefix, "game-rules"),
                ArrayReader.toString(world.getGameRules())
        ).replace(
                prepare(method, prefix, "border-size"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                        ? webhook.escapeMarkdownAfterMethodCall(world.getWorldBorder().getSize())
                        : webhook.getNotImplementedText()
        ).replace(
                prepare(method, prefix, "has-raids"),
                bool(webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && world.hasRaids()
                )
        ).replace(
                prepare(method, prefix, "has-sky-light"),
                bool(webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && world.hasSkyLight()
                )
        ).replace(
                prepare(method, prefix, "pvp-allowed"),
                bool(webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && world.getPVP()
                )
        ).replace(
                prepare(method, prefix, "is-hardcore"),
                bool(webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && world.isHardcore()
                )
        ).replace(
                prepare(method, prefix, "game-time"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? webhook.escapeMarkdownAfterMethodCall(world.getGameTime())
                        : webhook.getNotImplementedText()
        );
        return string;
    }

    private static String getLocation(
            DiscordWebhook webhook,
            Location location,
            String method,
            String string,
            boolean block
    ) {
        if (location == null) {
            for (String placeholder : locationPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : worldPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : blockPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : materialPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            return string;
        }
        String prefix = "location";

        string = string.replace(
                prepare(method, prefix, "x"),
                webhook.escapeMarkdownAfterMethodCall(location.getX())
        ).replace(
                prepare(method, prefix, "y"),
                webhook.escapeMarkdownAfterMethodCall(location.getY())
        ).replace(
                prepare(method, prefix, "z"),
                webhook.escapeMarkdownAfterMethodCall(location.getZ())
        ).replace(
                prepare(method, prefix, "block-x"),
                webhook.escapeMarkdownAfterMethodCall(location.getBlockX())
        ).replace(
                prepare(method, prefix, "block-y"),
                webhook.escapeMarkdownAfterMethodCall(location.getBlockY())
        ).replace(
                prepare(method, prefix, "block-z"),
                webhook.escapeMarkdownAfterMethodCall(location.getBlockZ())
        ).replace(
                prepare(method, prefix, "yaw"),
                webhook.escapeMarkdownAfterMethodCall(location.getYaw())
        ).replace(
                prepare(method, prefix, "pitch"),
                webhook.escapeMarkdownAfterMethodCall(location.getPitch())
        ).replace(
                prepare(method, prefix, "chunk-x"),
                webhook.escapeMarkdownAfterMethodCall(location.getBlockX() >> 4)
        ).replace(
                prepare(method, prefix, "chunk-z"),
                webhook.escapeMarkdownAfterMethodCall(location.getBlockZ() >> 4)
        );

        string = getWorld(
                webhook,
                location.getWorld(),
                method,
                string
        );
        if (block) {
            string = getBlock(
                    webhook,
                    location.getBlock(),
                    method,
                    string
            );
        }
        return string;
    }

    private static String getBlock(
            DiscordWebhook webhook,
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
                        webhook.getNullText()
                );
            }
            for (String placeholder : materialPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
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
                bool(webhook, block.isEmpty())
        ).replace(
                prepare(method, prefix, "is-liquid"),
                bool(webhook, block.isLiquid())
        ).replace(
                prepare(method, prefix, "is-block-powered"),
                bool(webhook, block.isBlockPowered())
        ).replace(
                prepare(method, prefix, "is-block-indirectly-powered"),
                bool(webhook, block.isBlockIndirectlyPowered())
        ).replace(
                prepare(method, prefix, "is-passable"),
                bool(webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && block.isPassable()
                )
        );
        string = getLocation(
                webhook,
                block.getLocation(),
                method,
                string,
                false
        );
        string = getMaterial(
                webhook,
                block.getType(),
                method,
                string
        );
        return string;
    }

    private static String getItem(
            DiscordWebhook webhook,
            ItemStack itemStack,
            String method,
            String string
    ) {
        if (itemStack == null) {
            for (String placeholder : itemPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : materialPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            return string;
        }
        String prefix = "item";

        string = string.replace(
                prepare(method, prefix, "amount"),
                webhook.escapeMarkdownAfterMethodCall(itemStack.getAmount())
        ).replace(
                prepare(method, prefix, "durability"),
                webhook.escapeMarkdownAfterMethodCall(itemStack.getDurability())
        ).replace(
                prepare(method, prefix, "type"),
                itemStack.getType().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "has-meta"),
                bool(webhook, itemStack.hasItemMeta())
        ).replace(
                prepare(method, prefix, "has-enchants"),
                bool(webhook,
                        itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasEnchants()
                )
        ).replace(
                prepare(method, prefix, "enchantment-count"),
                webhook.escapeMarkdownAfterMethodCall(itemStack.getEnchantments().size())
        ).replace(
                prepare(method, prefix, "has-display-name"),
                bool(
                        webhook,
                        itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasDisplayName()
                )
        ).replace(
                prepare(method, prefix, "has-lore"),
                bool(
                        webhook,
                        itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasLore()
                )
        ).replace(
                prepare(method, prefix, "has-custom-model-data"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasLore()
                                && itemStack.getItemMeta().hasCustomModelData()
                )
        ).replace(
                prepare(method, prefix, "has-localized-name"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && itemStack.hasItemMeta()
                                && itemStack.getItemMeta().hasLore()
                                && itemStack.getItemMeta().hasLocalizedName()
                )
        );
        string = getMaterial(
                webhook,
                itemStack.getType(),
                method,
                string
        );
        return string;
    }

    private static String getMaterial(
            DiscordWebhook webhook,
            Material material,
            String method,
            String string
    ) {
        if (material == null) {
            for (String placeholder : materialPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
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
                bool(webhook, material.isBlock())
        ).replace(
                prepare(method, prefix, "is-edible"),
                bool(webhook, material.isEdible())
        ).replace(
                prepare(method, prefix, "is-solid"),
                bool(webhook, material.isSolid())
        ).replace(
                prepare(method, prefix, "is-transparent"),
                bool(webhook, material.isTransparent())
        ).replace(
                prepare(method, prefix, "is-flammable"),
                bool(webhook, material.isFlammable())
        ).replace(
                prepare(method, prefix, "is-item"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && material.isItem()
                )
        ).replace(
                prepare(method, prefix, "is-air"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && material.isAir()
                )
        ).replace(
                prepare(method, prefix, "is-interactable"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && material.isInteractable()
                )
        ).replace(
                prepare(method, prefix, "is-occluding"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && material.isOccluding()
                )
        );
        return string;
    }

    private static String getPlayer(
            DiscordWebhook webhook,
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
                        webhook.getNullText()
                );
            }
            for (String placeholder : livingEntityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : entityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : killerPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : vehiclePlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            return string;
        }
        string = string.replace(
                prepare(method, prefix, "is-sneaking"),
                bool(webhook, player.isSneaking())
        ).replace(
                prepare(method, prefix, "is-sprinting"),
                bool(webhook, player.isSprinting())
        ).replace(
                prepare(method, prefix, "is-flying"),
                bool(webhook, player.isFlying())
        ).replace(
                prepare(method, prefix, "can-fly"),
                bool(webhook, player.getAllowFlight())
        ).replace(
                prepare(method, prefix, "is-op"),
                bool(webhook, player.isOp())
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
                prepare(method, prefix, "name"),
                player.getName()
        ).replace(
                prepare(method, prefix, "level"),
                webhook.escapeMarkdownAfterMethodCall(player.getLevel())
        ).replace(
                prepare(method, prefix, "exp"),
                webhook.escapeMarkdownAfterMethodCall(player.getExp())
        ).replace(
                prepare(method, prefix, "is-player-time-relative"),
                bool(webhook, player.isPlayerTimeRelative())
        ).replace(
                prepare(method, prefix, "player-time"),
                webhook.escapeMarkdownAfterMethodCall(player.getPlayerTime()) // todo
        ).replace(
                prepare(method, prefix, "walk-speed"),
                webhook.escapeMarkdownAfterMethodCall(player.getWalkSpeed())
        ).replace(
                prepare(method, prefix, "fly-speed"),
                webhook.escapeMarkdownAfterMethodCall(player.getFlySpeed())
        ).replace(
                prepare(method, prefix, "total-experience"),
                webhook.escapeMarkdownAfterMethodCall(player.getTotalExperience())
        ).replace(
                prepare(method, prefix, "is-invisible"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && player.isInvisible()
                )
        ).replace(
                prepare(method, prefix, "is-invisible"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_18)
                        ? webhook.escapeMarkdownAfterMethodCall(player.getClientViewDistance())
                        : webhook.getNotImplementedText()
        ).replace(
                prepare(method, prefix, "big-head-image-url"),
                "https://mc-heads.net/avatar/" + player.getName() + "/200"
        ).replace(
                prepare(method, prefix, "medium-head-image-url"),
                "https://mc-heads.net/avatar/" + player.getName() + "/100"
        ).replace(
                prepare(method, prefix, "small-head-image-url"),
                "https://mc-heads.net/avatar/" + player.getName() + "/50"
        ).replace(
                prepare(method, prefix, "big-head-image-url-nohelm"),
                "https://mc-heads.net/avatar/" + player.getName() + "/200/nohelm"
        ).replace(
                prepare(method, prefix, "medium-head-image-url-nohelm"),
                "https://mc-heads.net/avatar/" + player.getName() + "/100/nohelm"
        ).replace(
                prepare(method, prefix, "small-head-image-url-nohelm"),
                "https://mc-heads.net/avatar/" + player.getName() + "/50/nohelm"
        );

        string = getLivingEntity(
                webhook,
                player,
                method,
                string,
                "living-entity",
                recursive
        );
        return string;
    }

    private static String getLivingEntity(
            DiscordWebhook webhook,
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
                        webhook.getNullText()
                );
            }
            for (String placeholder : entityPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : killerPlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            for (String placeholder : vehiclePlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            return string;
        }
        string = string.replace(
                prepare(method, prefix, "health"),
                webhook.escapeMarkdownAfterMethodCall(entity.getHealth())
        ).replace(
                prepare(method, prefix, "max-health"),
                webhook.escapeMarkdownAfterMethodCall(entity.getMaxHealth())
        ).replace(
                prepare(method, prefix, "is-swimming"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && entity.isSwimming()
                )
        ).replace(
                prepare(method, prefix, "is-gliding"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                && entity.isGliding()
                )
        ).replace(
                prepare(method, prefix, "eye-height"),
                webhook.escapeMarkdownAfterMethodCall(entity.getEyeHeight())
        ).replace(
                prepare(method, prefix, "is-leashed"),
                bool(webhook, entity.isLeashed())
        ).replace(
                prepare(method, prefix, "can-pickup-items"),
                bool(webhook, entity.getCanPickupItems())
        ).replace(
                prepare(method, prefix, "active-potion-effects"),
                webhook.escapeMarkdownAfterMethodCall(entity.getActivePotionEffects().size())
        ).replace(
                prepare(method, prefix, "remove-when-far-away"),
                bool(webhook, entity.getRemoveWhenFarAway())
        ).replace(
                prepare(method, prefix, "last-damage"),
                webhook.escapeMarkdownAfterMethodCall(entity.getLastDamage())
        ).replace(
                prepare(method, prefix, "maximum-no-damage-ticks"),
                webhook.escapeMarkdownAfterMethodCall(entity.getMaximumNoDamageTicks())
        ).replace(
                prepare(method, prefix, "no-damage-ticks"),
                webhook.escapeMarkdownAfterMethodCall(entity.getNoDamageTicks())
        ).replace(
                prepare(method, prefix, "arrows-in-body"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getArrowsInBody())
                        : webhook.getNotImplementedText()
        ).replace(
                prepare(method, prefix, "can-breathe-underwater"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.canBreatheUnderwater()
                )
        ).replace(
                prepare(method, prefix, "is-riptiding"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && entity.isRiptiding()
                )
        ).replace(
                prepare(method, prefix, "is-climbing"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isClimbing()
                )
        ).replace(
                prepare(method, prefix, "is-collidable"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                                && entity.isCollidable()
                )
        ).replace(
                prepare(method, prefix, "has-ai"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)
                                && entity.hasAI()
                )
        ).replace(
                prepare(method, prefix, "is-sleeping"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && entity.isSleeping()
                )
        );
        Player killer = entity.getKiller();

        if (recursive) {
            string = getPlayer(
                    webhook,
                    killer,
                    method,
                    string,
                    "killer",
                    false
            );
        } else {
            string = getPlayer(
                    webhook,
                    null,
                    method,
                    string,
                    "killer",
                    false
            );
        }
        string = getEntity(
                webhook,
                entity,
                method,
                string,
                "entity",
                recursive
        );
        return string;
    }

    private static String getEntity(
            DiscordWebhook webhook,
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
                        webhook.getNullText()
                );
            }
            for (String placeholder : vehiclePlaceholders) {
                string = string.replace(
                        "{" + method + placeholder,
                        webhook.getNullText()
                );
            }
            return string;
        }
        string = string.replace(
                prepare(method, prefix, "type"),
                entity.getType().name().toLowerCase().replace("_", "-")
        ).replace(
                prepare(method, prefix, "id"),
                webhook.escapeMarkdownAfterMethodCall(entity.getEntityId())
        ).replace(
                prepare(method, prefix, "uuid"),
                entity.getUniqueId().toString()
        ).replace(
                prepare(method, prefix, "fall-distance"),
                webhook.escapeMarkdownAfterMethodCall(entity.getFallDistance())
        ).replace(
                prepare(method, prefix, "fire-ticks"),
                webhook.escapeMarkdownAfterMethodCall(entity.getFireTicks())
        ).replace(
                prepare(method, prefix, "is-valid"),
                bool(webhook, entity.isValid())
        ).replace(
                prepare(method, prefix, "is-inside-vehicle"),
                bool(webhook, entity.isInsideVehicle())
        ).replace(
                prepare(method, prefix, "is-dead"),
                bool(webhook, entity.isDead())
        ).replace(
                prepare(method, prefix, "is-frozen"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isFrozen()
                )
        ).replace(
                prepare(method, prefix, "ticks-lived"),
                webhook.escapeMarkdownAfterMethodCall(entity.getTicksLived())
        ).replace(
                prepare(method, prefix, "has-passenger"),
                bool(webhook, entity.getPassenger() != null)
        ).replace(
                prepare(method, prefix, "max-fire-ticks"),
                webhook.escapeMarkdownAfterMethodCall(entity.getMaxFireTicks())
        ).replace(
                prepare(method, prefix, "name"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getName())
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "custom-name"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                        && entity.getCustomName() != null
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getCustomName())
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "custom-name-visible"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && entity.isCustomNameVisible()
                )
        ).replace(
                prepare(method, prefix, "has-gravity"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_10)
                                && entity.hasGravity()
                )
        ).replace(
                prepare(method, prefix, "is-silent"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                && entity.isSilent()
                )
        ).replace(
                prepare(method, prefix, "is-glowing"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                                && entity.isGlowing()
                )
        ).replace(
                prepare(method, prefix, "is-persistent"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)
                                && entity.isPersistent()
                )
        ).replace(
                prepare(method, prefix, "is-visual-fire"),
                bool(
                        webhook,
                        MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                                && entity.isVisualFire()
                )
        ).replace(
                prepare(method, prefix, "facing"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? entity.getFacing().name().toLowerCase().replace("_", "-")
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "height"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getHeight())
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "width"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getWidth())
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "pose"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? entity.getPose().name().toLowerCase().replace("_", "-")
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "spawn-category"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_18)
                        ? entity.getSpawnCategory().name().toLowerCase().replace("_", "-")
                        : webhook.getNullText()
        ).replace(
                prepare(method, prefix, "freeze-ticks"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getFreezeTicks())
                        : webhook.getNotImplementedText()
        ).replace(
                prepare(method, prefix, "max-freeze-ticks"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getMaxFreezeTicks())
                        : webhook.getNotImplementedText()
        ).replace(
                prepare(method, prefix, "portal-cooldown"),
                MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)
                        ? webhook.escapeMarkdownAfterMethodCall(entity.getPortalCooldown())
                        : webhook.getNotImplementedText()
        );
        Entity vehicle = entity.getVehicle();

        if (recursive
                && vehicle != null) {
            if (vehicle instanceof Player) {
                string = getPlayer(
                        webhook,
                        (Player) vehicle,
                        method,
                        string,
                        "vehicle",
                        false
                );
            } else if (vehicle instanceof LivingEntity) {
                string = getLivingEntity(
                        webhook,
                        (LivingEntity) vehicle,
                        method,
                        string,
                        "vehicle",
                        false
                );
            } else {
                string = getEntity(
                        webhook,
                        vehicle,
                        method,
                        string,
                        "vehicle",
                        false
                );
            }
        } else {
            string = getPlayer(
                    webhook,
                    null,
                    method,
                    string,
                    "vehicle",
                    false
            );
        }

        string = getLocation(
                webhook,
                entity.getLocation(),
                method,
                string,
                false
        );
        return string;
    }

    private static String getCustom(
            DiscordWebhook webhook,
            Object object,
            String methodName,
            Method method,
            String string
    ) {
        if (object == null) {
            string = string.replace(
                    "{" + methodName + customPlaceholder,
                    webhook.getNullText()
            );
        } else {
            string = string.replace(
                    "{" + methodName + customPlaceholder,
                    webhook.escapeMarkdownAfterMethodCall(
                            method.getReturnType().isEnum()
                                    ? object.toString().toLowerCase().replace("_", "-")
                                    : object.toString()
                    )
            );
        }
        return string;
    }

    public static String get(
            DiscordWebhook webhook,
            Object object,
            String methodName,
            Method method,
            String string
    ) {
        if (object instanceof World) {
            return getWorld(
                    webhook,
                    (World) object,
                    methodName,
                    string
            );
        } else if (object instanceof Location) {
            return getLocation(
                    webhook,
                    (Location) object,
                    methodName,
                    string,
                    true
            );
        } else if (object instanceof Block) {
            return getBlock(
                    webhook,
                    (Block) object,
                    methodName,
                    string
            );
        } else if (object instanceof ItemStack) {
            return getItem(
                    webhook,
                    (ItemStack) object,
                    methodName,
                    string
            );
        } else if (object instanceof Material) {
            return getMaterial(
                    webhook,
                    (Material) object,
                    methodName,
                    string
            );
        } else if (object instanceof Entity) {
            if (object instanceof Player) {
                return getPlayer(
                        webhook,
                        (Player) object,
                        methodName,
                        string,
                        "player",
                        true
                );
            } else if (object instanceof LivingEntity) {
                return getLivingEntity(
                        webhook,
                        (LivingEntity) object,
                        methodName,
                        string,
                        "living-entity",
                        true
                );
            } else {
                return getEntity(
                        webhook,
                        (Entity) object,
                        methodName,
                        string,
                        "entity",
                        true
                );
            }
        } else {
            return getCustom(
                    webhook,
                    object,
                    methodName,
                    method,
                    string
            );
        }
    }

}
