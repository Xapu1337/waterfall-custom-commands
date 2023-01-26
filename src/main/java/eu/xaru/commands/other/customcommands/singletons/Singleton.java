package eu.xaru.commands.other.customcommands.singletons;

import eu.xaru.commands.other.customcommands.commands.CustomCommandBase;

import java.util.LinkedHashMap;

public class Singleton {
    private volatile static Singleton instance;
    private Singleton() {}
    public static Singleton getSingleInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public LinkedHashMap<String, CustomCommandBase> customCommands = new LinkedHashMap<>();
}
