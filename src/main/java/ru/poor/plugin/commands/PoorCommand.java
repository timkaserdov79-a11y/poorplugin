package ru.poor.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.poor.plugin.PoorPlugin;

public class PoorCommand implements CommandExecutor {

    private PoorPlugin plugin;

    public PoorCommand(PoorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("poor.admin")) {
            sender.sendMessage("§cУ вас нет прав!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if (subcommand.equals("players")) {
            return showPlayers(sender);
        } else if (subcommand.equals("nakaz")) {
            if (args.length < 2) {
                sender.sendMessage("§cИспользование: /poor nakaz <ник игрока>");
                return true;
            }
            return punishPlayer(sender, args[1]);
        } else if (subcommand.equals("unakaz")) {
            if (args.length < 2) {
                sender.sendMessage("§cИспользование: /poor unakaz <ник игрока>");
                return true;
            }
            return removePunishment(sender, args[1]);
        } else {
            sendHelp(sender);
        }

        return true;
    }

    private boolean showPlayers(CommandSender sender) {
        sender.sendMessage("§6§l=== Игроки с модом Sipper ===");
        
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            sender.sendMessage("§e• " + player.getName() + " §7(" + player.getUniqueId() + ")");
            count++;
        }
        
        sender.sendMessage("§6Всего: §e" + count + " §6игроков");
        return true;
    }

    private boolean punishPlayer(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        
        if (target == null) {
            sender.sendMessage("§cИгрок §e" + playerName + " §cне найден!");
            return true;
        }

        plugin.getPunishmentManager().punishPlayer(target);
        sender.sendMessage("§aИгрок §e" + target.getName() + " §aполучил наказание!");
        
        target.sendMessage("§c§lВЫКЛЮЧИ ЧИТЫ!");
        target.sendMessage("§cЗабанены модули: §e" + String.join("§7, §e", plugin.getConfigManager().getBannedModules()));
        
        return true;
    }

    private boolean removePunishment(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        
        if (target == null) {
            sender.sendMessage("§cИгрок §e" + playerName + " §cне найден!");
            return true;
        }

        plugin.getPunishmentManager().removePunishment(target);
        sender.sendMessage("§aИгрок §e" + target.getName() + " §aсвободен от наказания!");
        target.sendMessage("§a§lЧиты отключены! Наказание снято!");
        
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== PoorPlugin помощь ===");
        sender.sendMessage("§e/poor players §7- Показать игроков с модом");
        sender.sendMessage("§e/poor nakaz <ник> §7- Выдать наказание");
        sender.sendMessage("§e/poor unakaz <ник> §7- Снять наказание");
    }
}
