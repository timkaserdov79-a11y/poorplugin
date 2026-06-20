package ru.poor.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.poor.plugin.PoorPlugin;

public class PluginListener implements Listener, PluginMessageListener {

    private PoorPlugin plugin;

    public PluginListener(PoorPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "siv:auth", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "siv:auth");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getLogger().info("Игрок " + player.getName() + " присоединился!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getLogger().info("Игрок " + player.getName() + " вышел!");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("siv:auth")) {
            return;
        }

        String data = new String(message);
        
        if (data.equals("CONFIRMED")) {
            // Клиент подтвердил что отключил все читы
            plugin.getPunishmentManager().removePunishment(player);
            plugin.getLogger().info("Игрок " + player.getName() + " подтвердил отключение читов!");
        } else {
            // Отправляем ответ с забанены модулями
            String response = plugin.getPunishmentManager().getResponseJson();
            player.sendPluginMessage(plugin, "siv:auth", response.getBytes());
        }
    }
}
