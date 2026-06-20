package ru.poor.plugin.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;
    private List<String> bannedModules;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");
        
        if (!configFile.exists()) {
            createDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        loadBannedModules();
    }

    private void createDefaultConfig() {
        try {
            configFile.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            // Список забанены модулей
            List<String> modules = new ArrayList<>();
            modules.add("GuiMove");
            modules.add("ItemScroller");
            modules.add("ItemSwapFix");
            modules.add("ItemsCooldown");
            modules.add("NoBounce");
            modules.add("NoDelay");
            modules.add("NoInteract");
            modules.add("NoPush");
            modules.add("ObsidianFarm");
            modules.add("ScoreboardHealth");
            modules.add("ServerJoiner");
            modules.add("Soundpad");
            modules.add("TallCropFarm");
            modules.add("TapeMouse");
            modules.add("TpLoot");
            modules.add("AIUse");
            modules.add("AimingBalls");
            modules.add("AntiFunction");
            modules.add("Assistent");
            modules.add("AuctionAnalyzer");
            modules.add("Auto Crystal");
            modules.add("AutoReple");
            modules.add("BetterChat");
            modules.add("ClickFriend");
            modules.add("ClickPearl");
            modules.add("ClientSounds");
            modules.add("EcOpen");
            modules.add("ItemFix");
            modules.add("ItemHelper");
            modules.add("LeaveTracker");
            modules.add("LockSlot");
            modules.add("Music");
            modules.add("NoCommands");
            modules.add("Scaffold");
            modules.add("ServerHelper");
            modules.add("ShulkerBypass");
            modules.add("StreamerMode");
            modules.add("Teams");
            modules.add("UnHook");
            modules.add("UseTracker");
            modules.add("NameProtect");

            config.set("banned-modules", modules);

            // Настройки эффектов
            config.set("effects.blindness-duration", 600); // 30 сек
            config.set("effects.slowness-duration", 600);
            config.set("effects.slowness-level", 2);

            config.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка создания config.yml: " + e.getMessage());
        }
    }

    private void loadBannedModules() {
        bannedModules = config.getStringList("banned-modules");
    }

    public List<String> getBannedModules() {
        return bannedModules;
    }

    public int getBlindnessDuration() {
        return config.getInt("effects.blindness-duration", 600);
    }

    public int getSlownessDuration() {
        return config.getInt("effects.slowness-duration", 600);
    }

    public int getSlownessLevel() {
        return config.getInt("effects.slowness-level", 2);
    }
}
