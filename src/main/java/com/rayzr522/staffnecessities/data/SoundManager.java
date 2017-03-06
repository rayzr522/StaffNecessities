/**
 * 
 */
package com.rayzr522.staffnecessities.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Rayzr
 *
 */
public class SoundManager {
    private Map<String, Sound> sounds = new HashMap<>();
    private Plugin plugin;

    public SoundManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load(ConfigurationSection config) {
        sounds.clear();

        for (String key : config.getKeys(true)) {
            String value = config.get(key).toString();
            if ("none".equalsIgnoreCase(value)) {
                continue;
            }
            try {
                Sound sound = Sound.valueOf(value.toUpperCase().replaceAll("[^A-Z0-9]", "_"));
                sounds.put(key, sound);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, String.format("'%s' is an invalid sound!", value), e);
            }
        }
    }

    public Optional<Sound> getSound(String key) {
        return Optional.ofNullable(sounds.get(key));
    }

    public void play(String key, Player player, float volume, float pitch) {
        if (!sounds.containsKey(key)) {
            return;
        }

        player.playSound(player.getLocation(), sounds.get(key), volume, pitch);
    }

    public void play(String key, Player player) {
        this.play(key, player, 1.0f, 1.0f);
    }

}
