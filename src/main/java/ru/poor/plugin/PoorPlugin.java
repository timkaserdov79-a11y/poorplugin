package ru.poor.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import ru.poor.plugin.commands.PoorCommand;
import ru.poor.plugin.listeners.PluginListener;
import ru.poor.plugin.managers.PunishmentManager;
import ru.poor.plugin.managers.ConfigManager;

public class PoorPlugin extends JavaPlugin {

    private static PoorPlugin instance;
    private PunishmentManager punishmentManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        
        getLogger().info("§a[PoorPlugin] Включаюсь...");
        
        // Загружаем конфиг
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Инициализируем менеджер наказаний
        punishmentManager = new PunishmentManager(this);
        
        // Регистрируем команды
        getCommand("poor").setExecutor(new PoorCommand(this));
        
        // Регистрируем слушатели
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);
        
        getLogger().info("§a[PoorPlugin] Успешно включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("§c[PoorPlugin] Выключаюсь...");
    }

    public static PoorPlugin getInstance() {
        return instance;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
