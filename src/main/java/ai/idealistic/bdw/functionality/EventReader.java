package ai.idealistic.bdw.functionality;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.xml.stream.Location;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventReader {

    public static String[] getPlaceholders(String methodName, Object object) {
        Collection<String> list = new ArrayList<>();

        if (object instanceof World) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.worldPlaceholders
            )));
        } else if (object instanceof Location) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.locationPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.worldPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.blockPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        } else if (object instanceof Block) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.blockPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.worldPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.locationPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        } else if (object instanceof ItemStack) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.itemPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        } else if (object instanceof Material) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        } else if (object instanceof Entity) {
            if (object instanceof Player) {
                list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                        methodName,
                        EventPlaceholders.playerPlaceholders
                )));
                list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                        methodName,
                        EventPlaceholders.livingEntityPlaceholders
                )));
            } else if (object instanceof LivingEntity) {
                list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                        methodName,
                        EventPlaceholders.livingEntityPlaceholders
                )));
            }
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.entityPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.locationPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.worldPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.blockPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        }
        return list.toArray(new String[0]);
    }

    public static Map<String, Method> getValidMethods(Event event) {
        Map<String, Method> methods = new ConcurrentHashMap<>(1);

        for (Method method : event.getClass().getDeclaredMethods()) {
            try {
                method.setAccessible(true);

                if (method.getReturnType() != Void.TYPE
                        && method.getParameterCount() == 0) {
                    methods.put(method.getName().toLowerCase(), method);
                }
            } catch (Exception ignored) {
            }
        }
        return methods;
    }

}
