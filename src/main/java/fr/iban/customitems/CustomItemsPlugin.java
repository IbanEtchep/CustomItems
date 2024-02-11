package fr.iban.customitems;

import fr.iban.customitems.listener.BukkitListeners;
import fr.iban.customitems.listener.JobsRebornListeners;
import fr.iban.customitems.listener.SurvivalCoreListeners;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public final class CustomItemsPlugin extends JavaPlugin {

    private static CustomItemsPlugin instance;
    private CustomAttributeManager attributeManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.attributeManager = new CustomAttributeManager(this);
        registerCommands();
        registerEvent(new BukkitListeners(this));
        if (isEnabled("SurvivalCore")) {
            getLogger().info("Hooked SurvivalCore.");
            registerEvent(new SurvivalCoreListeners(this));
        }

        if (isEnabled("Jobs")) {
            getLogger().info("Hooked Jobs.");
            registerEvent(new JobsRebornListeners(this));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new CustomAttributeCommands(this));
        commandHandler.register(new UpdateItemsCommand(this));
    }

    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private boolean isEnabled(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    public CustomAttributeManager getAttributeManager() {
        return attributeManager;
    }

}
