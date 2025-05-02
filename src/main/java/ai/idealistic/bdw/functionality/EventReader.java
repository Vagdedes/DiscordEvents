package ai.idealistic.bdw.functionality;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.xml.stream.Location;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventReader {

    public static String[] getPlaceholders(String methodName, Class<?> returnType) {
        Collection<String> list = new ArrayList<>();

        if (returnType.equals(World.class)) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.worldPlaceholders
            )));
        } else if (returnType.equals(Location.class)) {
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
        } else if (returnType.equals(Block.class)) {
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
        } else if (returnType.equals(ItemStack.class)) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.itemPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        } else if (returnType.equals(Material.class)) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.materialPlaceholders
            )));
        } else if (returnType.equals(Player.class)) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.playerPlaceholders
            )));
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.livingEntityPlaceholders
            )));
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
        } else if (returnType.equals(LivingEntity.class)) {
            list.addAll(Arrays.asList(EventPlaceholders.getPlaceholders(
                    methodName,
                    EventPlaceholders.livingEntityPlaceholders
            )));
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
        } else if (returnType.equals(Entity.class)) {
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

    public static Map<String, Method> getValidMethods(Class<?> event) {
        Map<String, Method> methods = new ConcurrentHashMap<>(1);

        for (Method method : event.getDeclaredMethods()) {
            method.setAccessible(true);

            if (method.getReturnType() != Void.TYPE
                    && method.getParameterCount() == 0) {
                methods.put(method.getName().toLowerCase(), method);
            }
        }
        return methods;
    }

}
