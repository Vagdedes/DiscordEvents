package ai.idealistic.bdw.utils;

public class IDs {

    public static final boolean
            enabled = validInteger("%%__RESOURCE__%%"),
            builtByBit = "%%__BUILTBYBIT__%%".length() != 18,
            polymart = "%%__POLYMART__%%".length() == 1,
            spigot = !builtByBit && !polymart && enabled;

    public static String platform() {
        return IDs.builtByBit ? "BuiltByBit" : IDs.polymart ? "Polymart" : IDs.spigot ? "SpigotMC" : "Free";
    }

    private static boolean validInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
