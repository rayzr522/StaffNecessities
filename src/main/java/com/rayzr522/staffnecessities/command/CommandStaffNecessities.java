/**
 * 
 */
package com.rayzr522.staffnecessities.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.base.Strings;
import com.rayzr522.staffnecessities.StaffNecessities;

/**
 * @author Rayzr
 *
 */
public class CommandStaffNecessities implements CommandExecutor, Listener {
    private Map<UUID, Long> chatCooldowns = new HashMap<>();

    private List<UUID> staffChat = new ArrayList<>();
    private boolean muteChat = false;
    private boolean slowChat = false;

    private StaffNecessities plugin;

    public CommandStaffNecessities(StaffNecessities plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private long getSlowChatRate() {
        return plugin.getConfig().getLong("slow-chat-rate");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!checkPermission(sender, "base")) {
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.trRaw("command.usage"));
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("clear")) {
            if (!checkPermission(sender, "clear"))
                return true;

            Bukkit.broadcastMessage(Strings.repeat(" \n", 255));
            sender.sendMessage(plugin.tr("command.success.clear"));

            plugin.getSounds().getSound("clear").ifPresent(sound -> {
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 1.0f, 1.0f));
            });

        } else if (sub.equals("mute")) {
            if (!checkPermission(sender, "mute"))
                return true;

            muteChat = !muteChat;
            sender.sendMessage(plugin.tr("command.success.mute", muteChat ? plugin.trRaw("value.enabled") : plugin.trRaw("value.disabled")));

        } else if (sub.equals("slow")) {
            if (!checkPermission(sender, "slow")) {
                return true;
            }

            slowChat = !slowChat;
            chatCooldowns.clear();

            sender.sendMessage(plugin.tr("command.success.slow", slowChat ? plugin.trRaw("value.enabled") : plugin.trRaw("value.disabled")));

        } else if (sub.equals("staff")) {
            if (!checkPermission(sender, "staff")) {
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.tr("command.fail.only-players"));
                return true;
            }

            Player player = (Player) sender;
            if (staffChat.remove(player.getUniqueId())) {
                player.sendMessage(plugin.tr("command.success.staff", plugin.trRaw("value.disabled")));
            } else {
                staffChat.add(player.getUniqueId());
                player.sendMessage(plugin.tr("command.success.staff", plugin.trRaw("value.enabled")));
            }

        } else if (sub.equals("reload")) {
            if (!checkPermission(sender, "reload")) {
                return true;
            }

            plugin.reload();
            sender.sendMessage(plugin.tr("command.success.reload"));

            if (sender instanceof Player) {
                plugin.getSounds().play("reload", (Player) sender);
            }
        } else {
            sender.sendMessage(plugin.trRaw("command.usage"));
        }

        return true;
    }

    private boolean checkPermission(CommandSender sender, String key) {
        if (!plugin.getConfig().isString("permission." + key)) {
            return true;
        } else {
            String permission = plugin.getConfig().getString("permission." + key);
            boolean hasPermission = sender.hasPermission(permission);
            if (!hasPermission) {
                sender.sendMessage(plugin.tr("no-permission", permission));
            }
            return hasPermission;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (staffChat.contains(player.getUniqueId())) {
            e.setCancelled(true);

            staffChat.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(target -> {
                target.sendMessage(plugin.trRaw("chat.staff", player.getDisplayName(), e.getMessage()));
                plugin.getSounds().play("staff", target);
            });

            return;
        }

        if (muteChat) {
            e.setCancelled(true);
            player.sendMessage(plugin.tr("chat.muted"));
            plugin.getSounds().play("mute", player);
            return;
        }

        if (slowChat) {
            long rate = getSlowChatRate();
            long now = System.currentTimeMillis();

            long last = chatCooldowns.getOrDefault(player.getUniqueId(), now - rate);

            if (now - last < rate) {
                e.setCancelled(true);
                player.sendMessage(plugin.tr("chat.slow"));
                plugin.getSounds().play("slow", player);
            } else {
                chatCooldowns.put(player.getUniqueId(), now);
            }
            return;
        }
    }

}
