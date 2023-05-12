package me.Incbom.fishingsystem.utils;

public class Config {
    private static Plugin plugin;

    public static void setPlugin(Plugin plugin) {
        Config.plugin = plugin;
    }

    public static int getInt(String path) {
        return plugin.getConfig().getInt(path);
    }

    public static String getString(String path) {
        return plugin.getConfig().getString(path);
    }

    public static boolean getBoolean(String path) {
        return plugin.getConfig().getBoolean(path);
    }

    public static void set(String path, Object value) {
        plugin.getConfig().set(path, value);
    }
    
}
