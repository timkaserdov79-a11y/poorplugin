package ru.poor.plugin.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PunishmentManager {

    private JavaPlugin plugin;
    private Map<UUID, Long> punishedPlayers = new HashMap<>();

    public PunishmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void punishPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        
        // Добавляем слепоту (Blindness)
        int blindDuration = plugin.getConfig().getInt("effects.blindness-duration", 600);
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                blindDuration,
                0,
                false,
                false
        ));

        // Добавляем замедление (Slowness)
        int slowDuration = plugin.getConfig().getInt("effects.slowness-duration", 600);
        int slowLevel = plugin.getConfig().getInt("effects.slowness-level", 2);
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                slowDuration,
                slowLevel - 1,
                false,
                false
        ));

        punishedPlayers.put(uuid, System.currentTimeMillis());
        
        Bukkit.broadcastMessage("§c[PoorPlugin] Игрок §e" + player.getName() + " §cполучил наказание!");
    }

    public void removePunishment(Player player) {
        UUID uuid = player.getUniqueId();
        
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOWNESS);
        
        punishedPlayers.remove(uuid);
        
        Bukkit.broadcastMessage("§a[PoorPlugin] Игрок §e" + player.getName() + " §aснял наказание!");
    }

    public boolean isPunished(Player player) {
        return punishedPlayers.containsKey(player.getUniqueId());
    }

    public String getResponseJson() {
        JsonObject response = new JsonObject();
        JsonArray bannedArray = new JsonArray();
        
        for (String module : plugin.getConfig().getStringList("banned-modules")) {
            bannedArray.add(module);
        }
        
        response.add("banned", bannedArray);
        return "charliedelta" + response.toString();
    }
}
